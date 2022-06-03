package com.example.shoppinglist.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.shoppinglist.converters.DateConverter
import com.example.shoppinglist.data.database.dao.ListProductDao
import com.example.shoppinglist.data.database.dao.ShoppingListDao
import com.example.shoppinglist.data.database.entities.ListProduct
import com.example.shoppinglist.data.database.entities.ShoppingList

@Database(
    entities = [
        ListProduct::class,
        ShoppingList::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    DateConverter::class
)
abstract class DatabaseContext : RoomDatabase() {

    abstract val shoppingListDao: ShoppingListDao
    abstract val listProductDao: ListProductDao

    companion object {
        @Volatile
        private var INSTANCE: DatabaseContext? = null

        fun getInstance(context: Context): DatabaseContext {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        DatabaseContext::class.java,
                        "database_context"
                    )
                        .fallbackToDestructiveMigration()
                        .build()

                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}