package com.example.shoppinglist.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.api.dto.ProductDto
import com.example.shoppinglist.data.database.DatabaseContext
import com.example.shoppinglist.data.database.dao.ListProductDao
import com.example.shoppinglist.data.database.dao.ShoppingListDao
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.data.database.entities.ShoppingList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ProductsViewModel (application: Application): AndroidViewModel(application) {
    private val listProductDao: ListProductDao = DatabaseContext.getInstance(application).listProductDao

    fun getProductById(productId: Long)= runBlocking {
        listProductDao.getProduct(productId)
    }

    fun insertListProduct(product: ListProduct){
        viewModelScope.launch(Dispatchers.IO) {
            listProductDao.insertProduct(product)
        }
    }

    fun deleteListProduct(productId: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            listProductDao.deleteProduct(productId)
        }
    }

    fun updateListProduct(product: ListProduct) {
        viewModelScope.launch(Dispatchers.IO) {
            listProductDao.updateProduct(product)
        }
    }

    fun reduceProductAmount(productId: Long) {
        val product: ListProduct = runBlocking {
            listProductDao.getProduct(productId)
        }
        if (product.amount < 1) return

        product.amount -= 1
        this.updateListProduct(product)
    }

    fun increaseProductAmount(productId: Long) {
        val product: ListProduct = runBlocking {
            listProductDao.getProduct(productId)
        }
        product.amount += 1
        this.updateListProduct(product)
    }
}