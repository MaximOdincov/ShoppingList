package com.example.shoppinglist.domain

class DeleteItemUseCase(private val shopListRepository: ShopListRepository){
    suspend fun deleteItemInShopList(item: ShopItem) {
        shopListRepository.deleteItemInShopList(item)
    }
}