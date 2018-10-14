package com.example.android.pijjybank;

public class Category {
    String categoryName;
    int categoryIcon;

    public Category() {
    }

    public Category(String categoryName, int categoryIcon) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public int getCategoryIcon() {
        return categoryIcon;
    }

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    }
}
