package com.example.shoppinglist.view_models

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.shoppinglist.data.api.dto.ProductDto
import com.example.shoppinglist.data.database.entities.ListProduct

class Communicator: ViewModel() {
    val ShoppingListId = MutableLiveData<Long>()
    val Product = MutableLiveData<ProductDto>()
    val AddedProductId = MutableLiveData<Long>()

    fun setShoppingListId(value: Long){
        ShoppingListId.value = value
    }

    fun setProduct(value: ProductDto){
        Product.value = value
    }

    fun setAddedProductId(value: Long){
        AddedProductId.value = value
    }
}