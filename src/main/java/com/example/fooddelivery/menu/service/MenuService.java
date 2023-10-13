package com.example.fooddelivery.menu.service;

import com.example.fooddelivery.common.exception.BadRequestException;
import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.common.exception.UnauthorizedException;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.dto.*;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.menufood.domain.MenuFood;
import com.example.fooddelivery.menufood.repository.MenuFoodRepository;
import com.example.fooddelivery.restaurant.domain.Restaurant;
import com.example.fooddelivery.restaurant.repository.RestaurantRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final FoodRepository foodRepository;
    private final MenuFoodRepository menuFoodRepository;
    private final RestaurantRepository restaurantRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, FoodRepository foodRepository, MenuFoodRepository menuFoodRepository, RestaurantRepository restaurantRepository) {
        this.menuRepository = menuRepository;
        this.foodRepository = foodRepository;
        this.menuFoodRepository = menuFoodRepository;
        this.restaurantRepository = restaurantRepository;
    }

    @Transactional
    public Long createMenu(String identifier, CreateMenuReqDto requestDto, Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        validateOwner(identifier, restaurant);
        Menu menu = convertToMenu(requestDto, restaurant);
        Menu saveMenu = menuRepository.save(menu);
        List<MenuFood> menuFoodList = makeMenuFoodList(requestDto.getFoodReqList(), saveMenu);
        validateMenuPrice(menuFoodList, menu);
        menuFoodRepository.saveAll(menuFoodList);
        return saveMenu.getId();
    }

    private void validateOwner(String identifier, Restaurant restaurant) {
        if (!restaurant.isOwner(identifier)) {
            throw new UnauthorizedException("식당 주인만 사용할 수 있는 기능입니다.");
        }
    }

    private int getFoodsPrice(List<MenuFood> menuFoodList) {
        return menuFoodList.stream()
            .map(MenuFood::getTimeQuantityAndPrice)
            .reduce(0, Integer::sum);
    }

    private void validateMenuPrice(List<MenuFood> menuFoodList, Menu menu) {
        int sumFoodPrice = getFoodsPrice(menuFoodList);
        if (menu.isFairPrice(sumFoodPrice)) {
            throw new BadRequestException("메뉴 가격은 구성된 음식 가격의 합보다 같거나 작아야 합니다.");
        }
    }

    private List<MenuFood> makeMenuFoodList(List<FoodQuantityReqDto> reqDtoList, Menu menu) {
        return reqDtoList.stream()
            .map((req) -> MenuFood.createMenuFood(req.getQuantity(), menu, findFoodById(req.getId())))
            .collect(Collectors.toList());
    }

    private Food findFoodById(Long foodId) {
        return foodRepository.findById(foodId).orElseThrow(
            () -> new NotFoundException("음식을 찾지 못했습니다.")
        );
    }

    private Menu convertToMenu(CreateMenuReqDto requestDto, Restaurant restaurant) {
        return Menu.createMenu(requestDto.getName(), requestDto.getPrice(),
            requestDto.getDescribe(), restaurant);
    }

    private Restaurant findRestaurantById(Long restaurantId) {
        return restaurantRepository.findById(restaurantId).orElseThrow(
                () -> new NotFoundException("식당을 찾을 수 없습니다.")
        );
    }

    @Transactional(readOnly = true)
    public List<AdminMenuResDto> adminFindAllMenu(String identifier, Long restaurantId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        validateOwner(identifier, restaurant);
        List<Menu> menuList = findMenusByRestaurantId(restaurantId);
        return makeAdminMenuResDtoList(menuList);
    }

    private List<Menu> findMenusByRestaurantId(Long restaurantId) {
        return menuRepository.findAllByRestaurantId(restaurantId);
    }

    private List<AdminMenuResDto> makeAdminMenuResDtoList(List<Menu> menuList) {
        return menuList.stream()
            .map(AdminMenuResDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<MenuResDto> findAllMenu(Long restaurantId) {
        List<Menu> menuList = findMenusByRestaurantId(restaurantId);
        return makeMenuResDtoList(menuList);
    }

    private List<MenuResDto> makeMenuResDtoList(List<Menu> menuList) {
        return menuList.stream()
            .map(MenuResDto::new)
            .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public AdminMenuDetailResDto adminFindMenu(String identifier, Long restaurantId, Long menuId) {
        Restaurant restaurant = findRestaurantById(restaurantId);
        validateOwner(identifier, restaurant);
        Menu menu = findMenuById(menuId);
        List<MenuFood> menuFoodList = findMenuFoodById(menuId);
        List<MenuFoodResDto> resDtoList = makeMenuFoodResDtoList(menuFoodList);
        return new AdminMenuDetailResDto(menu, resDtoList);
    }

    private List<MenuFood> findMenuFoodById(Long id) {
        return menuFoodRepository.findByMenuId(id);
    }

    private List<MenuFoodResDto> makeMenuFoodResDtoList(List<MenuFood> menuFoodList) {
        return menuFoodList.stream()
            .map(MenuFoodResDto::new)
            .collect(Collectors.toList());
    }

    private Menu findMenuById(Long id) {
        return menuRepository.findById(id).orElseThrow(
            () -> new NotFoundException("메뉴가 존재하지 않습니다.")
        );
    }

    @Transactional(readOnly = true)
    public MenuDetailResDto findMenu(Long menuId) {
        Menu menu = findMenuById(menuId);
        List<MenuFood> menuFoodList = findMenuFoodById(menuId);
        List<MenuFoodResDto> resDtoList = makeMenuFoodResDtoList(menuFoodList);
        return new MenuDetailResDto(menu, resDtoList);
    }

    @Transactional
    public void updateMenu(String identifier, Long restaurantsId, Long menuId, CreateMenuReqDto reqDto) {
        Restaurant restaurant = findRestaurantById(restaurantsId);
        validateOwner(identifier, restaurant);
        Menu menu = findMenuById(menuId);
        updateMenu(reqDto, menu);
        validateFoodReq(reqDto.getFoodReqList());
        menuFoodRepository.deleteAllByMenuId(menu.getId());
        List<MenuFood> menuFoodList = makeMenuFoodList(reqDto.getFoodReqList(), menu);
        validateMenuPrice(menuFoodList, menu);
        menuFoodRepository.saveAll(menuFoodList);
    }

    private void validateFoodReq(List<FoodQuantityReqDto> reqDtoList) {
        if (reqDtoList.isEmpty()) {
            throw new BadRequestException("메뉴에 들어갈 음식 정보가 없습니다.");
        }
    }

    private void updateMenu(CreateMenuReqDto reqDto, Menu menu) {
        menu.updateMenu(reqDto.getName(), reqDto.getPrice(), reqDto.getDescribe());
    }

    @Transactional
    public void deleteMenu(String identifier, Long restaurantsId, Long id) {
        Restaurant restaurant = findRestaurantById(restaurantsId);
        validateOwner(identifier, restaurant);
        menuFoodRepository.deleteAllByMenuId(id);
        menuRepository.deleteById(id);
    }
}
