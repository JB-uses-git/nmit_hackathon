package com.ecofinds

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecofinds.adapter.ProductAdapter
import com.ecofinds.database.EcoFindsDatabase
import com.ecofinds.model.Product
import com.ecofinds.utils.SessionManager
import kotlinx.coroutines.launch

class PurchaseHistoryActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var database: EcoFindsDatabase
    private lateinit var productAdapter: ProductAdapter
    
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvEmptyHistory: TextView
    
    private var purchasedProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_purchase_history)

        sessionManager = SessionManager(this)
        database = EcoFindsDatabase.getDatabase(this)

        title = "Purchase History"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        setupRecyclerView()
        loadPurchaseHistory()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewHistory)
        tvEmptyHistory = findViewById(R.id.tvEmptyHistory)
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }
        
        recyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(this@PurchaseHistoryActivity)
        }
    }

    private fun loadPurchaseHistory() {
        val currentUserId = sessionManager.getUserId()
        
        lifecycleScope.launch {
            try {
                purchasedProducts.clear()
                purchasedProducts.addAll(database.purchaseDao().getPurchaseHistoryForUser(currentUserId))
                
                if (purchasedProducts.isEmpty()) {
                    showEmptyHistory()
                } else {
                    showHistory()
                }
                
                productAdapter.updateProducts(purchasedProducts)
            } catch (e: Exception) {
                Toast.makeText(this@PurchaseHistoryActivity, "Error loading history: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun showEmptyHistory() {
        recyclerView.visibility = android.view.View.GONE
        tvEmptyHistory.visibility = android.view.View.VISIBLE
    }

    private fun showHistory() {
        recyclerView.visibility = android.view.View.VISIBLE
        tvEmptyHistory.visibility = android.view.View.GONE
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
