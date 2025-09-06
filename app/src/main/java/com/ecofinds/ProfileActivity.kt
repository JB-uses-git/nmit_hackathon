package com.ecofinds

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ecofinds.adapter.ProductAdapter
import com.ecofinds.database.EcoFindsDatabase
import com.ecofinds.model.Product
import com.ecofinds.model.User
import com.ecofinds.utils.SessionManager
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var database: EcoFindsDatabase
    private lateinit var productAdapter: ProductAdapter
    
    private lateinit var tvUsername: TextView
    private lateinit var tvEmail: TextView
    private lateinit var etUsername: EditText
    private lateinit var etEmail: EditText
    private lateinit var btnEdit: Button
    private lateinit var btnSave: Button
    private lateinit var btnCancel: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var tvMyProducts: TextView
    private lateinit var tvNoProducts: TextView
    
    private var myProducts = mutableListOf<Product>()
    private var isEditMode = false
    private var currentUser: User? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        sessionManager = SessionManager(this)
        database = EcoFindsDatabase.getDatabase(this)

        title = "My Profile"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initViews()
        setupRecyclerView()
        loadUserProfile()
        loadMyProducts()
    }

    private fun initViews() {
        tvUsername = findViewById(R.id.tvUsername)
        tvEmail = findViewById(R.id.tvEmail)
        etUsername = findViewById(R.id.etUsername)
        etEmail = findViewById(R.id.etEmail)
        btnEdit = findViewById(R.id.btnEdit)
        btnSave = findViewById(R.id.btnSave)
        btnCancel = findViewById(R.id.btnCancel)
        recyclerView = findViewById(R.id.recyclerViewMyProducts)
        tvMyProducts = findViewById(R.id.tvMyProducts)
        tvNoProducts = findViewById(R.id.tvNoProducts)

        btnEdit.setOnClickListener { enableEditMode() }
        btnSave.setOnClickListener { saveProfile() }
        btnCancel.setOnClickListener { disableEditMode() }
    }

    private fun setupRecyclerView() {
        productAdapter = ProductAdapter { product ->
            val intent = Intent(this, ProductDetailActivity::class.java)
            intent.putExtra("product_id", product.id)
            startActivity(intent)
        }
        
        recyclerView.apply {
            adapter = productAdapter
            layoutManager = LinearLayoutManager(this@ProfileActivity)
        }
    }

    private fun loadUserProfile() {
        val currentUserId = sessionManager.getUserId()
        
        lifecycleScope.launch {
            try {
                currentUser = database.userDao().getUserById(currentUserId)
                currentUser?.let { user ->
                    tvUsername.text = user.username
                    tvEmail.text = user.email
                    etUsername.setText(user.username)
                    etEmail.setText(user.email)
                }
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error loading profile: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun loadMyProducts() {
        val currentUserId = sessionManager.getUserId()
        
        lifecycleScope.launch {
            try {
                myProducts.clear()
                myProducts.addAll(database.productDao().getProductsBySeller(currentUserId))
                
                if (myProducts.isEmpty()) {
                    showNoProducts()
                } else {
                    showProducts()
                }
                
                productAdapter.updateProducts(myProducts)
            } catch (e: Exception) {
                Toast.makeText(this@ProfileActivity, "Error loading products: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun showNoProducts() {
        recyclerView.visibility = android.view.View.GONE
        tvNoProducts.visibility = android.view.View.VISIBLE
    }

    private fun showProducts() {
        recyclerView.visibility = android.view.View.VISIBLE
        tvNoProducts.visibility = android.view.View.GONE
    }

    private fun enableEditMode() {
        isEditMode = true
        tvUsername.visibility = android.view.View.GONE
        tvEmail.visibility = android.view.View.GONE
        etUsername.visibility = android.view.View.VISIBLE
        etEmail.visibility = android.view.View.VISIBLE
        btnEdit.visibility = android.view.View.GONE
        btnSave.visibility = android.view.View.VISIBLE
        btnCancel.visibility = android.view.View.VISIBLE
    }

    private fun disableEditMode() {
        isEditMode = false
        tvUsername.visibility = android.view.View.VISIBLE
        tvEmail.visibility = android.view.View.VISIBLE
        etUsername.visibility = android.view.View.GONE
        etEmail.visibility = android.view.View.GONE
        btnEdit.visibility = android.view.View.VISIBLE
        btnSave.visibility = android.view.View.GONE
        btnCancel.visibility = android.view.View.GONE
        
        // Reset values
        currentUser?.let { user ->
            etUsername.setText(user.username)
            etEmail.setText(user.email)
        }
    }

    private fun saveProfile() {
        val newUsername = etUsername.text.toString().trim()
        val newEmail = etEmail.text.toString().trim()

        if (!validateInput(newUsername, newEmail)) {
            return
        }

        currentUser?.let { user ->
            lifecycleScope.launch {
                try {
                    // Check if email is already taken by another user
                    val existingUser = database.userDao().getUserByEmail(newEmail)
                    if (existingUser != null && existingUser.id != user.id) {
                        Toast.makeText(this@ProfileActivity, "Email is already taken", Toast.LENGTH_SHORT).show()
                        return@launch
                    }

                    val updatedUser = user.copy(username = newUsername, email = newEmail)
                    database.userDao().updateUser(updatedUser)
                    
                    // Update session
                    sessionManager.saveUserSession(user.id, newUsername, newEmail)
                    
                    tvUsername.text = newUsername
                    tvEmail.text = newEmail
                    currentUser = updatedUser
                    
                    Toast.makeText(this@ProfileActivity, "Profile updated successfully", Toast.LENGTH_SHORT).show()
                    disableEditMode()
                } catch (e: Exception) {
                    Toast.makeText(this@ProfileActivity, "Error updating profile: ${e.message}", Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }
            }
        }
    }

    private fun validateInput(username: String, email: String): Boolean {
        if (username.isEmpty()) {
            etUsername.error = "Username is required"
            return false
        }

        if (username.length < 3) {
            etUsername.error = "Username must be at least 3 characters"
            return false
        }

        if (email.isEmpty()) {
            etEmail.error = "Email is required"
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = "Invalid email format"
            return false
        }

        return true
    }

    override fun onResume() {
        super.onResume()
        loadMyProducts()
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}
