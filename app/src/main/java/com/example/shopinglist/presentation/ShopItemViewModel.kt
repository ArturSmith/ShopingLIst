package com.example.shopinglist.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shopinglist.data.ShopListRepositoryImpl
import com.example.shopinglist.domain.AddShopItemUseCase
import com.example.shopinglist.domain.EditShopItemUseCase
import com.example.shopinglist.domain.GetShopItemUseCase
import com.example.shopinglist.domain.ShopItem
import java.lang.Exception

class ShopItemViewModel : ViewModel() {
    // TODO: This is wrong temporary implementation of calling repository in view model, should be fixed!
    private val rep = ShopListRepositoryImpl
    private val getShopItemUseCase = GetShopItemUseCase(rep)
    private val addShopItemUseCase = AddShopItemUseCase(rep)
    private val editShopItemUseCase = EditShopItemUseCase(rep)

    private val _errorInputName = MutableLiveData<Boolean>()
    val errorInputName: LiveData<Boolean>
        get() = _errorInputName

    private val _errorInputCount = MutableLiveData<Boolean>()
    val errorInputCount: LiveData<Boolean>
        get() = _errorInputCount

    private val _shopItemGetter = MutableLiveData<ShopItem>()
    val shopItemGetter: LiveData<ShopItem>
        get() = _shopItemGetter


    private val _shouldFinishActivity = MutableLiveData<Unit>()
    val shouldFinishActivity: LiveData<Unit>
        get() = _shouldFinishActivity


    fun getShopItem(shopItemId: Int) {
        val shopItem = getShopItemUseCase.getShopItem(shopItemId)
        _shopItemGetter.value = shopItem
    }

    fun addShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            val shopItem = ShopItem(name, count, true)
            addShopItemUseCase.addShopItem(shopItem)
            finishActivity()
        }
    }

    fun editShopItem(inputName: String?, inputCount: String?) {
        val name = parseName(inputName)
        val count = parseCount(inputCount)
        val fieldsValid = validateInput(name, count)
        if (fieldsValid) {
            _shopItemGetter.value?.let {
                val item = it.copy(name = name, count = count)
                editShopItemUseCase.editShopItem(item)
                finishActivity()
            }
        }
    }

    private fun finishActivity() {
        _shouldFinishActivity.value = Unit
    }

    private fun parseName(inputName: String?): String {
        return inputName?.trim() ?: ""
    }

    private fun parseCount(inputCount: String?): Int {

        return try {
            inputCount?.toInt() ?: 0
        } catch (e: Exception) {
            0
        }
    }

    private fun validateInput(name: String, count: Int): Boolean {

        var result = true
        if (name.isBlank()) {
            _errorInputName.value = true
            result = false
        }

        if (count <= 0) {
            _errorInputCount.value = true
            result = false
        }
        return result
    }

    fun resetErrorInputName() {
        _errorInputName.value = false
    }

    fun resetErrorInputCount() {
        _errorInputCount.value = false
    }
}