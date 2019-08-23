package com.goutham.perugu.shopapp

import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.room.Room
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.runner.AndroidJUnit4
import com.blankj.utilcode.util.ResourceUtils
import com.google.gson.Gson
import com.goutham.perugu.shopapp.db.*
import com.goutham.perugu.shopapp.viewmodel.*
import io.reactivex.observers.TestObserver
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class ShoppingAppTests {

    private lateinit var shoppingViewModel: ShoppingViewModel

    private lateinit var dataBase: ProductsDataDb

    private var stateObserver = TestObserver.create<PageType>()

    @Before
    fun init() {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        dataBase = Room.inMemoryDatabaseBuilder(context, ProductsDataDb::class.java).allowMainThreadQueries().build()
        val dataBaseApi: DataBaseApi = DataBaseApiImpl(dataBase, CoroutineScope(Dispatchers.Main))
        shoppingViewModel = ShoppingViewModel(dataBaseApi)
        shoppingViewModel.subscribeToShoppingState().subscribe(stateObserver)
        val readFile2String = ResourceUtils.readAssets2String("input_data.json")
        val products = Gson().fromJson<ProductsData>(readFile2String, ProductsData::class.java)
        shoppingViewModel.updateDbData(products.productsList)
    }

    @Test
    fun checkIfValuesAreInserted() {
        val valueList = getValue(shoppingViewModel.subscribeToDataBase())
        valueList.size shouldBe 6
    }

    @Test
    fun checkIfItemsAreAddedToCart(){ //Adding an item twice to the cart

        shoppingViewModel.actionAddItemToCart("2")
        shoppingViewModel.actionAddItemToCart("2")

        val valueList = getValue(shoppingViewModel.getCartProducts())
        valueList.size shouldBe 1
        valueList[0].cart shouldBe 2
        valueList[0].stock shouldBe 15
    }

    @Test
    fun addingMultipleProductsToCart(){

        shoppingViewModel.actionAddItemToCart("2")
        shoppingViewModel.actionAddItemToCart("2")
        shoppingViewModel.actionAddItemToCart("3")

        val valueList = getValue(shoppingViewModel.getCartProducts())
        valueList.size shouldBe 2

        var cart = 0

        valueList.forEach {
            cart += it.cart
        }

        cart shouldBe 3

        valueList[0].stock shouldBe 15
        valueList[1].stock shouldBe 10
    }

    @Test
    fun removingItemFromCart(){
        //Adding them first
        shoppingViewModel.actionAddItemToCart("2")
        shoppingViewModel.actionAddItemToCart("2")
        shoppingViewModel.actionAddItemToCart("3")

        //Removing them now
        shoppingViewModel.actionRemoveItemFromCart("2")

        val valueList = getValue(shoppingViewModel.getCartProducts())
        valueList.size shouldBe 2

        var cart = 0

        valueList.forEach {
            cart += it.cart
        }

        cart shouldBe 2

        valueList[0].stock shouldBe 16
        valueList[1].stock shouldBe 10
    }

    @Test
    fun readAnItemData(){
        val product = getValue(shoppingViewModel.getProductDetails("1"))
        product.name shouldBe "Microwave oven"
        product.cart shouldBe 0
        product.stock shouldBe 14
        product.categoryId shouldBe Category.ELECTRICAL
        product.cost shouldBe 150.0f

    }

    @Throws(InterruptedException::class)
    fun <T> getValue(liveData: LiveData<T>): T {
        val data = arrayOfNulls<Any>(1)
        val latch = CountDownLatch(1)
        val observer = object : Observer<T> {
            override fun onChanged(t: T?) {
                data[0] = t
                latch.countDown()
                liveData.removeObserver(this)
            }

        }
        android.os.Handler(Looper.getMainLooper()).post {
            liveData.observeForever(observer)
        }

        latch.await(2, TimeUnit.SECONDS)

        return data[0] as T
    }

    @Test
    fun openDetailsPage(){
        shoppingViewModel.actionListingThumbnailClicked("2")
        (stateObserver.values().last() is Details) shouldBe true
    }

    @Test
    fun openCartPage(){
        shoppingViewModel.actionCartMenuItemClicked()
        (stateObserver.values().last() is Cart) shouldBe true
    }

    @Test
    fun openDetailsPageFromCart(){
        shoppingViewModel.actionCartListItemClicked("2")
        (stateObserver.values().last() is Details) shouldBe true
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        dataBase.close()
    }


}
