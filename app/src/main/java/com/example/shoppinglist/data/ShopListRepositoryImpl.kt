package com.example.shoppinglist.data

import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository

class ShopListRepositoryImpl: ShopListRepository{
    private val shopList = mutableListOf<ShopItem>()
    private var autoId = 0


    override fun getShopList(): List<ShopItem> {
        return shopList.toList()
    }

    override fun getShopItemById(id: Int): ShopItem {
        return shopList.find{it.id == id} ?: throw RuntimeException("Element with id $id not found")}

    override fun deleteItemInShopList(item: ShopItem) {
        shopList.remove(item)
    }

    override fun editItem(item: ShopItem) {
        val oldElement = getShopItemById(item.id)
        deleteItemInShopList(oldElement)
        addItemToShopList(item)
    }

    override fun addItemToShopList(item: ShopItem) {
        if(item.id == ShopItem.UNDEFINED_ID){
            item.id = autoId++
        }
        shopList.add(item)
    }
}