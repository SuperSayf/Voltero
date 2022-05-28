package com.voltero;

public class CartItemCard {
    private String course_name;
    private String course_image;
    private String course_quantity;

    // Constructor
    public CartItemCard(String course_name, String course_image, String course_quantity) {
        this.course_name = course_name;
        this.course_image = course_image;
        this.course_quantity = course_quantity;
    }

    // Getter and Setter
    public String getCourse_name() {
        return course_name;
    }

    public void setCourse_name(String course_name) {
        this.course_name = course_name;
    }

    public String getCourse_image() {
        return course_image;
    }

    public void setCourse_image(String course_image) { this.course_image = course_image; }

    public String getCourse_quantity() {
        return course_quantity;
    }

    public void setCourse_quantity(String course_name) {
        this.course_quantity = course_quantity;
    }
}
