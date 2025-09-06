package com.ecofinds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecofinds.R
import com.ecofinds.model.Product
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class ProductAdapter(
    private val onItemClick: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    private var products = mutableListOf<Product>()

    fun updateProducts(newProducts: List<Product>) {
        products.clear()
        products.addAll(newProducts)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        holder.bind(products[position])
    }

    override fun getItemCount(): Int = products.size

    inner class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        private val tvProductTitle: TextView = itemView.findViewById(R.id.tvProductTitle)
        private val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val tvProductCategory: TextView = itemView.findViewById(R.id.tvProductCategory)

        fun bind(product: Product) {
            tvProductTitle.text = product.title
            tvProductCategory.text = product.category
            
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            tvProductPrice.text = formatter.format(product.price)

            // Load product image (placeholder for now)
            Glide.with(itemView.context)
                .load(product.imageUrl.ifEmpty { com.ecofinds.utils.Constants.IMAGE_PLACEHOLDER_URL })
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(ivProductImage)

            itemView.setOnClickListener {
                onItemClick(product)
            }
        }
    }
}
