package com.example.xfitapplication.presentation.screens

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R

class SearchActivity : AppCompatActivity() {

    private lateinit var etSearch: EditText
    private lateinit var lvResults: ListView

    private val products = listOf(
        "Куриная грудка", "Рис отварной", "Гречка", "Яблоко",
        "Молоко 2.5%", "Хлеб черный", "Творог 5%", "Яйцо куриное"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        etSearch = findViewById(R.id.etSearchQuery)
        lvResults = findViewById(R.id.lvSearchResults)

        // адаптер для списка
        val adapter = ArrayAdapter(this, R.layout.item_product_list, products)
        lvResults.adapter = adapter

        // логика фильтрации при вводе
        etSearch.addTextChangedListener(object : android.text.TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                adapter.getFilter().filter(s.toString())
            }
            override fun afterTextChanged(s: android.text.Editable?) {}
        })

        // Обработка клика по продукту
        lvResults.setOnItemClickListener { parent, view, position, id ->
            val selectedProduct = products[position]
            // TODO: Здесь мы должны открыть экран ВВОДА ВЕСА продукта
            Toast.makeText(this, "Выбран: $selectedProduct", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}