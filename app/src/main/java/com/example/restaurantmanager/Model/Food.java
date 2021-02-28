package com.example.restaurantmanager.Model;



public class Food {
    private String Name, Image,MenuId;
    private Long Price;

    public Food() {
    }


    public Food(String name, String image, Long price, String menuId) {
        Name = name;
        Image = image;
        Price = price;
        MenuId = menuId;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public Long getPrice() {
        return Price;
    }

    public void setPrice(Long price) {
        Price = price;
    }

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }
}
