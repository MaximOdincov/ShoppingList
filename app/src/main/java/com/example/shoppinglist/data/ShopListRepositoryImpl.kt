package com.example.shoppinglist.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

object ShopListRepositoryImpl: ShopListRepository{
    private val shopList = sortedSetOf<ShopItem>({ o1, o2 -> o1.id.compareTo(o2.id) })
    private val shopListLiveData = MutableLiveData<List<ShopItem>>()
    private var autoId = 0

    init{
        for(i in 0 until 100){
            val item = ShopItem("Name $i", i, Random.nextBoolean())
            addItemToShopList(item)
        }
    }

    override fun getShopList(): LiveData<List<ShopItem>> {
        return shopListLiveData
    }

    override fun getShopItemById(id: Int): ShopItem {
        return shopList.find{it.id == id} ?: throw RuntimeException("Element with id $id not found")}

    override fun deleteItemInShopList(item: ShopItem) {
        shopList.remove(item)
        updateLiveData()
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
        updateLiveData()
    }

    fun updateLiveData(){
        shopListLiveData.value = shopList.toList()
    }
}