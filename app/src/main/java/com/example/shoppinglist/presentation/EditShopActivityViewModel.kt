package com.example.shoppinglist.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.ShopListRepositoryImpl
import com.example.shoppinglist.domain.AddItemUseCase
import com.example.shoppinglist.domain.EditItemUseCase
import com.example.shoppinglist.domain.GetShopItemUseCase
import com.example.shoppinglist.domain.ShopItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class EditShopActivityViewModel(app: Application): AndroidViewModel(app) {
    private val shopListRepository = ShopListRepositoryImpl(app)

    private val addItemUseCase = AddItemUseCase(shopListRepository)
    private val editItemUseCase = EditItemUseCase(shopListRepository)
    private val getShopItemUseCase = GetShopItemUseCase(shopListRepository)
    private val scope = CoroutineScope(Dispatchers.IO)

    private var _inputNameException = MutableLiveData<Boolean>()
    val inputNameException: LiveData<Boolean>
        get() = _inputNameException

    private var _currentItem = MutableLiveData<ShopItem>()
    val currentItem: LiveData<ShopItem>
        get() = _currentItem

    private var _isFinished = MutableLiveData<Unit>()
    val isFinished: LiveData<Unit>
        get() = _isFinished

    private var _inputCountException = MutableLiveData<Boolean>()
    val inputCountException: LiveData<Boolean>
        get() = _inputCountException

    fun addItem(inputName: String?, inputCount: String?){
        viewModelScope.launch {
            val name = parseName(inputName)
            val count = parseCount(inputCount)
            if(validateData(name,count)){
                val item = ShopItem(name, count, true)
                addItemUseCase.addItemToShopList(item)
            }
            _isFinished.value = Unit
        }

    }

    fun editItem(inputName: String?, inputCount: String?){
        viewModelScope.launch {
            val name = parseName(inputName)
            val count = parseCount(inputCount)
            if(validateData(name,count)){
                _currentItem.value?.let{
                    val item = it.copy(name = name, count = count)
                    editItemUseCase.editItem(item)
                    _isFinished.value = Unit
                }
            }
        }


    }

    fun getShopItem(id: Int){
        viewModelScope.launch {
            val item = getShopItemUseCase.getShopItemById(id)
            _currentItem.value = item
        }

    }

    private fun parseName(inputName: String?): String{
        return inputName?.trim()?: ""
    }

    private fun parseCount(inputCount: String?): Int{
        var count = 0
        kotlin.runCatching{
            count = inputCount?.trim()?.toInt() ?: 0
        }
        return count
    }

    private fun validateData(name: String, count: Int): Boolean{
        var result = true
        if(name.isBlank()) {
            result = false
            _inputNameException.value = true
        }
        if(count<=0){
            result = false
            _inputCountException.value = true
        }
        return result
    }


    fun resetInputNameException(){
        _inputNameException.value = false
    }

    fun resetInputCountException(){
        _inputCountException.value = false
    }
}