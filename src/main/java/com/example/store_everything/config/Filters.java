package com.example.store_everything.config;

public class Filters {

    private String categoryFilter, dateFilter;

    public String getCategoryFilter() {
        return categoryFilter;
    }

    public void setCategoryFilter(String categoryFilter) {
        this.categoryFilter = categoryFilter;
    }

    public String getDateFilter() {
        return dateFilter;
    }

    public void setDateFilter(String dateFilter) {
        this.dateFilter = dateFilter;
    }

    private String categorySort, alphabeticalSort;

    public String getCategorySort() {
        return categorySort;
    }

    public void setCategorySort(String categorySort) {
        this.categorySort = categorySort;
    }

    public String getAlphabeticalSort() {
        return alphabeticalSort;
    }

    public void setAlphabeticalSort(String alphabeticalSort) {
        this.alphabeticalSort = alphabeticalSort;
    }
}
