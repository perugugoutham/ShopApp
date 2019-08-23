package com.goutham.perugu.shopapp.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface ProductsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertData(productsList: List<Product>)

    @get:Query("SELECT * FROM PRODUCTS" )
    val products: LiveData<List<Product>>

    @Query("SELECT * FROM PRODUCTS WHERE itemId=:itemId")
    fun getDataOf(itemId: String):LiveData<Product>

    @Query("SELECT * FROM PRODUCTS WHERE cart > 0")
    fun getCartProducts(): LiveData<List<Product>>

    @Query("UPDATE Products SET  cart = (cart + 1), stock = (stock - 1) WHERE (itemId = :itemId AND stock > 0)")
    fun addItemToCart(itemId: String)

    @Query("UPDATE Products SET  cart = (cart - 1), stock = (stock + 1) WHERE (itemId = :itemId AND cart > 0)")
    fun removeItemFromCart(itemId: String)

}