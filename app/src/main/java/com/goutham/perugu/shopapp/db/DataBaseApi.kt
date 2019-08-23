package com.goutham.perugu.shopapp.db

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope

interface DataBaseApi {

    fun insertData(productsList: List<Product>)

    fun getDbData(): LiveData<List<Product>>

    fun getDbDataOf(itemId: String): LiveData<Product>

    fun getCartProducts(): LiveData<List<Product>>

    fun addItemToCart(itemId: String)

    fun removeItemFromCart(itemId: String)

    val dataBaseScope: CoroutineScope

}