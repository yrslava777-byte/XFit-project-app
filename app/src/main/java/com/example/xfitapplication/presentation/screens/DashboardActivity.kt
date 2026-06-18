package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R

class DashboardActivity : AppCompatActivity() {

    private lateinit var btnBreakfast: Button
    private lateinit var btnLunch: Button
    private lateinit var btnDinner: Button
    private lateinit var btnDiary: Button
    private lateinit var btnProfile: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val calories = intent.getDoubleExtra("CALORIES", 2000.0)
        val protein = intent.getDoubleExtra("PROTEIN", 120.0)
        val fat = intent.getDoubleExtra("FAT", 60.0)
        val carbs = intent.getDoubleExtra("CARBS", 250.0)

        // Инициализация View
        val txtCalories = findViewById<TextView>(R.id.txtCalories)
        val circleProgress = findViewById<ProgressBar>(R.id.progressCircle)

        // Прогресс-бары БЖУ
        val barProtein = findViewById<ProgressBar>(R.id.barProtein)
        val barFat = findViewById<ProgressBar>(R.id.barFat)
        val barCarbs = findViewById<ProgressBar>(R.id.barCarbs)

        // Кнопки "+"
        btnBreakfast = findViewById(R.id.btnBreakfast)
        btnLunch = findViewById(R.id.btnLunch)
        btnDinner = findViewById(R.id.btnDinner)

        // Кнопки нижнего меню
        btnDiary = findViewById(R.id.btnDiary)
        btnProfile = findViewById(R.id.btnProfile)


        // Калории
        txtCalories.text = "0 / ${calories.toInt()} ккал"
        circleProgress.max = calories.toInt()
        circleProgress.progress = 0

        // БЖУ
        barProtein.max = protein.toInt()
        barProtein.progress = 0

        barFat.max = fat.toInt()
        barFat.progress = 0

        barCarbs.max = carbs.toInt()
        barCarbs.progress = 0

        // Обработчик для кнопок "+"
        val onClickAdd = View.OnClickListener {
            val intent = Intent(this@DashboardActivity, SearchActivity::class.java)
            startActivity(intent)
        }

        // Назначаем обработчики
        btnBreakfast.setOnClickListener(onClickAdd)
        btnLunch.setOnClickListener(onClickAdd)
        btnDinner.setOnClickListener(onClickAdd)

        // Обработчики кнопок нижнего меню
        btnDiary.setOnClickListener {
            // TODO: Переход на экран дневника
            Toast.makeText(this, "Открыть дневник", Toast.LENGTH_SHORT).show()
        }

        btnProfile.setOnClickListener {
            // TODO: Переход на экран профиля
            Toast.makeText(this, "Открыть профиль", Toast.LENGTH_SHORT).show()
        }
    }
}