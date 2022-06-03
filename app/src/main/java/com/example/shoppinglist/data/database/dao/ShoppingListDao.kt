package com.example.shoppinglist.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.data.database.entities.ShoppingList
import java.util.*

@Dao
interface ShoppingListDao {
    // Służy do pobierania listy zakupowej o konkretnym id.
    @Query("SELECT * FROM shopping_lists WHERE shopping_list_id = :shoppingListId")
    suspend fun getShoppingList(shoppingListId: Long) : ShoppingList

    @Query("SELECT SUM(cost*amount) FROM list_products WHERE shopping_list_id = :shoppingListId")
    suspend fun getShoppingListTotalSumPrice(shoppingListId: Long) : Double

    @Query("SELECT SUM(cost*amount) FROM list_products l INNER JOIN shopping_lists s ON l.shopping_list_id = s.shopping_list_id WHERE s.shopping_list_id = :shoppingListId AND l.was_bought = 1")
    suspend fun getShoppingListPayedSumPrice(shoppingListId: Long) : Double

    // Służy do pobrania produktów w określonym stanie dla listy zakupowej o podanym ID.
    @Query("SELECT * FROM list_products WHERE was_bought = :wasBought AND shopping_list_id = :shoppingListId")
    fun getProductsForShoppingListInState(shoppingListId: Long, wasBought: Boolean): LiveData<List<ListProduct>>

    // Służy do pobrania wszystkich produktów z listy zakupowej o konkretnym id.
    @Query("SELECT * FROM list_products WHERE shopping_list_id = :shoppingListId")
    fun getProductsForShoppingList(shoppingListId: Long): LiveData<List<ListProduct>>

    // Służy do zaktualizowania danych listy zakupowej.
    @Update
    suspend fun updateShoppingList(shoppingList: ShoppingList)

    // Służy do dodania nowej listy zakupowej.
    @Insert
    suspend fun insertShoppingList(shoppingList: ShoppingList)

    // Służy do pobrania wszystkich list zakupowych w danym okresie czasu.
    @Query("SELECT * FROM shopping_lists WHERE date >= :startDate AND date <= :finishDate")
    fun getShoppingListInPeriodOfTime(startDate: Date, finishDate: Date) : LiveData<List<ShoppingList>>

    // Służy do pobrania wszystkich list zakupowych.
    @Query("SELECT * FROM shopping_lists")
    fun getShoppingLists() : LiveData<List<ShoppingList>>

    // Służy do usunięcia istniejącej listy
    @Delete
    fun deleteShoppingList(list: ShoppingList)
}