package com.example.unipiandroid;

import android.graphics.drawable.Drawable;

import androidx.fragment.app.Fragment;

public class MenuModel {

    public String menuName;
    public boolean hasChildren, isGroup;
    public Fragment fragmentClass;
    public Drawable icon;

    public MenuModel(String menuName, boolean isGroup, boolean hasChildren, Fragment fragmentClass,Drawable icon) {

        this.menuName = menuName;
        this.fragmentClass = fragmentClass;
        this.isGroup = isGroup;
        this.hasChildren = hasChildren;
        this.icon = icon;
    }
}