package com.voltero;

public class OrderCard {
    private String course_name;
    private String course_image;
    private String shopperName;
    private String shopperAddress;

    // Constructor
    public OrderCard(String course_name, String shopperName, String shopperAddress, String course_image) {
        this.course_name = course_name;
        this.shopperName = shopperName;
        this.shopperAddress = shopperAddress;
        this.course_image = course_image;
    }


    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getShopper_name() {
        return shopperName;
    }

    public void setShopper_name(String shopperName) {
        this.shopperName = shopperName;
    }

    public String getShopper_address() {
        return shopperAddress;
    }

    public void setShopper_address(String shopperAddress) {
        this.shopperAddress = shopperAddress;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) { this.course_image = course_image; }
}
