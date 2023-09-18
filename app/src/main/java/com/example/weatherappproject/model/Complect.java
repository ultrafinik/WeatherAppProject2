package com.example.weatherappproject.model;

import java.io.Serializable;

public class Complect implements Serializable
{
    private int temp1;
    private int temp2;
    private String footwear;
    private String headgear;
    private String outerwear;
    private String Pants;
    private String Shirt;
    private String email;

    public Complect(int temp1, int temp2, String footwear, String headgear, String outerwear, String pants, String shirt, String email) {
        this.temp1 = temp1;
        this.temp2 = temp2;
        this.footwear = footwear;
        this.headgear = headgear;
        this.outerwear = outerwear;
        Pants = pants;
        Shirt = shirt;
        this.email = email;
    }
    public Complect() {
    }

    public int getTemp1() {
        return temp1;
    }

    public void setTemp1(int temp1) {
        this.temp1 = temp1;
    }

    public int getTemp2() {
        return temp2;
    }

    public void setTemp2(int temp2) {
        this.temp2 = temp2;
    }

    public String getFootwear() {
        return footwear;
    }

    public void setFootwear(String footwear) {
        this.footwear = footwear;
    }

    public String getHeadgear() {
        return headgear;
    }

    public void setHeadgear(String headgear) {
        this.headgear = headgear;
    }

    public String getOuterwear() {
        return outerwear;
    }

    public void setOuterwear(String outerwear) {
        this.outerwear = outerwear;
    }

    public String getPants() {
        return Pants;
    }

    public void setPants(String pants) {
        Pants = pants;
    }

    public String getShirt() {
        return Shirt;
    }

    public void setShirt(String shirt) {
        Shirt = shirt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
