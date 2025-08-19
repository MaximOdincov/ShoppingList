package com.example.shoppinglist.domain

import androidx.lifecycle.LiveData

interface ShopListRepository {
    suspend fun getShopList(): LiveData<List<ShopItem>>
    suspend fun getShopItemById(id: Int): ShopItem
    suspend fun editItem(item: ShopItem)
    suspend fun deleteItemInShopList(item: ShopItem)
    suspend fun addItemToShopList(item: ShopItem)
}