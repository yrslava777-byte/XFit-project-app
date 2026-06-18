package com.example.xfitapplication.presentation.screens

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.viewmodel.EditProfileViewModel
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity : AppCompatActivity() {

    private val viewModel: EditProfileViewModel by viewModels {
        ViewModelFactory { EditProfileViewModel(application) }
    }

    private var currentGender = "female"
    private var currentActivity = 2

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
            if (user == null) return@observe
            currentGender = user.gender
            currentActivity = user.activityLevel
            etHeight.setText(user.heightCm.toInt().toString())
            etWeight.setText(user.weightKg.toInt().toString())
            etAge.setText(user.ageYears.toString())
            etTargetWeight.setText((user.targetWeightKg ?: user.weightKg).toInt().toString())
            etCalories.setText(user.dailyCalories.toInt().toString())
            etProtein.setText(user.dailyProtein.toInt().toString())
            etFat.setText(user.dailyFat.toInt().toString())
            etCarbs.setText(user.dailyCarbs.toInt().toString())
        }

        viewModel.saveResult.observe(this) { result ->
            when (result) {
                is EditProfileViewModel.SaveResult.Success -> {
                    Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
                is EditProfileViewModel.SaveResult.Error -> {
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
            }
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSave)
            .setOnClickListener { save(recalculateNorm = true) }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnReset)
            .setOnClickListener {
                viewModel.user.value?.let { user ->
                    etHeight.setText(user.heightCm.toInt().toString())
                    etWeight.setText(user.weightKg.toInt().toString())
                    etAge.setText(user.ageYears.toString())
                    etTargetWeight.setText((user.targetWeightKg ?: user.weightKg).toInt().toString())
                    etCalories.setText(user.dailyCalories.toInt().toString())
                    etProtein.setText(user.dailyProtein.toInt().toString())
                    etFat.setText(user.dailyFat.toInt().toString())
                    etCarbs.setText(user.dailyCarbs.toInt().toString())
                    Toast.makeText(this, "Изменения сброшены", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun save(recalculateNorm: Boolean) {
        val height = etHeight.text.toString().toDoubleOrNull()
        val weight = etWeight.text.toString().toDoubleOrNull()
        val age = etAge.text.toString().toIntOrNull()
        val targetWeight = etTargetWeight.text.toString().toDoubleOrNull()
        val calories = etCalories.text.toString().toDoubleOrNull()
        val protein = etProtein.text.toString().toDoubleOrNull()
        val fat = etFat.text.toString().toDoubleOrNull()
        val carbs = etCarbs.text.toString().toDoubleOrNull()

        if (height == null || weight == null || age == null || targetWeight == null ||
            calories == null || protein == null || fat == null || carbs == null
        ) {
            Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
            return
        }

        viewModel.save(
            height, weight, age, targetWeight,
            calories, protein, fat, carbs,
            currentGender, currentActivity, recalculateNorm
        )
    }
}
