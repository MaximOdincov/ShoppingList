package com.example.shoppinglist.domain

class AddItemUseCase(private val shopListRepository: ShopListRepository){
    fun addItemToShopList(item: ShopItem){
        shopListRepository.addItemToShopList(item)
    }
}