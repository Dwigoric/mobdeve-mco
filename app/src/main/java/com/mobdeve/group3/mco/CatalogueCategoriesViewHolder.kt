package com.mobdeve.group3.mco

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView.ViewHolder

class CatalogueCategoriesViewHolder(itemView: View) : ViewHolder(itemView) {
    private val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
    private val txtCategoryName: TextView = itemView.findViewById(R.id.txtCategoryName)

    fun bindData(category: Category) {
        imgCategory.setImageResource(category.imageId)
        txtCategoryName.text = category.name
    }
}