package com.mobdeve.group3.mco

class CategoryGenerator {
    companion object {
        fun generateCategories(): ArrayList<Category> {
            val categories = ArrayList<Category>()

            categories.add(Category("1", "Fish", R.drawable.nemo))

            return categories
        }
    }
}