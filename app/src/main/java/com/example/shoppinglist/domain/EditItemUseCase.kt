package com.example.shoppinglist.domain

class EditItemUseCase(private val shopListRepository: ShopListRepository) {
    fun editItem(item: ShopItem) {
        shopListRepository.editItem(item)
    }

}