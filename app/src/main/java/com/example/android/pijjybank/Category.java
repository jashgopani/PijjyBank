package com.example.android.pijjybank;

public class Category {
    String categoryName;
    int categoryIcon;

    public Category() {
    } //default constructor

    public Category(String categoryName, int categoryIcon) {
        this.categoryName = categoryName;
        this.categoryIcon = categoryIcon;
    } //constructor for setting the name and icon

    public String getCategoryName() {
        return categoryName;
    } //fetching the category name

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    } //setting category name

    public int getCategoryIcon() {
        return categoryIcon;
    } //fetching category icon

    public void setCategoryIcon(int categoryIcon) {
        this.categoryIcon = categoryIcon;
    } //setting category icon
}
