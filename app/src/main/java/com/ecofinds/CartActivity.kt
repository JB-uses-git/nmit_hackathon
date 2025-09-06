package com.ecofinds

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecofinds.adapter.CartAdapter
import com.ecofinds.database.EcoFindsDatabase
import com.ecofinds.model.Product
import com.ecofinds.model.Purchase
import com.ecofinds.utils.SessionManager
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class CartActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var database: EcoFindsDatabase
    private lateinit var cartAdapter: CartAdapter
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmptyCart: TextView
    private lateinit var tvTotalPrice: TextView
    private lateinit var btnCheckout: Button
    
    private var cartItems = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        sessionManager = SessionManager(this)
        database = EcoFindsDatabase.getDatabase(this)

        title = "My Cart"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        setupRecyclerView()
        loadCartItems()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewCart)
        tvEmptyCart = findViewById(R.id.tvEmptyCart)
        tvTotalPrice = findViewById(R.id.tvTotalPrice)
        btnCheckout = findViewById(R.id.btnCheckout)

        btnCheckout.setOnClickListener {
            checkoutCart()
        }
    }

    private fun setupRecyclerView() {
        cartAdapter = CartAdapter(
            onItemClick = { product ->
                val intent = Intent(this, ProductDetailActivity::class.java)
                intent.putExtra("product_id", product.id)
                startActivity(intent)
            },
            onRemoveClick = { product ->
                removeFromCart(product)
            }
        )
        
        recyclerView.apply {
            adapter = cartAdapter
            layoutManager = LinearLayoutManager(this@CartActivity)
        }
    }

    private fun loadCartItems() {
        val currentUserId = sessionManager.getUserId()
        
        lifecycleScope.launch {
            try {
                cartItems.clear()
                cartItems.addAll(database.cartDao().getCartItemsForUser(currentUserId))
                
                if (cartItems.isEmpty()) {
                    showEmptyCart()
                } else {
                    showCartItems()
                }
                
                cartAdapter.updateCartItems(cartItems)
                updateTotalPrice()
            } catch (e: Exception) {
                Toast.makeText(this@CartActivity, "Error loading cart: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun showEmptyCart() {
        recyclerView.visibility = android.view.View.GONE
        tvEmptyCart.visibility = android.view.View.VISIBLE
        tvTotalPrice.visibility = android.view.View.GONE
        btnCheckout.visibility = android.view.View.GONE
    }

    private fun showCartItems() {
        recyclerView.visibility = android.view.View.VISIBLE
        tvEmptyCart.visibility = android.view.View.GONE
        tvTotalPrice.visibility = android.view.View.VISIBLE
        btnCheckout.visibility = android.view.View.VISIBLE
    }

    private fun updateTotalPrice() {
        val total = cartItems.sumOf { it.price }
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        tvTotalPrice.text = "Total: ${formatter.format(total)}"
    }

    private fun removeFromCart(product: Product) {
        val currentUserId = sessionManager.getUserId()
        
        lifecycleScope.launch {
            try {
                val cartItem = database.cartDao().getCartItem(currentUserId, product.id)
                cartItem?.let {
                    database.cartDao().removeFromCart(it)
                    Toast.makeText(this@CartActivity, "Removed from cart", Toast.LENGTH_SHORT).show()
                    loadCartItems()
                }
            } catch (e: Exception) {
                Toast.makeText(this@CartActivity, "Error removing item: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun checkoutCart() {
        val currentUserId = sessionManager.getUserId()
        
        if (cartItems.isEmpty()) {
            Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show()
            return
        }

        btnCheckout.isEnabled = false
        btnCheckout.text = "Processing..."

        lifecycleScope.launch {
            try {
                // Create purchase records for all items
                for (product in cartItems) {
                    val purchase = Purchase(
                        buyerId = currentUserId,
                        productId = product.id,
                        purchasePrice = product.price
                    )
                    database.purchaseDao().insertPurchase(purchase)
                    
                    // Mark product as unavailable
                    val updatedProduct = product.copy(isAvailable = false)
                    database.productDao().updateProduct(updatedProduct)
                }
                
                // Clear cart
                database.cartDao().clearCart(currentUserId)
                
                Toast.makeText(this@CartActivity, "Purchase successful!", Toast.LENGTH_LONG).show()
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@CartActivity, "Purchase failed: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } finally {
                btnCheckout.isEnabled = true
                btnCheckout.text = "Checkout"
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadCartItems()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
