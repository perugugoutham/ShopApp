package com.goutham.perugu.shopapp.db

import androidx.lifecycle.LiveData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class DataBaseApiImpl(private val productsDataDb: ProductsDataDb, override val dataBaseScope: CoroutineScope) : DataBaseApi {

    override fun getDbData(): LiveData<List<Product>> {
        return productsDataDb.productsDataDao().products
    }

    override fun insertData(productsList: List<Product>) {
        dataBaseScope.launch {
            productsDataDb.productsDataDao().insertData(productsList)
        }
    }

    override fun getDbDataOf(itemId: String): LiveData<Product> {
        return productsDataDb.productsDataDao().getDataOf(itemId)
    }

    override fun getCartProducts(): LiveData<List<Product>> {
        return  productsDataDb.productsDataDao().getCartProducts()
    }

    override fun addItemToCart(itemId: String) {
        dataBaseScope.launch {
            productsDataDb.productsDataDao().addItemToCart(itemId)
        }
    }

    override fun removeItemFromCart(itemId: String) {
        dataBaseScope.launch {
            productsDataDb.productsDataDao().removeItemFromCart(itemId)
        }
    }

}