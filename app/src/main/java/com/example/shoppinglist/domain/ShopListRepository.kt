package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {
    fun getShopList(): LiveData<List<ShopItem>>
    fun getShopItemById(id: Int): ShopItem
    fun editItem(item: ShopItem)
    fun deleteItemInShopList(item: ShopItem)
    fun addItemToShopList(item: ShopItem)
}