package com.brunonlemanski.shops.database;

/**
 * Database model.
 */
public class ShopModel {

    private String name;
    private String desc;
    private String radius;
    private String location;

    public ShopModel(String name, String desc, String radius, String location) {
        this.name = name;
        this.desc = desc;
        this.radius = radius;
        this.location = location;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getRadius() {
        return radius;
    }

    public void setRadius(String radius) {
        this.radius = radius;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
