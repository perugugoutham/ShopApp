package com.goutham.perugu.shopapp.db

import androidx.room.TypeConverter

class ProductDataTypeConverter {

    @TypeConverter
    fun stringToCategory(id: String): Category? {
        return Category.values().find { it.id == id }
    }

    @TypeConverter
    fun categoryToString(category: Category): String {
        return category.id
    }
}