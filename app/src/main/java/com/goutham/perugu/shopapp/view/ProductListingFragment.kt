package com.goutham.perugu.shopapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goutham.perugu.shopapp.*
import com.goutham.perugu.shopapp.adapter.ProductListingAdapter
import com.goutham.perugu.shopapp.viewmodel.Cart
import com.goutham.perugu.shopapp.viewmodel.Details
import com.goutham.perugu.shopapp.viewmodel.ShoppingViewModel
import com.goutham.perugu.shopapp.viewmodel.ViewModelUtil
import io.reactivex.disposables.Disposable

open class ProductListingFragment: Fragment(), ITalkToViewFromAdapter {

    lateinit var shoppingViewModel: ShoppingViewModel

    lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_product_listing_layout, container, false)
    }

    override fun onStart() {
        super.onStart()

        shoppingViewModel = ViewModelUtil.getShopViewModel(activity!!)

        val listingRecyclerView = view?.findViewById<RecyclerView>(R.id.listing_recycler_view)

        shoppingViewModel.subscribeToDataBase().observe(viewLifecycleOwner, Observer {
            val productListingAdapter = listingRecyclerView?.adapter as ProductListingAdapter
            productListingAdapter.updateList(it)
        })

        disposable = shoppingViewModel.subscribeToShoppingState()
            .subscribe {
            when (it) {

                is Details -> {
                    if (findNavController().currentDestination?.id == R.id.productListingFragment){
                        val actionProductListingFragmentToProductDetailsFragment =
                            ProductListingFragmentDirections.actionProductListingFragmentToProductDetailsFragment(
                                it.itemId
                            )
                        findNavController().navigate(actionProductListingFragmentToProductDetailsFragment)
                    }
                }

                Cart -> {
                    if (findNavController().currentDestination?.id == R.id.productListingFragment){
                        findNavController().navigate(ProductListingFragmentDirections.actionProductListingFragmentToCartDisplayFragment())
                    }
                }
            }
        }

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val listingRecyclerView = view.findViewById<RecyclerView>(R.id.listing_recycler_view)

        val productListingAdapter = ProductListingAdapter(listOf(), this)
        listingRecyclerView.adapter = productListingAdapter

        val gridLayoutManager = GridLayoutManager(view.context, 2)
        /*gridLayoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                if (productListingAdapter.getItemViewType(position) == 1) {
                    return 1
                } else {
                    return 2
                }
            }
        }*/
        listingRecyclerView.layoutManager = gridLayoutManager
    }

    override fun onThumbnailClicked(itemId: String) {
        shoppingViewModel.actionListingThumbnailClicked(itemId)
    }

    override fun onStop() {
        super.onStop()
        if (!disposable.isDisposed){
            disposable.dispose()
        }
    }

}

interface ITalkToViewFromAdapter{
    fun onThumbnailClicked(itemId: String)
}