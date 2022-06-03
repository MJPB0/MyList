package com.example.shoppinglist.data.database.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.shoppinglist.data.database.entities.ListProduct

@Dao
interface ListProductDao {
    // Służy do pobrania pojedynczego produktu za pomocą jego ID.
    @Query("SELECT * FROM list_products WHERE product_id = :productId")
    suspend fun getProduct(productId: Long) : ListProduct

    // Służy do dodania produktu do listy zakupów.
    @Insert
    suspend fun insertProduct(listProduct: ListProduct)

    // Służy do usunięcia produktu z listy zakupów
    @Query("DELETE FROM list_products WHERE product_id = :listProductId")
    suspend fun deleteProduct(listProductId: Long)

    // Służy do aktualizowania danych produktu.
    @Update
    suspend fun updateProduct(listProduct: ListProduct)

    // Służy do pobrania wszystkich roduktów wszystkich list zakupowych.
    @Query("SELECT * FROM list_products")
    fun getAllProducts() : LiveData<List<ListProduct>>
}