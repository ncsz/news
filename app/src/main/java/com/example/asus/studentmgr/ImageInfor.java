package com.example.asus.studentmgr;

/**
 * Created by asus on 2019/5/6.
 */

public class ImageInfor {
    private String imagePath;
    private String imageName;
    private boolean isChoose = false;
    public ImageInfor(String path, String name){
        this.imagePath = path;
        this.imageName = name;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public boolean isChoose() {
        return isChoose;
    }

    public void setChoose(boolean choose) {
        isChoose = choose;
    }
}
