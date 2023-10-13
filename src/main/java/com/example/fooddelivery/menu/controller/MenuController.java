package com.example.fooddelivery.menu.controller;

import com.example.fooddelivery.common.interceptor.Authenticated;
import com.example.fooddelivery.common.resolver.OwnerIdentifier;
import com.example.fooddelivery.menu.dto.CreateMenuReqDto;
import com.example.fooddelivery.menu.dto.AdminMenuDetailResDto;
import com.example.fooddelivery.menu.dto.AdminMenuResDto;
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

    @Authenticated
    @PostMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<Void> createMenu(@OwnerIdentifier String identifier,
        @RequestBody @Valid CreateMenuReqDto requestDto,
        @PathVariable Long restaurantId) {
        Long id = menuService.createMenu(identifier, requestDto, restaurantId);
        return ResponseEntity.created(URI.create("/api/v1/menus/" + id)).build();
    }

    @Authenticated
    @GetMapping("/restaurants/{restaurantId}/menus/admin")
    public ResponseEntity<List<AdminMenuResDto>> adminFindAllMenu(@OwnerIdentifier String identifier,
        @PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuService.adminFindAllMenu(identifier, restaurantId));
    }

    @GetMapping("/restaurants/{restaurantId}/menus")
    public ResponseEntity<List<MenuResDto>> findAllMenu(@PathVariable Long restaurantId) {
        return ResponseEntity.ok(menuService.findAllMenu(restaurantId));
    }

    @GetMapping("/menus/{id}/admin")
    public ResponseEntity<AdminMenuDetailResDto> adminFindMenu(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.adminFindMenu(id));
    }

    @Authenticated
    @PutMapping("/restaurants/{restaurantsId}/menus/{menuId}")
    public ResponseEntity<Void> updateMenu(@OwnerIdentifier String identifier,
        @PathVariable Long restaurantsId,
        @PathVariable Long menuId,
        @RequestBody @Valid CreateMenuReqDto reqDto) {
        menuService.updateMenu(identifier, restaurantsId, menuId, reqDto);
        return ResponseEntity.ok().build();
    }

    @Authenticated
    @DeleteMapping("/restaurants/{restaurantsId}/menus/{menuId}")
    public ResponseEntity<Void> deleteMenu(@OwnerIdentifier String identifier,
        @PathVariable Long restaurantsId,
        @PathVariable Long menuId) {
        menuService.deleteMenu(identifier, restaurantsId, menuId);
        return ResponseEntity.ok().build();
    }
}
