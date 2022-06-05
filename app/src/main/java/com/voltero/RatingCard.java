package com.voltero;

public class RatingCard {
    private String rator_email;
    private String rator_rating;
    private String rator_name;
    private String rator_comment;
    private String rator_image;

    // Constructor
    public RatingCard(String rator_email, String rator_rating, String rator_name, String rator_comment, String rator_image) {
        this.rator_email = rator_email;
        this.rator_rating = rator_rating;
        this.rator_name = rator_name;
        this.rator_comment = rator_comment;
        this.rator_image = rator_image;
    }

    public String getRator_email() {
        return rator_email;
    }

    public void setRator_email(String rator_email) {
        this.rator_email = rator_email;
    }

    public String getRator_rating() {
        return rator_rating;
    }

    public void setRator_rating(String rator_rating) {
        this.rator_rating = rator_rating;
    }

    public String getRator_name() {
        return rator_name;
    }

    public void setRator_name(String rator_name) {
        this.rator_name = rator_name;
    }

    public String getRator_comment() {
        return rator_comment;
    }

    public void setRator_comment(String rator_comment) { this.rator_comment = rator_comment; }

    public String getRator_image() {
        return rator_image;
    }
    public void setRator_image(String rator_image) { this.rator_image = rator_image; }
}
