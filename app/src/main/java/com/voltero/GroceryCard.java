package com.voltero;

public class GroceryCard {
    private String course_name;
    private String course_image;

    // Constructor
    public GroceryCard(String course_name, String course_image) {
        this.course_name = course_name;
        this.course_image = course_image;
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
}
