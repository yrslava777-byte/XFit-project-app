package com.example.xfitapplication.presentation.screens

import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.google.android.material.textfield.TextInputEditText

class EditProfileActivity : AppCompatActivity() {

    private var originalHeight = 0.0
    private var originalWeight = 0.0
    private var originalAge = 0
    private var originalTargetWeight = 0.0
    private var originalCalories = 0.0
    private var originalProtein = 0.0
    private var originalFat = 0.0
    private var originalCarbs = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnReset = findViewById<Button>(R.id.btnReset)

        val etHeight = findViewById<TextInputEditText>(R.id.etHeight)
        val etWeight = findViewById<TextInputEditText>(R.id.etWeight)
        val etAge = findViewById<TextInputEditText>(R.id.etAge)
        val etTargetWeight = findViewById<TextInputEditText>(R.id.etTargetWeight)
        val etCalories = findViewById<TextInputEditText>(R.id.etCalories)
        val etProtein = findViewById<TextInputEditText>(R.id.etProtein)
        val etFat = findViewById<TextInputEditText>(R.id.etFat)
        val etCarbs = findViewById<TextInputEditText>(R.id.etCarbs)

        originalHeight = intent.getDoubleExtra("HEIGHT", 170.0)
        originalWeight = intent.getDoubleExtra("WEIGHT", 70.0)
        originalAge = intent.getIntExtra("AGE", 25)
        originalTargetWeight = intent.getDoubleExtra("TARGET_WEIGHT", 65.0)
        originalCalories = intent.getDoubleExtra("CALORIES", 2000.0)
        originalProtein = intent.getDoubleExtra("PROTEIN", 120.0)
        originalFat = intent.getDoubleExtra("FAT", 60.0)
        originalCarbs = intent.getDoubleExtra("CARBS", 250.0)

        etHeight.setText(originalHeight.toInt().toString())
        etWeight.setText(originalWeight.toInt().toString())
        etAge.setText(originalAge.toString())
        etTargetWeight.setText(originalTargetWeight.toInt().toString())
        etCalories.setText(originalCalories.toInt().toString())
        etProtein.setText(originalProtein.toInt().toString())
        etFat.setText(originalFat.toInt().toString())
        etCarbs.setText(originalCarbs.toInt().toString())

        btnBack.setOnClickListener { finish() }

        btnSave.setOnClickListener {
            val newHeight = etHeight.text.toString().toDoubleOrNull()
            val newWeight = etWeight.text.toString().toDoubleOrNull()
            val newAge = etAge.text.toString().toIntOrNull()
            val newTargetWeight = etTargetWeight.text.toString().toDoubleOrNull()
            val newCalories = etCalories.text.toString().toDoubleOrNull()
            val newProtein = etProtein.text.toString().toDoubleOrNull()
            val newFat = etFat.text.toString().toDoubleOrNull()
            val newCarbs = etCarbs.text.toString().toDoubleOrNull()

            if (newHeight == null || newWeight == null || newAge == null ||
                newTargetWeight == null || newCalories == null ||
                newProtein == null || newFat == null || newCarbs == null) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val resultIntent = android.content.Intent().apply {
                putExtra("HEIGHT", newHeight)
                putExtra("WEIGHT", newWeight)
                putExtra("AGE", newAge)
                putExtra("TARGET_WEIGHT", newTargetWeight)
                putExtra("CALORIES", newCalories)
                putExtra("PROTEIN", newProtein)
                putExtra("FAT", newFat)
                putExtra("CARBS", newCarbs)
            }

            setResult(RESULT_OK, resultIntent)
            Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show()
            finish()
        }

        btnReset.setOnClickListener {
            etHeight.setText(originalHeight.toInt().toString())
            etWeight.setText(originalWeight.toInt().toString())
            etAge.setText(originalAge.toString())
            etTargetWeight.setText(originalTargetWeight.toInt().toString())
            etCalories.setText(originalCalories.toInt().toString())
            etProtein.setText(originalProtein.toInt().toString())
            etFat.setText(originalFat.toInt().toString())
            etCarbs.setText(originalCarbs.toInt().toString())

            Toast.makeText(this, "Изменения сброшены", Toast.LENGTH_SHORT).show()
        }
    }
}