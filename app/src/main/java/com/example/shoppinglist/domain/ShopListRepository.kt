package com.example.shoppinglist.domain

interface ShopListRepository {
    fun getShopList(): List<ShopItem>
    fun getShopItemById(id: Int): ShopItem
    fun editItem(item: ShopItem)
    fun deleteItemInShopList(item: ShopItem)
    fun addItemToShopList(item: ShopItem)
}