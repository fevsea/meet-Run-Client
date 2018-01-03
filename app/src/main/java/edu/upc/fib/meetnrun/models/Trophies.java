package edu.upc.fib.meetnrun.models;


import java.util.ArrayList;

public class Trophies {
    private String image_title;
    private Integer image_id;
    //private int position;
    //private String image_description;
    private String image_obtained;

    public String getImage_title() {
        return image_title;
    }

    public String getImage_description() {
        return "This is the description of the trophie.";
    }

    /*public Trophies getTrophieAtPosition(int p) {
        return this;
    }

    public int setTrophie_position(int p) {
        return this.position = p;
    }*/

    public String getImage_isObtained() {
        return image_obtained;
    }

    public void setImage_title(String android_version_name) {
        this.image_title = android_version_name;
    }

    public void setImage_isObtained(String android_version_name) {
        this.image_obtained = android_version_name;
    }

    public Integer getImage_ID() {
        return image_id;
    }

    public void setImage_ID(Integer android_image_url) {
        this.image_id = android_image_url;
    }

}
