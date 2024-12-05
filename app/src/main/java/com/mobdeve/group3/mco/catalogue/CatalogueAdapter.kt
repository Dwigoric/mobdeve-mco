package com.mobdeve.group3.mco.catalogue

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mobdeve.group3.mco.R

class CatalogueAdapter(private val data: ArrayList<CatalogueSpecies>) :
    RecyclerView.Adapter<CatalogueViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CatalogueViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_category_catalogue, parent, false)
        return CatalogueViewHolder(view)
    }

    override fun onBindViewHolder(holder: CatalogueViewHolder, position: Int) {
        holder.bindData(data[position])
    }

    override fun getItemCount(): Int {
        return data.size
    }
}