package com.hm.iou.uikit.richedittext.itemview;

public class RichItemData {
    private String text;
    private String src;
    private int height;
    private int width;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
    public String toString() {
        return "RichItemData{" +
                "text='" + text + '\'' +
                ", src='" + src + '\'' +
                ", height=" + height +
                ", width=" + width +
                '}';
    }
}