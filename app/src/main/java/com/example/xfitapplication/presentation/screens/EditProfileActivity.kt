package com.example.xfitapplication.presentation.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.util.parseStrictInt
import com.example.xfitapplication.presentation.viewmodel.EditProfileViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.roundToInt

class EditProfileActivity : AppCompatActivity() {

    private val viewModel: EditProfileViewModel by viewModels {
        ViewModelFactory { EditProfileViewModel(application) }
    }

    private var isFormLoaded = false

    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var etTargetWeight: TextInputEditText
    private lateinit var etCalories: TextInputEditText
    private lateinit var etProtein: TextInputEditText
    private lateinit var etFat: TextInputEditText
    private lateinit var etCarbs: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        etAge = findViewById(R.id.etAge)
        etTargetWeight = findViewById(R.id.etTargetWeight)
        etCalories = findViewById(R.id.etCalories)
        etProtein = findViewById(R.id.etProtein)
        etFat = findViewById(R.id.etFat)
        etCarbs = findViewById(R.id.etCarbs)

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBack)
            .setOnClickListener { finish() }

        viewModel.user.observe(this) { user ->
            if (user == null || isFormLoaded) return@observe
            isFormLoaded = true
            fillForm(
                user.heightCm.roundToInt(),
                user.weightKg.roundToInt(),
                user.ageYears,
                (user.targetWeightKg ?: user.weightKg).roundToInt(),
                user.dailyCalories.roundToInt(),
                user.dailyProtein.roundToInt(),
                user.dailyFat.roundToInt(),
                user.dailyCarbs.roundToInt()
            )
        }

        viewModel.saveResult.observe(this) { event ->
            when (val result = event?.getContentIfNotHandled()) {
                is EditProfileViewModel.SaveResult.Success -> {
                    Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
                is EditProfileViewModel.SaveResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
                null -> Unit
            }
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSave)
            .setOnClickListener {
                readForm()?.let { viewModel.save(it) }
            }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnReset)
            .setOnClickListener {
                viewModel.user.value?.let { user ->
                    fillForm(
                        user.heightCm.roundToInt(),
                        user.weightKg.roundToInt(),
                        user.ageYears,
                        (user.targetWeightKg ?: user.weightKg).roundToInt(),
                        user.dailyCalories.roundToInt(),
                        user.dailyProtein.roundToInt(),
                        user.dailyFat.roundToInt(),
                        user.dailyCarbs.roundToInt()
                    )
                    Toast.makeText(this, "Изменения сброшены", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fillForm(
        height: Int, weight: Int, age: Int, targetWeight: Int,
        calories: Int, protein: Int, fat: Int, carbs: Int
    ) {
        etHeight.setText(height.toString())
        etWeight.setText(weight.toString())
        etAge.setText(age.toString())
        etTargetWeight.setText(targetWeight.toString())
        etCalories.setText(calories.toString())
        etProtein.setText(protein.toString())
        etFat.setText(fat.toString())
        etCarbs.setText(carbs.toString())
    }

    private fun readForm(): EditProfileViewModel.ProfileForm? {
        val height = readIntField(etHeight, "Рост")
        val weight = readIntField(etWeight, "Вес")
        val age = readIntField(etAge, "Возраст")
        val targetWeight = readIntField(etTargetWeight, "Целевой вес")
        val calories = readIntField(etCalories, "Калории")
        val protein = readIntField(etProtein, "Белки")
        val fat = readIntField(etFat, "Жиры")
        val carbs = readIntField(etCarbs, "Углеводы")

        if (height == null || weight == null || age == null || targetWeight == null ||
            calories == null || protein == null || fat == null || carbs == null
        ) {
            return null
        }

        return EditProfileViewModel.ProfileForm(
            height = height,
            weight = weight,
            age = age,
            targetWeight = targetWeight,
            calories = calories,
            protein = protein,
            fat = fat,
            carbs = carbs
        )
    }

    private fun readIntField(field: TextInputEditText, label: String): Int? {
        val raw = field.text?.toString().orEmpty()
        val value = raw.parseStrictInt()
        if (value == null) {
            field.error = "Только целое число"
            Toast.makeText(this, "Поле «$label»: введите целое число", Toast.LENGTH_SHORT).show()
        } else {
            field.error = null
        }
        return value
    }
}
