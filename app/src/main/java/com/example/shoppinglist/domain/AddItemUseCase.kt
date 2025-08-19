package com.example.shoppinglist.domain

class AddItemUseCase(private val shopListRepository: ShopListRepository){
    suspend fun addItemToShopList(item: ShopItem){
        shopListRepository.addItemToShopList(item)
    }
}