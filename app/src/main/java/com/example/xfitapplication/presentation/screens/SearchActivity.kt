package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doAfterTextChanged
import com.example.xfitapplication.R
import com.example.xfitapplication.domain.model.Product
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.viewmodel.SearchViewModel

class SearchActivity : AppCompatActivity() {

    private val viewModel: SearchViewModel by viewModels {
        ViewModelFactory { SearchViewModel(application) }
    }

    private lateinit var lvResults: ListView
    private var products: List<Product> = emptyList()
    private lateinit var adapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        val etSearch = findViewById<com.google.android.material.textfield.TextInputEditText>(R.id.etSearchQuery)
        lvResults = findViewById(R.id.lvSearchResults)
        val mealType = intent.getStringExtra("MEAL_TYPE") ?: "breakfast"

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBack)
            .setOnClickListener { finish() }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAddCustom)
            .setOnClickListener {
                startActivity(
                    Intent(this, AddCustomProductActivity::class.java).apply {
                        putExtra("MEAL_TYPE", mealType)
                    }
                )
            }

        adapter = ArrayAdapter(this, R.layout.item_product_list, R.id.tvProductItem, mutableListOf())
        lvResults.adapter = adapter

        viewModel.products.observe(this) { list ->
            products = list
            adapter.clear()
            adapter.addAll(list.map { it.name })
            adapter.notifyDataSetChanged()
        }

        etSearch.doAfterTextChanged { text ->
            viewModel.setQuery(text?.toString().orEmpty())
        }

        lvResults.setOnItemClickListener { _, _, position, _ ->
            val product = products.getOrNull(position) ?: return@setOnItemClickListener

            startActivity(
                Intent(this, AddFoodActivity::class.java).apply {
                    putExtra("PRODUCT_ID", product.id)
                    putExtra("PRODUCT_NAME", product.name)
                    putExtra("KCAL_PER_100", product.caloriesPer100g)
                    putExtra("PROT_PER_100", product.proteinPer100g)
                    putExtra("FAT_PER_100", product.fatPer100g)
                    putExtra("CARB_PER_100", product.carbsPer100g)
                    putExtra("MEAL_TYPE", mealType)
                }
            )
            finish()
        }
    }
}
