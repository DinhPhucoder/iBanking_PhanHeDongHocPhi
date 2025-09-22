package com.example.ibanking_phanhedonghocphi.model;

public class MenuItem {
    private int iconResId;
    private String title;

    public MenuItem(int iconResId, String title) {
        this.iconResId = iconResId;
        this.title = title;
    }

    public int getIconResId() {
        return iconResId;
    }
    public String getTitle() {
        return title;
    }

    public void setIconResId(int iconResId) {
        this.iconResId = iconResId;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

