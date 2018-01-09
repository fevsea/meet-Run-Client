package edu.upc.fib.meetnrun.models;


public class Trophie {
    private String id;
    private String title;
    private Integer image_Obtained;
    private Integer image_NotObtained;
    private boolean obtained;
    private String description = "This is the description of the trophie.";

    public Trophie(){

    }
    public Trophie(boolean obtained) {
        this.obtained = obtained;
    }

    //public void setTrophieId(String id){this.id = id;}

    //public String getTrophieId(){return this.id;}

    public void setTrophieTitle(String title) {
        this.title = title;
    }

    public String getTrophieTitle() {
        return this.title;
    }

    public void setTrophieDescription(String description) {
        this.description = description;
    }

    public String getTrophieDescription() {
        return this.description;
    }

    public void setTrophieIsObtained(boolean obtained) {
        this.obtained = obtained;
    }

    public boolean getTrophieIsObtained() {
        return this.obtained;
    }

    public void setImage_Obtained(Integer android_image_url) {
        this.image_Obtained = android_image_url;
    }

    public void setImage_NotObtained(Integer android_image_url) {
        this.image_NotObtained = android_image_url;
    }

    public Integer getImage() {
        if (this.obtained) return this.image_Obtained;
        else return this.image_NotObtained;

    }

}
