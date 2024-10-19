package com.mobdeve.group3.mco

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

class CatalogueCategoriesAdapter(private val data: ArrayList<Category>) :
    RecyclerView.Adapter<CatalogueCategoriesViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogueCategoriesViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_category_catalogue, parent, false)
        return CatalogueCategoriesViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogueCategoriesViewHolder, position: Int) {
        holder.bindData(data.get(position))
    }

    override fun getItemCount(): Int {
        return data.size
    }
}