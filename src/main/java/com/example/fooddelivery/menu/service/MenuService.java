package com.example.fooddelivery.menu.service;

import com.example.fooddelivery.common.exception.NotFoundException;
import com.example.fooddelivery.food.domain.Food;
import com.example.fooddelivery.food.repository.FoodRepository;
import com.example.fooddelivery.menu.domain.Menu;
import com.example.fooddelivery.menu.dto.CreateMenuReqDto;
import com.example.fooddelivery.menu.dto.FoodQuantityReqDto;
import com.example.fooddelivery.menu.dto.MenuResDto;
import com.example.fooddelivery.menu.repository.MenuRepository;
import com.example.fooddelivery.menufood.domain.MenuFood;
import com.example.fooddelivery.menufood.repository.MenuFoodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
