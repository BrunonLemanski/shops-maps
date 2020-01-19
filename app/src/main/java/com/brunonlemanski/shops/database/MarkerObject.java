package com.brunonlemanski.shops.database;


public class MarkerObject {

    private String name;
    private String desc;
    private String radius;

    public MarkerObject(String name, String desc, String radius) {
        this.name = name;
        this.desc = desc;
        this.radius = radius;
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

}
