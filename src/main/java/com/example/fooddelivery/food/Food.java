package com.example.fooddelivery.food;

import lombok.Getter;

@Getter
public class Food {
    private long id;
    private String name;
    private int price;
    private String explain;
    private String group;

    //private List<Comment> comments;
    //private List<Image> images;

    private String createdAt;
    private String modifiedAt;

    public Food(String name, int price, String explain, String group) {
        this.name = name;
        this.price = price;
        this.explain = explain;
        this.group = group;
    }

    public void updateFood(String name, int price, String explain, String group) {
        this.name = name;
        this.price = price;
        this.explain = explain;
        this.group = group;
    }
}
