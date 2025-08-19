package com.example.shoppinglist.domain

class EditItemUseCase(private val shopListRepository: ShopListRepository) {
    suspend fun editItem(item: ShopItem) {
        shopListRepository.editItem(item)
    }

}