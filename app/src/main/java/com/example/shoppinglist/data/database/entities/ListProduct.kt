package com.example.shoppinglist.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

// Produkt z listy zakupowej.
@Entity(tableName = "list_products")
data class ListProduct(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "product_id")
    var productId: Long = 0L,

    @ColumnInfo(name = "shopping_list_id")
    var shoppingListId: Long,

    @ColumnInfo(name = "product")
    var product: String,

    @ColumnInfo(name = "amount")
    var amount: Int,

    @ColumnInfo(name = "unit")
    var unit: String,

    @ColumnInfo(name = "cost")
    var cost: Double,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "was_bought")
    var wasBought: Boolean
)