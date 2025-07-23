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

    companion object{
        const val VIEW_TYPE_ENABLED = 1
        const val VIEW_TYPE_DISABLED = 0
        const val MAX_POOL_SIZE = 15
    }
    var shopItemLongClickListener: ((ShopItem) -> Unit)? = null
    var shopItemClickListener: ((ShopItem) -> Unit)? = null


    var shopList = listOf<ShopItem>()
        @SuppressLint("NotifyDataSetChanged")
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ShopListViewHolder {
        val view = if(viewType == VIEW_TYPE_ENABLED) LayoutInflater.from(parent.context).inflate(R.layout.active_shop_item, parent, false)
        else LayoutInflater.from(parent.context).inflate(R.layout.inactive_shop_item, parent, false)
        return ShopListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return shopList.size
    }

    override fun getItemViewType(position: Int): Int {
        return if(shopList[position].enabled) VIEW_TYPE_ENABLED
        else VIEW_TYPE_DISABLED
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ShopListViewHolder, position: Int) {
        holder.name.text = shopList[position].name
        holder.count.text = shopList[position].count.toString()
        holder.itemView.setOnLongClickListener{
            shopItemLongClickListener?.invoke(shopList[position])
            true
        }
        holder.itemView.setOnClickListener{
            shopItemClickListener?.invoke(shopList[position])
        }

    }

    class ShopListViewHolder(private val view: View) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.item_name)
        val count = view.findViewById<TextView>(R.id.item_count)
    }

    interface OnShopItemLongClickListener{
        fun onClick(shopItem: ShopItem)
    }

    interface OnShopItemClickListener{
        fun OnClick(shopItem: ShopItem)
    }


}