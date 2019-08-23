@file:Suppress("UNCHECKED_CAST")

package com.goutham.perugu.shopapp.viewmodel

import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.goutham.perugu.shopapp.ITalkToViewFromViewModelUtil
import com.goutham.perugu.shopapp.db.DataBaseApi
import com.goutham.perugu.shopapp.db.DataBaseApiImpl
import com.goutham.perugu.shopapp.db.ProductsDataDb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

object ViewModelUtil {

    private var shopDb: ProductsDataDb? = null

    fun getShopViewModel(
        activity: FragmentActivity
    ): ShoppingViewModel {
        return ViewModelProviders.of(activity, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (shopDb == null){
                    shopDb = Room.databaseBuilder(activity, ProductsDataDb::class.java, "ShopAppDb").build()
                }

                val dataBaseApi: DataBaseApi =
                    DataBaseApiImpl(shopDb!!, CoroutineScope(Dispatchers.IO))

                return ShoppingViewModel(dataBaseApi) as T
            }

        })[ShoppingViewModel::class.java]
    }

    fun getShopViewModel(
        activity: FragmentActivity,
        iTalkToViewFromViewModelUtil: ITalkToViewFromViewModelUtil
    ): ShoppingViewModel {
        return ViewModelProviders.of(activity, object : ViewModelProvider.Factory{
            override fun <T : ViewModel?> create(modelClass: Class<T>): T {
                if (shopDb == null){
                    shopDb = Room.databaseBuilder(activity, ProductsDataDb::class.java, "ShopAppDb")
                        .addCallback(object: RoomDatabase.Callback(){
                            override fun onCreate(db: SupportSQLiteDatabase) {
                                super.onCreate(db)
                                iTalkToViewFromViewModelUtil.onDataBaseCreated()
                            }
                        }).build()
                }

                val dataBaseApi: DataBaseApi =
                    DataBaseApiImpl(shopDb!!, CoroutineScope(Dispatchers.IO))

                return ShoppingViewModel(dataBaseApi) as T
            }

        })[ShoppingViewModel::class.java]
    }
}