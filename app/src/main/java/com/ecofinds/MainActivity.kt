package com.ecofinds

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecofinds.adapter.ProductAdapter
import com.ecofinds.database.EcoFindsDatabase
import com.ecofinds.model.Product
import com.ecofinds.utils.SessionManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var database: EcoFindsDatabase
    private lateinit var productAdapter: ProductAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var bottomNavigation: BottomNavigationView
    private lateinit var categoryChipGroup: ChipGroup
    private lateinit var searchView: SearchView
    private lateinit var fab: FloatingActionButton
    
    private var allProducts = mutableListOf<Product>()
    private var filteredProducts = mutableListOf<Product>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sessionManager = SessionManager(this)
        database = EcoFindsDatabase.getDatabase(this)

        // Check if user is logged in
        if (!sessionManager.isLoggedIn()) {
            startActivity(Intent(this, LoginActivity::class.java))
            finish()
            return
        }

        initViews()
        setupRecyclerView()
        setupBottomNavigation()
        setupCategoryFilters()
        loadProducts()
    }

    private fun initViews() {
        recyclerView = findViewById(R.id.recyclerViewProducts)
        bottomNavigation = findViewById(R.id.bottomNavigation)
        categoryChipGroup = findViewById(R.id.categoryChipGroup)
        fab = findViewById(R.id.fabAddProduct)
        
        fab.setOnClickListener {
            startActivity(Intent(this, AddProductActivity::class.java))
        }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }
        
        recyclerView.apply {
            adapter = productAdapter
            layoutManager = GridLayoutManager(this@MainActivity, 2)
        }
    }

    private fun setupBottomNavigation() {
        bottomNavigation.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_home -> {
                    // Already on home
                    true
                }
                R.id.nav_profile -> {
                    startActivity(Intent(this, ProfileActivity::class.java))
                    true
                }
                R.id.nav_cart -> {
                    startActivity(Intent(this, CartActivity::class.java))
                    true
                }
                R.id.nav_history -> {
                    startActivity(Intent(this, PurchaseHistoryActivity::class.java))
                    true
                }
                else -> false
            }
        }
    }

    private fun setupCategoryFilters() {
        com.ecofinds.utils.Constants.PRODUCT_CATEGORIES.forEach { category ->
            val chip = Chip(this)
            chip.text = category
            chip.isCheckable = true
            categoryChipGroup.addView(chip)
        }

        categoryChipGroup.setOnCheckedStateChangeListener { group, checkedIds ->
            filterProducts()
        }
    }

    private fun loadProducts() {
        lifecycleScope.launch {
            try {
                allProducts.clear()
                allProducts.addAll(database.productDao().getAllAvailableProducts())
                filteredProducts.clear()
                filteredProducts.addAll(allProducts)
                productAdapter.updateProducts(filteredProducts)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun filterProducts() {
        val selectedCategories = mutableListOf<String>()
        
        for (i in 0 until categoryChipGroup.childCount) {
            val chip = categoryChipGroup.getChildAt(i) as Chip
            if (chip.isChecked) {
                selectedCategories.add(chip.text.toString())
            }
        }

        filteredProducts.clear()
        if (selectedCategories.isEmpty()) {
            filteredProducts.addAll(allProducts)
        } else {
            filteredProducts.addAll(allProducts.filter { 
                selectedCategories.contains(it.category) 
            })
        }

        productAdapter.updateProducts(filteredProducts)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        
        val searchItem = menu?.findItem(R.id.action_search)
        searchView = searchItem?.actionView as SearchView
        
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                searchProducts(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    filterProducts()
                } else {
                    searchProducts(newText)
                }
                return true
            }
        })
        
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_logout -> {
                sessionManager.clearSession()
                startActivity(Intent(this, LoginActivity::class.java))
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun searchProducts(query: String?) {
        if (query.isNullOrEmpty()) {
            filterProducts()
            return
        }

        lifecycleScope.launch {
            try {
                val searchResults = database.productDao().searchProducts("%$query%")
                filteredProducts.clear()
                filteredProducts.addAll(searchResults)
                productAdapter.updateProducts(filteredProducts)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadProducts()
    }
}
