package com.example.shoppinglist.data

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.example.shoppinglist.domain.ShopItem
import com.example.shoppinglist.domain.ShopListRepository
import kotlin.random.Random

class ShopListRepositoryImpl(app: Application): ShopListRepository{

    private val shopListDao = AppDatabase.getInstance(app).shopListDao()
    private val mapper = ShopListMapper()

    override fun getShopList(): LiveData<List<ShopItem>> {
        return MediatorLiveData<List<ShopItem>>().apply {
            addSource(shopListDao.getShopList()){
                value = mapper.mapListDbModelToListEntity(it)
        }}
    }

    override fun getShopItemById(id: Int): ShopItem {
        val dbModel = shopListDao.getShopItem(id)
        return mapper.mapDbModelToEntity(dbModel)}

    override fun deleteItemInShopList(item: ShopItem) {
        shopListDao.deleteShopItem(item.id)
    }

    override fun editItem(item: ShopItem) {
        addItemToShopList(item)
    }

    override fun addItemToShopList(item: ShopItem) {
        shopListDao.addShopItem(mapper.mapEntityToDbModel(item))
    }
}