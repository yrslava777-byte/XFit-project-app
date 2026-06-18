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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val calories = intent.getDoubleExtra("CALORIES", 2000.0)
        val protein = intent.getDoubleExtra("PROTEIN", 120.0)
        val fat = intent.getDoubleExtra("FAT", 60.0)
        val carbs = intent.getDoubleExtra("CARBS", 250.0)

        val txtCalories = findViewById<TextView>(R.id.txtCalories)
        val circleProgress = findViewById<ProgressBar>(R.id.progressCircle)

        // Прогресс-бары БЖУ
        val barProtein = findViewById<ProgressBar>(R.id.barProtein)
        val barFat = findViewById<ProgressBar>(R.id.barFat)
        val barCarbs = findViewById<ProgressBar>(R.id.barCarbs)


        // Калории
        txtCalories.text = "0 / ${calories.toInt()} ккал" // Пока съедено 0
        circleProgress.max = calories.toInt()
        circleProgress.progress = 0 // Пока съедено 0

        // Кнопки
        val btnBreakfast = findViewById<Button>(R.id.btnBreakfast)
        val btnDiary = findViewById<Button>(R.id.btnDiary)
    }
}