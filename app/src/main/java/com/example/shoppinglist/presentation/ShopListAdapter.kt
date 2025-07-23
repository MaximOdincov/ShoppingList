package com.example.shoppinglist.presentation

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.shoppinglist.R
import com.example.shoppinglist.domain.ShopItem

class ShopListAdapter: RecyclerView.Adapter<ShopListAdapter.ShopListViewHolder>() {

    private val shopList = listOf<ShopItem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.active_shop_item, parent, false)
        return ShopListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        holder.name.text = shopList[position].name
        holder.count.text = shopList[position].count.toString()
        holder.itemView.setOnClickListener{
            true
        }
    }

    class ShopListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.item_name)
        val count = view.findViewById<TextView>(R.id.item_count)
    }
}