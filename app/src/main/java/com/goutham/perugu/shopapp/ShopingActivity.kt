package com.goutham.perugu.shopapp

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.blankj.utilcode.util.ResourceUtils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.goutham.perugu.shopapp.db.ProductsData
import com.goutham.perugu.shopapp.viewmodel.ShoppingViewModel
import com.goutham.perugu.shopapp.viewmodel.ViewModelUtil

class ShopingActivity : FragmentActivity() {

    lateinit var shoppingViewModel: ShoppingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val toolbar = findViewById<Toolbar>(R.id.toolBar)!!
        toolbar.inflateMenu(R.menu.shop_app_menu)

        val findNavController = findNavController(R.id.navHostFragment)
        NavigationUI.setupWithNavController(toolbar, findNavController)
        findNavController.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener {
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {
                toolbar.menu.findItem(R.id.menu_cart)?.isVisible = destination.id != R.id.cartDisplayFragment
            }

        })

        shoppingViewModel = ViewModelUtil.getShopViewModel(this, object : ITalkToViewFromViewModelUtil{
            override fun onDataBaseCreated() {

                val jsonString = ResourceUtils.readAssets2String("input_data.json")

                val productsData = Gson().fromJson<ProductsData>(jsonString, ProductsData::class.java)

                shoppingViewModel.updateDbData(productsData.productsList)

            }

        })

        shoppingViewModel.getCartProducts().observe(this, Observer {
            var cartCount = 0

            it.forEach { product ->
                cartCount += product.cart
            }

            val menuItem = toolbar.menu.findItem(R.id.menu_cart)
            val textView = menuItem.actionView.findViewById<TextView>(R.id.cart_count)
            textView.text = cartCount.toString()
        })

        toolbar.menu.findItem(R.id.menu_cart).actionView.setOnClickListener {
            shoppingViewModel.actionCartMenuItemClicked()
        }

    }
}

interface ITalkToViewFromViewModelUtil{
    fun onDataBaseCreated()
}
