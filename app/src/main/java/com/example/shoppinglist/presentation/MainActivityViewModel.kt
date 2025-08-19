package com.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.DeleteItemUseCase
import com.example.shoppinglist.domain.EditItemUseCase
import com.example.shoppinglist.domain.GetShopListUseCase
import com.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class MainActivityViewModel(app: Application): AndroidViewModel(app) {
    private val shopListRepository = ShopListRepositoryImpl(app)

    private val getShopListUseCase = GetShopListUseCase(shopListRepository)
    private val deleteItemUseCase = DeleteItemUseCase(shopListRepository)
    private val editItemUseCase = EditItemUseCase(shopListRepository)
    val shopList = shopListRepository.getShopList()

    fun deleteItem(item: ShopItem){
        viewModelScope.launch {
            deleteItemUseCase.deleteItemInShopList(item)
        }
    }

    fun editItem(item: ShopItem){
        viewModelScope.launch {
            val newItem = item.copy(enabled = !item.enabled)
            editItemUseCase.editItem(newItem)
        }

    }

}