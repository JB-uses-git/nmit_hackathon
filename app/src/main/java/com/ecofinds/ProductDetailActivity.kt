package com.ecofinds

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ecofinds.database.EcoFindsDatabase
import com.ecofinds.model.Product
import com.ecofinds.model.CartItem
import com.ecofinds.model.Purchase
import com.ecofinds.utils.SessionManager
import com.bumptech.glide.Glide
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var database: EcoFindsDatabase
    
    private lateinit var ivProductImage: ImageView
    private lateinit var tvTitle: TextView
    private lateinit var tvCategory: TextView
    private lateinit var tvPrice: TextView
    private lateinit var tvDescription: TextView
    private lateinit var btnAddToCart: Button
    private lateinit var btnBuyNow: Button
    private lateinit var btnEdit: Button
    private lateinit var btnDelete: Button
    
    private var productId: Long = -1
    private var currentProduct: Product? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        sessionManager = SessionManager(this)
        database = EcoFindsDatabase.getDatabase(this)
        
        productId = intent.getLongExtra("product_id", -1)
        
        if (productId == -1L) {
            Toast.makeText(this, "Product not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        initViews()
        loadProduct()
    }

    private fun initViews() {
        ivProductImage = findViewById(R.id.ivProductImage)
        tvTitle = findViewById(R.id.tvTitle)
        tvCategory = findViewById(R.id.tvCategory)
        tvPrice = findViewById(R.id.tvPrice)
        tvDescription = findViewById(R.id.tvDescription)
        btnAddToCart = findViewById(R.id.btnAddToCart)
        btnBuyNow = findViewById(R.id.btnBuyNow)
        btnEdit = findViewById(R.id.btnEdit)
        btnDelete = findViewById(R.id.btnDelete)

        btnAddToCart.setOnClickListener { addToCart() }
        btnBuyNow.setOnClickListener { buyNow() }
        btnEdit.setOnClickListener { editProduct() }
        btnDelete.setOnClickListener { deleteProduct() }
    }

    private fun loadProduct() {
        lifecycleScope.launch {
            try {
                val product = database.productDao().getProductById(productId)
                if (product != null) {
                    currentProduct = product
                    displayProduct(product)
                } else {
                    Toast.makeText(this@ProductDetailActivity, "Product not found", Toast.LENGTH_SHORT).show()
                    finish()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProductDetailActivity, "Error loading product: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
                finish()
            }
        }
    }

    private fun displayProduct(product: Product) {
        tvTitle.text = product.title
        tvCategory.text = product.category
        tvDescription.text = product.description
        
        val formatter = NumberFormat.getCurrencyInstance(Locale.getDefault())
        tvPrice.text = formatter.format(product.price)

        // Load product image
        Glide.with(this)
            .load(product.imageUrl.ifEmpty { com.ecofinds.utils.Constants.IMAGE_PLACEHOLDER_URL })
            .placeholder(R.drawable.ic_product_placeholder)
            .error(R.drawable.ic_product_placeholder)
            .into(ivProductImage)

        // Show/hide buttons based on ownership
        val currentUserId = sessionManager.getUserId()
        val isOwner = product.sellerId == currentUserId

        if (isOwner) {
            btnAddToCart.visibility = android.view.View.GONE
            btnBuyNow.visibility = android.view.View.GONE
            btnEdit.visibility = android.view.View.VISIBLE
            btnDelete.visibility = android.view.View.VISIBLE
        } else {
            btnAddToCart.visibility = android.view.View.VISIBLE
            btnBuyNow.visibility = android.view.View.VISIBLE
            btnEdit.visibility = android.view.View.GONE
            btnDelete.visibility = android.view.View.GONE
        }
    }

    private fun addToCart() {
        val currentUserId = sessionManager.getUserId()
        
        lifecycleScope.launch {
            try {
                // Check if item is already in cart
                val existingCartItem = database.cartDao().getCartItem(currentUserId, productId)
                
                if (existingCartItem == null) {
                    val cartItem = CartItem(
                        userId = currentUserId,
                        productId = productId
                    )
                    database.cartDao().addToCart(cartItem)
                    Toast.makeText(this@ProductDetailActivity, "Added to cart", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ProductDetailActivity, "Item already in cart", Toast.LENGTH_SHORT).show()
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProductDetailActivity, "Error adding to cart: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun buyNow() {
        currentProduct?.let { product ->
            val currentUserId = sessionManager.getUserId()
            
            lifecycleScope.launch {
                try {
                    // Create purchase record
                    val purchase = Purchase(
                        buyerId = currentUserId,
                        productId = productId,
                        purchasePrice = product.price
                    )
                    database.purchaseDao().insertPurchase(purchase)
                    
                    // Mark product as unavailable
                    val updatedProduct = product.copy(isAvailable = false)
                    database.productDao().updateProduct(updatedProduct)
                    
                    Toast.makeText(this@ProductDetailActivity, "Purchase successful!", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@ProductDetailActivity, "Purchase failed: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun editProduct() {
        val intent = Intent(this, AddProductActivity::class.java)
        intent.putExtra("product_id", productId)
        startActivity(intent)
    }

    private fun deleteProduct() {
        currentProduct?.let { product ->
            lifecycleScope.launch {
                try {
                    database.productDao().deleteProduct(product)
                    Toast.makeText(this@ProductDetailActivity, "Product deleted", Toast.LENGTH_SHORT).show()
                    finish()
                } catch (e: Exception) {
                    Toast.makeText(this@ProductDetailActivity, "Error deleting product: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadProduct()
    }
}
