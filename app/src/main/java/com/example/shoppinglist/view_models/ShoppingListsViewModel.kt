package com.example.shoppinglist.view_models

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.shoppinglist.data.database.DatabaseContext
import com.example.shoppinglist.data.database.dao.ShoppingListDao
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.data.database.entities.ShoppingList
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class ShoppingListsViewModel (application: Application): AndroidViewModel(application) {
    private val shoppingListDAO: ShoppingListDao = DatabaseContext.getInstance(application).shoppingListDao

    fun getAllLists(): LiveData<List<ShoppingList>> = runBlocking {
        shoppingListDAO.getShoppingLists()
    }

    fun getListById(listId: Long): ShoppingList = runBlocking {
        shoppingListDAO.getShoppingList(listId)
    }

    fun deleteShoppingList(list: ShoppingList) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingListDAO.deleteShoppingList(list)
        }
    }

    fun updateShoppingList(list: ShoppingList) {
        viewModelScope.launch(Dispatchers.IO) {
            shoppingListDAO.updateShoppingList(list)
        }
    }

    fun getShoppingListInPeriodOfTime(startDate: Date, finishDate: Date) : LiveData<List<ShoppingList>> = runBlocking {
        shoppingListDAO.getShoppingListInPeriodOfTime(startDate, finishDate)
    }

    fun addShoppingList(name: String, date: Date) {
        viewModelScope.launch(Dispatchers.IO) {
            val list = ShoppingList(0L, name, date)
            shoppingListDAO.insertShoppingList(list)
        }
    }

    fun getProductsForShoppingList(shoppingListId: Long): LiveData<List<ListProduct>> = runBlocking {
        shoppingListDAO.getProductsForShoppingList(shoppingListId)
    }

    fun getShoppingListTotalSumPrice(shoppingListId: Long): Double = runBlocking {
        if (shoppingListDAO.getShoppingListTotalSumPrice(shoppingListId) == null) 0.0 else shoppingListDAO.getShoppingListTotalSumPrice(shoppingListId)
    }

    fun getShoppingListPayedSumPrice(shoppingListId: Long): Double = runBlocking {
        if (shoppingListDAO.getShoppingListPayedSumPrice(shoppingListId) == null) 0.0 else shoppingListDAO.getShoppingListPayedSumPrice(shoppingListId)
    }
}