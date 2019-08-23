package com.goutham.perugu.shopapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.blankj.utilcode.util.ImageUtils
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton
import com.goutham.perugu.shopapp.*
import com.goutham.perugu.shopapp.viewmodel.Cart
import com.goutham.perugu.shopapp.viewmodel.ShoppingViewModel
import com.goutham.perugu.shopapp.viewmodel.ViewModelUtil
import io.reactivex.disposables.Disposable

class ProductDetailsFragment: Fragment() {

    private lateinit var shoppingViewModel: ShoppingViewModel

    lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_details_layout, container, false)
    }

    override fun onStart() {
        super.onStart()

        shoppingViewModel = ViewModelUtil.getShopViewModel(activity!!)

        val productDetailsFragmentArgs =
            ProductDetailsFragmentArgs.fromBundle(arguments!!)

        val itemId = productDetailsFragmentArgs.itemId

        shoppingViewModel.getProductDetails(itemId).observe(viewLifecycleOwner, Observer {
            val imageView = view!!.findViewById<ImageView>(R.id.product_image)
            val bitmap = ImageUtils.getBitmap(imageView.context.assets.open(it.thumbnail))
            imageView.setImageBitmap(bitmap)

            val productNameView = view!!.findViewById<TextView>(R.id.product_name)
            productNameView.text = it.name

            val productPrice = view!!.findViewById<TextView>(R.id.product_price)
            productPrice.text = productPrice.context.getString(R.string.rupees, it.cost.toString())

            val stock_count = view!!.findViewById<TextView>(R.id.stock_count)
            stock_count.text = stock_count.context.getString(R.string.available, it.stock.toString())

        })

        disposable = shoppingViewModel.subscribeToShoppingState()
            .subscribe {
            when(it){

                Cart -> {
                    if (findNavController().currentDestination?.id == R.id.productDetailsFragment){
                        findNavController().navigate(ProductDetailsFragmentDirections.actionProductDetailsFragmentToCartDisplayFragment())
                    }
                }
            }
        }

        view!!.findViewById<ExtendedFloatingActionButton>(R.id.add_to_cart_fab).setOnClickListener {
            shoppingViewModel.actionAddItemToCart(itemId)
        }
    }

    override fun onStop() {
        super.onStop()
        if (!disposable.isDisposed){
            disposable.dispose()
        }
    }
}