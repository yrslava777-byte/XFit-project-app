package com.example.xfitapplication.presentation.screens

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.domain.usecase.ValidatePortionWeightUseCase
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.util.readPortionWeightField
import com.example.xfitapplication.presentation.util.setupDecimalField
import com.example.xfitapplication.presentation.viewmodel.AddFoodViewModel
import com.google.android.material.textfield.TextInputEditText
import java.util.Locale

class AddFoodActivity : AppCompatActivity() {

    private val viewModel: AddFoodViewModel by viewModels {
        ViewModelFactory { AddFoodViewModel(application) }
    }

    private val validatePortionWeight = ValidatePortionWeightUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_food)

        val productId = intent.getIntExtra("PRODUCT_ID", 0)
        val productName = intent.getStringExtra("PRODUCT_NAME") ?: "Продукт"
        val kcalPer100 = intent.getDoubleExtra("KCAL_PER_100", 0.0)
        val protPer100 = intent.getDoubleExtra("PROT_PER_100", 0.0)
        val fatPer100 = intent.getDoubleExtra("FAT_PER_100", 0.0)
        val carbPer100 = intent.getDoubleExtra("CARB_PER_100", 0.0)
        val mealType = intent.getStringExtra("MEAL_TYPE") ?: "breakfast"

        viewModel.setProduct(productId, productName, kcalPer100, protPer100, fatPer100, carbPer100)

        val tvProductName = findViewById<TextView>(R.id.tvProductName)
        val tvKcal100 = findViewById<TextView>(R.id.tvKcal100)
        val tvProt100 = findViewById<TextView>(R.id.tvProt100)
        val tvFat100 = findViewById<TextView>(R.id.tvFat100)
        val tvCarb100 = findViewById<TextView>(R.id.tvCarb100)
        val etWeight = findViewById<TextInputEditText>(R.id.etWeight)
        etWeight.setupDecimalField()

        tvProductName.text = productName
        tvKcal100.text = "${kcalPer100.toInt()} ккал"
        tvProt100.text = String.format(Locale.getDefault(), "%.1f г", protPer100)
        tvFat100.text = String.format(Locale.getDefault(), "%.1f г", fatPer100)
        tvCarb100.text = String.format(Locale.getDefault(), "%.1f г", carbPer100)

        viewModel.error.observe(this) { event ->
            event?.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancel)
            .setOnClickListener { finish() }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnAdd)
            .setOnClickListener {
                val weight = readPortionWeightField(this, etWeight) { value ->
                    validatePortionWeight.execute(value).message
                } ?: return@setOnClickListener

                viewModel.addFood(weight, mealType) {
                    if (isFinishing) return@addFood
                    setResult(RESULT_OK)
                    finish()
                }
            }
    }
}
