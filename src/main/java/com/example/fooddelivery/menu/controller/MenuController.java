package com.example.fooddelivery.menu.controller;

import com.example.fooddelivery.menu.dto.CreateMenuReqDto;
import com.example.fooddelivery.menu.dto.MenuResDto;
import com.example.fooddelivery.menu.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/v1/menus")
public class MenuController {

    private final MenuService menuService;

    @Autowired
    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @PostMapping("")
    public ResponseEntity<MenuResDto> createMenu(@RequestBody CreateMenuReqDto requestDto) {
        Long id = menuService.createMenu(requestDto).getId();
        return ResponseEntity.created(URI.create("/api/v1/menus/" + id)).build();
    }

    @GetMapping("")
    public ResponseEntity<List<MenuResDto>> findAllMenu() {
        return ResponseEntity.ok(menuService.findAllMenu());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findMenu(@PathVariable Long id) {
        return ResponseEntity.ok(menuService.findMenu(id));
    }

    @PutMapping("/{id}")
    public void updateMenu(@PathVariable Long id, @RequestBody CreateMenuReqDto reqDto) {
        menuService.updateMenu(id, reqDto);
    }

    @DeleteMapping("/{id}")
    public void deleteMenu(@PathVariable Long id) {
        menuService.deleteMenu(id);
    }
}
