package com.ecofinds

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.ecofinds.database.EcoFindsDatabase
import com.ecofinds.model.Product
import com.ecofinds.utils.SessionManager
import com.ecofinds.utils.Constants
import kotlinx.coroutines.launch

class AddProductActivity : AppCompatActivity() {

    private lateinit var sessionManager: SessionManager
    private lateinit var database: EcoFindsDatabase
    
    private lateinit var etTitle: EditText
    private lateinit var etDescription: EditText
    private lateinit var spCategory: Spinner
    private lateinit var etPrice: EditText
    private lateinit var btnSave: Button
    
    private var productId: Long = -1
    private var isEditMode = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        sessionManager = SessionManager(this)
        database = EcoFindsDatabase.getDatabase(this)

        productId = intent.getLongExtra("product_id", -1)
        isEditMode = productId != -1L

        initViews()
        setupSpinner()
        
        if (isEditMode) {
            title = "Edit Product"
            loadProductData()
        } else {
            title = "Add Product"
        }
    }

    private fun initViews() {
        etTitle = findViewById(R.id.etTitle)
        etDescription = findViewById(R.id.etDescription)
        spCategory = findViewById(R.id.spCategory)
        etPrice = findViewById(R.id.etPrice)
        btnSave = findViewById(R.id.btnSave)
        
        btnSave.setOnClickListener {
            if (validateInput()) {
                saveProduct()
            }
        }
    }

    private fun setupSpinner() {
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, Constants.PRODUCT_CATEGORIES)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spCategory.adapter = adapter
    }

    private fun loadProductData() {
        lifecycleScope.launch {
            try {
                val product = database.productDao().getProductById(productId)
                product?.let {
                    etTitle.setText(it.title)
                    etDescription.setText(it.description)
                    etPrice.setText(it.price.toString())
                    
                    val categoryIndex = Constants.PRODUCT_CATEGORIES.indexOf(it.category)
                    if (categoryIndex >= 0) {
                        spCategory.setSelection(categoryIndex)
                    }
                }
            } catch (e: Exception) {
                Toast.makeText(this@AddProductActivity, "Error loading product: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            }
        }
    }

    private fun validateInput(): Boolean {
        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val priceText = etPrice.text.toString().trim()

        if (title.isEmpty()) {
            etTitle.error = "Title is required"
            return false
        }

        if (description.isEmpty()) {
            etDescription.error = "Description is required"
            return false
        }

        if (priceText.isEmpty()) {
            etPrice.error = "Price is required"
            return false
        }

        try {
            val price = priceText.toDouble()
            if (price <= 0) {
                etPrice.error = "Price must be greater than 0"
                return false
            }
        } catch (e: NumberFormatException) {
            etPrice.error = "Invalid price format"
            return false
        }

        return true
    }

    private fun saveProduct() {
        btnSave.isEnabled = false
        btnSave.text = if (isEditMode) "Updating..." else "Saving..."

        val title = etTitle.text.toString().trim()
        val description = etDescription.text.toString().trim()
        val category = spCategory.selectedItem.toString()
        val price = etPrice.text.toString().toDouble()
        val sellerId = sessionManager.getUserId()

        lifecycleScope.launch {
            try {
                if (isEditMode) {
                    // Update existing product
                    val existingProduct = database.productDao().getProductById(productId)
                    existingProduct?.let { product ->
                        val updatedProduct = product.copy(
                            title = title,
                            description = description,
                            category = category,
                            price = price
                        )
                        database.productDao().updateProduct(updatedProduct)
                        Toast.makeText(this@AddProductActivity, "Product updated successfully", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // Create new product
                    val product = Product(
                        title = title,
                        description = description,
                        category = category,
                        price = price,
                        sellerId = sellerId,
                        imageUrl = Constants.IMAGE_PLACEHOLDER_URL
                    )
                    
                    database.productDao().insertProduct(product)
                    Toast.makeText(this@AddProductActivity, "Product added successfully", Toast.LENGTH_SHORT).show()
                }
                
                finish()
            } catch (e: Exception) {
                Toast.makeText(this@AddProductActivity, "Error saving product: ${e.message}", Toast.LENGTH_SHORT).show()
                e.printStackTrace()
            } finally {
                btnSave.isEnabled = true
                btnSave.text = "Save"
            }
        }
    }
}
