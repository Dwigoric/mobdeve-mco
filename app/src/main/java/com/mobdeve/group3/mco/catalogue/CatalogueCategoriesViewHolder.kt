package com.mobdeve.group3.mco.catalogue

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.R

class CatalogueCategoriesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    private val imgCategory: ImageView = itemView.findViewById(R.id.imgCategory)
    private val txtCategoryName: TextView = itemView.findViewById(R.id.txtCategoryName)

    fun bindData(category: Category) {
        imgCategory.setImageResource(category.imageId)
        txtCategoryName.text = category.name
    }
}