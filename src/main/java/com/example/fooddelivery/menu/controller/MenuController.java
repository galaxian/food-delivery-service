package com.example.fooddelivery.menu.controller;

import com.example.fooddelivery.menu.dto.CreateMenuReqDto;
import com.example.fooddelivery.menu.dto.MenuDetailResDto;
import com.example.fooddelivery.menu.dto.MenuResDto;
import com.example.fooddelivery.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<Void> createMenu(@RequestBody @Valid CreateMenuReqDto requestDto, @PathVariable Long restaurantId) {
        Long id = menuService.createMenu(requestDto, restaurantId);
        return ResponseEntity.created(URI.create("/api/v1/menus/" + id)).build();
    }

    @GetMapping("/restaurants/{restaurantsId}/menus")
    public ResponseEntity<List<MenuResDto>> findAllMenu(@PathVariable Long restaurantsId) {
        return ResponseEntity.ok(menuService.findAllMenu(restaurantsId));
    }

    @GetMapping("/menus/{id}")
    public ResponseEntity<MenuDetailResDto> findMenu(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.findMenu(id));
    }

    @PutMapping("/menus/{id}")
    public ResponseEntity<Void> updateMenu(@PathVariable Long id, @RequestBody @Valid CreateMenuReqDto reqDto) {
        menuService.updateMenu(id, reqDto);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/menus/{id}")
    public ResponseEntity<Void> deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
        return ResponseEntity.ok().build();
    }
}
