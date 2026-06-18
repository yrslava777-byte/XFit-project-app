package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R

class ProfileActivity : AppCompatActivity() {

    private lateinit var profileLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val btnBack = findViewById<Button>(R.id.btnBack)
        val btnEditParams = findViewById<Button>(R.id.btnEditParams)

        // TextView для отображения данных
        val tvHeight = findViewById<TextView>(R.id.tvHeight)
        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvAge = findViewById<TextView>(R.id.tvAge)
        val tvTargetWeight = findViewById<TextView>(R.id.tvTargetWeight)
        val tvCalories = findViewById<TextView>(R.id.tvCalories)
        val tvProtein = findViewById<TextView>(R.id.tvProtein)
        val tvFat = findViewById<TextView>(R.id.tvFat)
        val tvCarbs = findViewById<TextView>(R.id.tvCarbs)

        // Получаем данные из Intent
        val height = intent.getDoubleExtra("HEIGHT", 170.0)
        val weight = intent.getDoubleExtra("WEIGHT", 70.0)
        val age = intent.getIntExtra("AGE", 25)
        val targetWeight = intent.getDoubleExtra("TARGET_WEIGHT", 65.0)
        val calories = intent.getDoubleExtra("CALORIES", 2000.0)
        val protein = intent.getDoubleExtra("PROTEIN", 120.0)
        val fat = intent.getDoubleExtra("FAT", 60.0)
        val carbs = intent.getDoubleExtra("CARBS", 250.0)

        // Отображаем данные
        tvHeight.text = "${height.toInt()} см"
        tvWeight.text = "${weight.toInt()} кг"
        tvAge.text = "$age года"
        tvTargetWeight.text = "${targetWeight.toInt()} кг"
        tvCalories.text = "${calories.toInt()} ккал"
        tvProtein.text = "${protein.toInt()} г"
        tvFat.text = "${fat.toInt()} г"
        tvCarbs.text = "${carbs.toInt()} г"

        // Кнопка "Назад" на Dashboard
        btnBack.setOnClickListener {
            finish()
        }

        // Лаунчер для экрана редактирования
        profileLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                // Обновляем данные
                val newHeight = data?.getDoubleExtra("HEIGHT", height) ?: height
                val newWeight = data?.getDoubleExtra("WEIGHT", weight) ?: weight
                val newAge = data?.getIntExtra("AGE", age) ?: age
                val newTargetWeight = data?.getDoubleExtra("TARGET_WEIGHT", targetWeight) ?: targetWeight
                val newCalories = data?.getDoubleExtra("CALORIES", calories) ?: calories
                val newProtein = data?.getDoubleExtra("PROTEIN", protein) ?: protein
                val newFat = data?.getDoubleExtra("FAT", fat) ?: fat
                val newCarbs = data?.getDoubleExtra("CARBS", carbs) ?: carbs

                // Обновляем отображение
                tvHeight.text = "${newHeight.toInt()} см"
                tvWeight.text = "${newWeight.toInt()} кг"
                tvAge.text = "$newAge года"
                tvTargetWeight.text = "${newTargetWeight.toInt()} кг"
                tvCalories.text = "${newCalories.toInt()} ккал"
                tvProtein.text = "${newProtein.toInt()} г"
                tvFat.text = "${newFat.toInt()} г"
                tvCarbs.text = "${newCarbs.toInt()} г"

                // Возвращаем обновлённые данные на Dashboard
                val resultIntent = Intent().apply {
                    putExtra("HEIGHT", newHeight)
                    putExtra("WEIGHT", newWeight)
                    putExtra("AGE", newAge)
                    putExtra("CALORIES", newCalories)
                    putExtra("PROTEIN", newProtein)
                    putExtra("FAT", newFat)
                    putExtra("CARBS", newCarbs)
                }
                setResult(RESULT_OK, resultIntent)
            }
        }

        // Кнопка "Изменить параметры"
        btnEditParams.setOnClickListener {
            val intent = Intent(this, EditProfileActivity::class.java).apply {
                putExtra("HEIGHT", height)
                putExtra("WEIGHT", weight)
                putExtra("AGE", age)
                putExtra("TARGET_WEIGHT", targetWeight)
                putExtra("CALORIES", calories)
                putExtra("PROTEIN", protein)
                putExtra("FAT", fat)
                putExtra("CARBS", carbs)
            }
            profileLauncher.launch(intent)
        }
    }
}