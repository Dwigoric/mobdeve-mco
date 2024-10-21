package com.mobdeve.group3.mco.catalogue

import com.mobdeve.group3.mco.R

class CategoryGenerator {
    companion object {
        fun generateCategories(): ArrayList<Category> {
            val categories = ArrayList<Category>()

            categories.add(Category("1", "Fish", R.drawable.fish))
            categories.add(Category("2", "Mammals", R.drawable.mammals))
            categories.add(Category("3", "Crustaceans", R.drawable.crustaceans))
            categories.add(Category("4", "Mollusks", R.drawable.mollusks))
            categories.add(Category("5", "Cnidarians", R.drawable.cnidarians))
            categories.add(Category("6", "Echinoderms", R.drawable.echinoderms))
            categories.add(Category("7", "Reptiles", R.drawable.reptiles))
            categories.add(Category("8", "Birds", R.drawable.birds))

            return categories
        }
    }
}