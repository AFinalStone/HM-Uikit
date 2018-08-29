package com.hm.iou.uikit.richedittext.model;

public class EditData {
    private String inputStr;
    private ImageData imageData;

    public String getInputStr() {
        return inputStr;
    }

    public void setInputStr(String inputStr) {
        this.inputStr = inputStr;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public void setImageData(ImageData imageData) {
        this.imageData = imageData;
    }

    @Override
    public String toString() {
        return "EditData{" +
                "inputStr='" + inputStr + '\'' +
                ", imageData=" + imageData +
                '}';
    }
}