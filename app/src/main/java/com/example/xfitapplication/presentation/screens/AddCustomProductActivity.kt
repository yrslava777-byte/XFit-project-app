package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.domain.usecase.AddProductUseCase
import com.example.xfitapplication.domain.usecase.ValidateNutritionPer100UseCase
import com.example.xfitapplication.domain.usecase.ValidateProductNameUseCase
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.util.DecimalFieldSpec
import com.example.xfitapplication.presentation.util.setupDecimalField
import com.example.xfitapplication.presentation.util.setupProductNameField
import com.example.xfitapplication.presentation.util.setFieldError
import com.example.xfitapplication.presentation.util.showFormErrors
import com.example.xfitapplication.presentation.viewmodel.AddCustomProductViewModel
import com.google.android.material.textfield.TextInputEditText

class AddCustomProductActivity : AppCompatActivity() {

    private val viewModel: AddCustomProductViewModel by viewModels {
        ViewModelFactory { AddCustomProductViewModel(application) }
    }

    private val validateName = ValidateProductNameUseCase()
    private val validateNutrition = ValidateNutritionPer100UseCase()

    private lateinit var etProductName: TextInputEditText
    private lateinit var etCalories: TextInputEditText
    private lateinit var etProtein: TextInputEditText
    private lateinit var etFat: TextInputEditText
    private lateinit var etCarbs: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_custom_product)

        etProductName = findViewById(R.id.etProductName)
        etCalories = findViewById(R.id.etCalories)
        etProtein = findViewById(R.id.etProtein)
        etFat = findViewById(R.id.etFat)
        etCarbs = findViewById(R.id.etCarbs)

        listOf(etCalories, etProtein, etFat, etCarbs).forEach { it.setupDecimalField() }
        etProductName.setupProductNameField()

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBack)
            .setOnClickListener { finish() }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCancel)
            .setOnClickListener { finish() }

        viewModel.saveResult.observe(this) { event ->
            when (val result = event?.getContentIfNotHandled()) {
                is AddCustomProductViewModel.SaveResult.Success -> openAddFood(result.product)
                is AddCustomProductViewModel.SaveResult.Error -> {
                    showFormErrors(this, result.message.split("\n"))
                }
                null -> Unit
            }
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSave)
            .setOnClickListener { saveProduct() }
    }

    private fun saveProduct() {
        val nameRaw = etProductName.text?.toString().orEmpty()
        val nameError = validateName.execute(nameRaw).message
        if (nameError != null) {
            etProductName.setFieldError(nameError)
        } else {
            etProductName.setFieldError(null)
        }

        val nutritionValidation = com.example.xfitapplication.presentation.util.applyDecimalFieldErrors(
            listOf(
                DecimalFieldSpec(etCalories, "Калории") { value ->
                    validateNutrition.execute(ValidateNutritionPer100UseCase.Nutrient.CALORIES, value).message
                },
                DecimalFieldSpec(etProtein, "Белки") { value ->
                    validateNutrition.execute(ValidateNutritionPer100UseCase.Nutrient.PROTEIN, value).message
                },
                DecimalFieldSpec(etFat, "Жиры") { value ->
                    validateNutrition.execute(ValidateNutritionPer100UseCase.Nutrient.FAT, value).message
                },
                DecimalFieldSpec(etCarbs, "Углеводы") { value ->
                    validateNutrition.execute(ValidateNutritionPer100UseCase.Nutrient.CARBS, value).message
                }
            )
        )

        val errors = buildList {
            nameError?.let { add("Поле «Название»: $it") }
            addAll(nutritionValidation.errorMessages)
        }
        if (errors.isNotEmpty()) {
            showFormErrors(this, errors)
            return
        }

        viewModel.save(
            AddProductUseCase.Form(
                name = nameRaw.trim(),
                caloriesPer100g = nutritionValidation.values[0]!!,
                proteinPer100g = nutritionValidation.values[1]!!,
                fatPer100g = nutritionValidation.values[2]!!,
                carbsPer100g = nutritionValidation.values[3]!!
            )
        )
    }

    private fun openAddFood(product: com.example.xfitapplication.domain.model.Product) {
        if (isFinishing) return
        val mealType = intent.getStringExtra("MEAL_TYPE") ?: "breakfast"
        Toast.makeText(this, "Продукт сохранён", Toast.LENGTH_SHORT).show()
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
