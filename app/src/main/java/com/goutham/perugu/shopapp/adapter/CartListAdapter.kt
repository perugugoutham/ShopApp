package com.goutham.perugu.shopapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ImageUtils
import com.goutham.perugu.shopapp.R
import com.goutham.perugu.shopapp.view.ITalkToCartFragment
import com.goutham.perugu.shopapp.db.Product

class CartListAdapter(private var products: List<Product>, val iTalkToCartFragment: ITalkToCartFragment): RecyclerView.Adapter<CartListAdapter.CartListHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartListHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cart_item_layout, parent, false)
        return CartListHolder(view)
    }

    override fun getItemCount(): Int {
        return products.size
    }

    override fun onBindViewHolder(holder: CartListHolder, position: Int) {
        val context = holder.itemView.context

        val product = products[position]

        val bitmap = ImageUtils.getBitmap(context.assets.open(product.thumbnail))
        val imageView = holder.itemView.findViewById<ImageView>(R.id.thumbnail_icon)
        imageView.setImageBitmap(bitmap)

        val item_name = holder.itemView.findViewById<TextView>(R.id.item_name)
        item_name.text = product.name

        val item_price = holder.itemView.findViewById<TextView>(R.id.item_cost)
        item_price.text = context.getString(R.string.rupees, (product.cart * product.cost).toString())

        val item_count = holder.itemView.findViewById<TextView>(R.id.item_count)
        item_count.text = "${ product.cart }"

        val stock_view = holder.itemView.findViewById<TextView>(R.id.stock_count)
        stock_view.text = context.getString(R.string.available, product.stock.toString())

        val addButton = holder.itemView.findViewById<ImageButton>(R.id.add)
        addButton.setOnClickListener {
            iTalkToCartFragment.actionIncrement(product.itemId)
        }

        val remove = holder.itemView.findViewById<ImageButton>(R.id.remove)
        remove.setOnClickListener {
            iTalkToCartFragment.actionDecrement(product.itemId)
        }

        holder.itemView.setOnClickListener {
            iTalkToCartFragment.onItemClicked(product.itemId)
        }
    }

    fun updateList(products: List<Product>?) {
        this.products = products!!
        notifyDataSetChanged()
    }

    inner class CartListHolder(view: View): RecyclerView.ViewHolder(view)
}