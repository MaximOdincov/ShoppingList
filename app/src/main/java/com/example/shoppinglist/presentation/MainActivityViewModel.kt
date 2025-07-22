package com.example.shoppinglist.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.DeleteItemUseCase
import com.example.shoppinglist.domain.EditItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopItem

class MainActivityViewModel: ViewModel() {
    private val shopListRepository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(shopListRepository)
    private val deleteItemUseCase = DeleteItemUseCase(shopListRepository)
    private val editItemUseCase = EditItemUseCase(shopListRepository)

    val shopList = MutableLiveData<List<ShopItem>>()

    fun getShopList(){
       shopList.value = getShopListUseCase.getShopList()
    }

    fun deleteItem(item: ShopItem){
        deleteItemUseCase.deleteItemInShopList(item)
        getShopList()
    }

    fun editItem(item: ShopItem){
        val newItem = item.copy(enabled = !item.enabled)
        editItemUseCase.editItem(newItem)
        getShopList()
    }

}