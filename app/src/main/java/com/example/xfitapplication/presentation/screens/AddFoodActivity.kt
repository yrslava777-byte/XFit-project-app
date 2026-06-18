package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class AddFoodActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Продукт"
        val kcalPer100 = intent.getDoubleExtra("KCAL_PER_100", 0.0)
        val protPer100 = intent.getDoubleExtra("PROT_PER_100", 0.0)
        val fatPer100 = intent.getDoubleExtra("FAT_PER_100", 0.0)
        val carbPer100 = intent.getDoubleExtra("CARB_PER_100", 0.0)

        val tvProductName = findViewById<TextView>(R.id.tvProductName)
        val tvKcal100 = findViewById<TextView>(R.id.tvKcal100)
        val tvProt100 = findViewById<TextView>(R.id.tvProt100)
        val tvFat100 = findViewById<TextView>(R.id.tvFat100)
        val tvCarb100 = findViewById<TextView>(R.id.tvCarb100)
        val etWeight = findViewById<TextInputEditText>(R.id.etWeight)
        val btnCancel = findViewById<Button>(R.id.btnCancel)
        val btnAdd = findViewById<Button>(R.id.btnAdd)

        tvProductName.text = productName
        tvKcal100.text = "${kcalPer100.toInt()} ккал"
        tvProt100.text = String.format(Locale.getDefault(), "%.1f г", protPer100)
        tvFat100.text = String.format(Locale.getDefault(), "%.1f г", fatPer100)
        tvCarb100.text = String.format(Locale.getDefault(), "%.1f г", carbPer100)

        btnCancel.setOnClickListener { finish() }

        btnAdd.setOnClickListener {
            val weight = etWeight.text.toString().toDoubleOrNull() ?: 0.0
            val factor = weight / 100.0

            val mealType = intent.getStringExtra("MEAL_TYPE") ?: "breakfast"

            val resultIntent = Intent().apply {
                putExtra("PRODUCT_NAME", productName)
                putExtra("WEIGHT", weight)
                putExtra("TOTAL_KCAL", kcalPer100 * factor)
                putExtra("TOTAL_PROT", protPer100 * factor)
                putExtra("TOTAL_FAT", fatPer100 * factor)
                putExtra("TOTAL_CARB", carbPer100 * factor)
                putExtra("MEAL_TYPE", mealType)
            }
            setResult(RESULT_OK, resultIntent)
            finish()
        }
    }
}