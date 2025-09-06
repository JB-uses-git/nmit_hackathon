package com.ecofinds.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.ecofinds.R
import com.ecofinds.model.Product
import com.bumptech.glide.Glide
import java.text.NumberFormat
import java.util.Locale

class CartAdapter(
    private val onItemClick: (Product) -> Unit,
    private val onRemoveClick: (Product) -> Unit
) : RecyclerView.Adapter<CartAdapter.CartViewHolder>() {

    private var cartItems = mutableListOf<Product>()

    fun updateCartItems(newItems: List<Product>) {
        cartItems.clear()
        cartItems.addAll(newItems)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cart, parent, false)
        return CartViewHolder(view)
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        holder.bind(cartItems[position])
    }

    override fun getItemCount(): Int = cartItems.size

    inner class CartViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val ivProductImage: ImageView = itemView.findViewById(R.id.ivProductImage)
        private val tvProductTitle: TextView = itemView.findViewById(R.id.tvProductTitle)
        private val tvProductPrice: TextView = itemView.findViewById(R.id.tvProductPrice)
        private val tvProductCategory: TextView = itemView.findViewById(R.id.tvProductCategory)
        private val btnRemove: Button = itemView.findViewById(R.id.btnRemove)

        fun bind(product: Product) {
            tvProductTitle.text = product.title
            tvProductCategory.text = product.category
            
            val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
            tvProductPrice.text = formatter.format(product.price)

            // Load product image
            Glide.with(itemView.context)
                .load(product.imageUrl.ifEmpty { com.ecofinds.utils.Constants.IMAGE_PLACEHOLDER_URL })
                .placeholder(R.drawable.ic_product_placeholder)
                .error(R.drawable.ic_product_placeholder)
                .into(ivProductImage)

            itemView.setOnClickListener {
                onItemClick(product)
            }

            btnRemove.setOnClickListener {
                onRemoveClick(product)
            }
        }
    }
}
