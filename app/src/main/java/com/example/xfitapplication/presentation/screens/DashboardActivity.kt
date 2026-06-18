package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.viewmodel.DashboardViewModel

class DashboardActivity : AppCompatActivity() {

    private val viewModel: DashboardViewModel by viewModels {
        ViewModelFactory { DashboardViewModel(application) }
    }

    private lateinit var txtCal: TextView
    private lateinit var circleProg: ProgressBar
    private lateinit var barProt: ProgressBar
    private lateinit var barFat: ProgressBar
    private lateinit var barCarb: ProgressBar
    private lateinit var tvProteinValue: TextView
    private lateinit var tvFatValue: TextView
    private lateinit var tvCarbsValue: TextView
    private lateinit var tvBreakfastCal: TextView
    private lateinit var tvLunchCal: TextView
    private lateinit var tvDinnerCal: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        txtCal = findViewById(R.id.txtCalories)
        circleProg = findViewById(R.id.progressCircle)
        barProt = findViewById(R.id.barProtein)
        barFat = findViewById(R.id.barFat)
        barCarb = findViewById(R.id.barCarbs)
        tvProteinValue = findViewById(R.id.tvProteinValue)
        tvFatValue = findViewById(R.id.tvFatValue)
        tvCarbsValue = findViewById(R.id.tvCarbsValue)
        tvBreakfastCal = findViewById(R.id.tvBreakfastCal)
        tvLunchCal = findViewById(R.id.tvLunchCal)
        tvDinnerCal = findViewById(R.id.tvDinnerCal)

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBreakfast)
            .setOnClickListener { startSearch("breakfast") }
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnLunch)
            .setOnClickListener { startSearch("lunch") }
        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnDinner)
            .setOnClickListener { startSearch("dinner") }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnDiary)
            .setOnClickListener { startActivity(Intent(this, DiaryActivity::class.java)) }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnProfile)
            .setOnClickListener { startActivity(Intent(this, ProfileActivity::class.java)) }

        viewModel.uiState.observe(this) { state ->
            val progress = state.progress ?: return@observe

            circleProg.max = progress.normCalories.toInt().coerceAtLeast(1)
            barProt.max = progress.normProtein.toInt().coerceAtLeast(1)
            barFat.max = progress.normFat.toInt().coerceAtLeast(1)
            barCarb.max = progress.normCarbs.toInt().coerceAtLeast(1)

            txtCal.text = "${progress.consumedCalories.toInt()} / ${progress.normCalories.toInt()}"
            circleProg.progress = progress.consumedCalories.toInt()
            barProt.progress = progress.consumedProtein.toInt()
            barFat.progress = progress.consumedFat.toInt()
            barCarb.progress = progress.consumedCarbs.toInt()

            tvProteinValue.text = "${progress.consumedProtein.toInt()} / ${progress.normProtein.toInt()} г"
            tvFatValue.text = "${progress.consumedFat.toInt()} / ${progress.normFat.toInt()} г"
            tvCarbsValue.text = "${progress.consumedCarbs.toInt()} / ${progress.normCarbs.toInt()} г"

            tvBreakfastCal.text = "${progress.breakfastCalories.toInt()} / 500 ккал"
            tvLunchCal.text = "${progress.lunchCalories.toInt()} / 700 ккал"
            tvDinnerCal.text = "${progress.dinnerCalories.toInt()} / 550 ккал"
        }
    }

    private fun startSearch(type: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("MEAL_TYPE", type)
        startActivity(intent)
    }
}
