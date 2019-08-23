package com.goutham.perugu.shopapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.goutham.perugu.shopapp.db.DataBaseApi
import com.goutham.perugu.shopapp.db.Product
import com.jakewharton.rxrelay2.PublishRelay

class ShoppingViewModel(private val dataBaseApi: DataBaseApi) : ViewModel() {


    private val pageState: PublishRelay<PageType> = PublishRelay.create<PageType>()

    fun updateDbData(productsData: List<Product>) {
        dataBaseApi.insertData(productsData)
    }

    fun actionListingThumbnailClicked(itemId: String) {
        actionNavigateTo(Details(itemId))
    }

    fun subscribeToDataBase(): LiveData<List<Product>> {
        return dataBaseApi.getDbData()
    }

    fun subscribeToShoppingState(): PublishRelay<PageType> {
        return pageState
    }

    fun getProductDetails(itemId: String): LiveData<Product> {
        return dataBaseApi.getDbDataOf(itemId)
    }

    fun getCartProducts(): LiveData<List<Product>> {
        return dataBaseApi.getCartProducts()
    }

    fun actionAddItemToCart(itemId: String) {
        dataBaseApi.addItemToCart(itemId)
    }

    fun actionRemoveItemFromCart(itemId: String) {
        dataBaseApi.removeItemFromCart(itemId)
    }

    fun actionCartMenuItemClicked() {
        actionNavigateTo(Cart)
    }

    private fun actionNavigateTo(pageType: PageType){
        pageState.accept(pageType)
    }

    fun actionCartListItemClicked(itemId: String) {
        actionNavigateTo(Details(itemId))
    }
}