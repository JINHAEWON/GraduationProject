package com.example.test;

import android.graphics.drawable.Drawable;
import android.provider.ContactsContract;
import android.widget.ImageView;

public class ListViewItem {
    public ImageView icon ;
    public String text ;
    public boolean chk;

    public void setIcon(ImageView icon) {
        this.icon = icon ;
    }
    public void setText(String text) {
        this.text = text ;
    }

    public ImageView getIcon() {
        return this.icon ;
    }

    public String getText() {
        return this.text ;
    }
}