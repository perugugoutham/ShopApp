package com.goutham.perugu.shopapp.db

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [Product::class], version = 1)
open abstract class ProductsDataDb: RoomDatabase() {
    abstract fun productsDataDao(): ProductsDao
}