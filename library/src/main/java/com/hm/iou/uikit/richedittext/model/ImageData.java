package com.hm.iou.uikit.richedittext.model;

public class ImageData {
    private String imageWidth;
    private String imageHeight;
    private String imagePath;

    public ImageData(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getImageWidth() {
        return imageWidth;
    }

    public void setImageWidth(String imageWidth) {
        this.imageWidth = imageWidth;
    }

    public String getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(String imageHeight) {
        this.imageHeight = imageHeight;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    @Override
    public String toString() {
        return "ImageData{" +
                "imageWidth='" + imageWidth + '\'' +
                ", imageHeight='" + imageHeight + '\'' +
                ", imagePath='" + imagePath + '\'' +
                '}';
    }
}