package com.goutham.perugu.shopapp.db

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.google.gson.annotations.SerializedName


data class ProductsData(
    @SerializedName("Products")
    val productsList: List<Product> = mutableListOf()
)

@Entity(tableName = "Products")
@TypeConverters(ProductDataTypeConverter::class)
data class Product(

    val cart: Int = 0,

    @SerializedName("categoryId")
    val categoryId: Category = Category.NONE,

    @PrimaryKey
    @SerializedName("itemId")
    val itemId: String = "",

    val name: String = "",

    val stock: Int = 0,

    val thumbnail: String = "",

    val cost: Float = 0.0f
)

enum class Category(val id: String){

    @SerializedName("-1")
    NONE ("-1"),

    @SerializedName("1")
    ELECTRICAL("1"),

    @SerializedName("2")
    FURNITURE("2")
}
