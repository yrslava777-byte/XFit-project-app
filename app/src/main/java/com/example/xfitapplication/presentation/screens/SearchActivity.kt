package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R

class SearchActivity : AppCompatActivity() {
    data class ProductInfo(
        val name: String,
        val kcal: Double,
        val prot: Double,
        val fat: Double,
        val carb: Double
    )

    private lateinit var etSearch: EditText
    private lateinit var lvResults: ListView
    private lateinit var addFoodLauncher: ActivityResultLauncher<Intent>

    private val products = listOf(
        ProductInfo("Куриная грудка", 165.0, 31.0, 3.6, 0.0),
        ProductInfo("Рис отварной", 130.0, 2.7, 0.3, 28.0),
        ProductInfo("Гречка", 110.0, 4.2, 1.1, 20.0),
        ProductInfo("Яблоко", 52.0, 0.3, 0.2, 14.0),
        ProductInfo("Молоко 2.5%", 54.0, 2.8, 2.5, 4.7),
        ProductInfo("Хлеб черный", 250.0, 8.5, 1.2, 48.0),
        ProductInfo("Творог 5%", 120.0, 17.0, 5.0, 1.8),
        ProductInfo("Яйцо куриное", 155.0, 13.0, 11.0, 1.1)
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearch = findViewById(R.id.etSearchQuery)
        lvResults = findViewById(R.id.lvSearchResults)

        val adapter = ArrayAdapter(this, R.layout.item_product_list, products.map { it.name })
        lvResults.adapter = adapter

        addFoodLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                setResult(RESULT_OK, result.data)
                finish()
            }
        }

        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.filter.filter(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        lvResults.setOnItemClickListener { _, _, position, _ ->
            val selected = adapter.getItem(position) ?: return@setOnItemClickListener
            val product = products.find { it.name == selected } ?: return@setOnItemClickListener

            val mealType = intent.getStringExtra("MEAL_TYPE") ?: "breakfast"

            val intent = Intent(this, AddFoodActivity::class.java).apply {
                putExtra("PRODUCT_NAME", product.name)
                putExtra("KCAL_PER_100", product.kcal)
                putExtra("PROT_PER_100", product.prot)
                putExtra("FAT_PER_100", product.fat)
                putExtra("CARB_PER_100", product.carb)
                putExtra("MEAL_TYPE", mealType)
            }
            addFoodLauncher.launch(intent)
        }
    }
}