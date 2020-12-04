package com.example.test;

import android.widget.ImageView;

import android.widget.ImageView;

public class BuyingViewItem {
    public ImageView icon ;
    public String text ;
    public String text2;
    public boolean chk;

    public void setIcon(ImageView icon) {
        this.icon = icon ;
    }
    public void setText(String text) {
        this.text = text ;
    }
    public void setText2(String text) {
        this.text2 = text ;
    }

    public ImageView getIcon() {
        return this.icon ;
    }

    public String getText() {
        return this.text ;
    }
    public String getText2() {
        return this.text2 ;
    }
}