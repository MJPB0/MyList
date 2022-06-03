package com.example.shoppinglist.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

// Lista zakupowa.
@Entity(tableName = "shopping_lists")
class ShoppingList(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "shopping_list_id")
    var shoppingListId: Long = 0L,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "date")
    var date: Date
)