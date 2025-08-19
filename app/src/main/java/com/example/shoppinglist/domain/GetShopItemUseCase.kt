package com.example.shoppinglist.domain

class GetShopItemUseCase(private val shopListRepository: ShopListRepository){
    suspend fun getShopItemById(id: Int): ShopItem {
        return shopListRepository.getShopItemById(id)
    }
}