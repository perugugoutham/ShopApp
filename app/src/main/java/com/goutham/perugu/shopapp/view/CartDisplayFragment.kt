package com.goutham.perugu.shopapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.goutham.perugu.shopapp.R
import com.goutham.perugu.shopapp.adapter.CartListAdapter
import com.goutham.perugu.shopapp.viewmodel.Details
import com.goutham.perugu.shopapp.viewmodel.ShoppingViewModel
import com.goutham.perugu.shopapp.viewmodel.ViewModelUtil
import io.reactivex.disposables.Disposable

class CartDisplayFragment: Fragment(), ITalkToCartFragment {

    lateinit var shoppingViewModel: ShoppingViewModel

    lateinit var disposable: Disposable

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_cart_display_layout, container, false)
    }

    override fun onStart() {
        super.onStart()

        shoppingViewModel = ViewModelUtil.getShopViewModel(activity!!)

        val cartListingRecyclerView = view!!.findViewById<RecyclerView>(R.id.cart_recycler_view)

        shoppingViewModel.getCartProducts().observe(viewLifecycleOwner, Observer {
            val cartListAdapter = cartListingRecyclerView.adapter as CartListAdapter
            cartListAdapter.updateList(it)
            var totalCost = 0f
            it.forEach {
                totalCost += (it.cart * it.cost)
            }
            view?.findViewById<TextView>(R.id.total_cost)?.text = view?.context?.getString(R.string.total, totalCost.toString())
        })

        disposable = shoppingViewModel.subscribeToShoppingState()
            .subscribe {
                when(it){
                    is Details -> {
                        if (findNavController().currentDestination?.id == R.id.cartDisplayFragment){
                            findNavController().navigate(CartDisplayFragmentDirections.actionCartDisplayFragmentToProductDetailsFragment(it.itemId))
                        }
                    }
                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val cartListingRecyclerView = view.findViewById<RecyclerView>(R.id.cart_recycler_view)
        val linearLayoutManager = LinearLayoutManager(view.context)
        linearLayoutManager.orientation = RecyclerView.VERTICAL
        cartListingRecyclerView.layoutManager = linearLayoutManager
        cartListingRecyclerView.adapter = CartListAdapter(listOf(), this)
    }

    override fun actionDecrement(itemId: String) {
        shoppingViewModel.actionAddItemToCart(itemId)
    }

    override fun actionIncrement(itemId: String) {
        shoppingViewModel.actionRemoveItemFromCart(itemId)
    }

    override fun onItemClicked(itemId: String) {
        shoppingViewModel.actionCartListItemClicked(itemId)
    }

    override fun onStop() {
        super.onStop()
        if (!disposable.isDisposed){
            disposable.dispose()
        }
    }
}

interface ITalkToCartFragment{
    fun onItemClicked(itemId: String)
    fun actionIncrement(itemId: String)
    fun actionDecrement(itemId: String)
}