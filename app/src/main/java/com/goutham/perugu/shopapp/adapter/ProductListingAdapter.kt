package com.goutham.perugu.shopapp.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.ImageUtils
import com.goutham.perugu.shopapp.R
import com.goutham.perugu.shopapp.view.ITalkToViewFromAdapter
import com.goutham.perugu.shopapp.db.Product

class ProductListingAdapter(

    private var productsList: List<Product>,

    private val iTalkToViewFromAdapter: ITalkToViewFromAdapter

): RecyclerView.Adapter<ProductListingAdapter.ProductViewHolder>() {

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val context = holder.itemView.context

        val product = productsList[position]

        holder.itemView.setOnClickListener {
            iTalkToViewFromAdapter.onThumbnailClicked(product.itemId)
        }

        val bitmap = ImageUtils.getBitmap(context.assets.open(product.thumbnail))
        val productThumbnailImageView = holder.itemView.findViewById<ImageView>(R.id.product_thumbnail)
        productThumbnailImageView.setImageBitmap(bitmap)

        val productName = holder.itemView.findViewById<TextView>(R.id.product_name)
        productName.text = product.name

        val categoryName = holder.itemView.findViewById<TextView>(R.id.category_name)
        categoryName.text = product.categoryId.name

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_holder_layout, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int {
        return productsList.size
    }

    fun updateList(it: List<Product>?) {
        productsList = it!!
        notifyDataSetChanged()
    }

    inner class ProductViewHolder(view: View): RecyclerView.ViewHolder(view)

}
