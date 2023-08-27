package com.example.fooddelivery.menu.service;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.dto.*;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.menufood.domain.MenuFood;
import com.example.fooddelivery.menufood.repository.MenuFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;
    private final FoodRepository foodRepository;
    private final MenuFoodRepository menuFoodRepository;

    @Autowired
    public MenuService(MenuRepository menuRepository, FoodRepository foodRepository, MenuFoodRepository menuFoodRepository) {
        this.menuRepository = menuRepository;
        this.foodRepository = foodRepository;
        this.menuFoodRepository = menuFoodRepository;
    }

    @Transactional
    public MenuResDto createMenu(CreateMenuReqDto requestDto) {
        Menu saveMenu = menuRepository.save(requestDto.toEntity());

        List<FoodQuantityReqDto> foodIdQuantityList = requestDto.getFoodReqList();
        for (FoodQuantityReqDto req : foodIdQuantityList) {
            Food food = foodRepository.findById(req.getId()).orElseThrow(
                    () -> new NotFoundException("음식을 찾지 못했습니다.")
            );

            MenuFood menuFood = MenuFood.createMenuFood(req.getQuantity(), saveMenu, food);
            menuFoodRepository.save(menuFood);
        }

        return new MenuResDto(saveMenu);
    }

    @Transactional
    public List<MenuResDto> findAllMenu() {
        List<Menu> menuList = menuRepository.findAll();

        return menuList.stream().map(MenuResDto::new).collect(Collectors.toList());
    }

    @Transactional
    public MenuDetailResDto findMenu(Long id) {
        Menu menu = menuRepository.findById(id).orElseThrow(
                () -> new NotFoundException("메뉴가 존재하지 않습니다.")
        );

        List<MenuFood> menuFoodList = menuFoodRepository.findByMenuId(id);

        List<MenuFoodResDto> resDtoList = menuFoodList.stream().map(MenuFoodResDto::new).collect(Collectors.toList());

        return new MenuDetailResDto(menu, resDtoList);
    }

    @Transactional
    public void updateMenu(Long id, CreateMenuReqDto reqDto) {
        Menu menu = menuRepository.findById(id).orElseThrow(
                () -> new NotFoundException("메뉴가 존재하지 않습니다.")
        );

        menu.updateMenu(reqDto.getName(), reqDto.getPrice(), reqDto.getDescribe());

        if (reqDto.getFoodReqList() != null) {
            List<FoodQuantityReqDto> foodIdQuantityList = reqDto.getFoodReqList();

            menuFoodRepository.deleteAllByMenuId(menu.getId());

            for (FoodQuantityReqDto req : foodIdQuantityList) {
                Food food = foodRepository.findById(req.getId()).orElseThrow(
                        () -> new NotFoundException("음식을 찾지 못했습니다.")
                );

                MenuFood menuFood = MenuFood.createMenuFood(req.getQuantity(), menu, food);
                menuFoodRepository.save(menuFood);
            }

        }
    }

    @Transactional
    public void deleteMenu(Long id) {
        menuFoodRepository.deleteAllByMenuId(id);
        menuRepository.deleteById(id);
    }
}
