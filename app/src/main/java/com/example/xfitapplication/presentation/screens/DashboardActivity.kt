package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import java.util.ArrayList

class DashboardActivity : AppCompatActivity() {

    private lateinit var searchLauncher: ActivityResultLauncher<Intent>

    private var consumedCal = 0.0
    private var consumedProt = 0.0
    private var consumedFat = 0.0
    private var consumedCarb = 0.0

    private val diaryEntries = mutableListOf<DiaryEntry>()

    private var normCal = 2000.0
    private var normProt = 120.0
    private var normFat = 60.0
    private var normCarb = 250.0

    private var breakfastCal = 0.0
    private var lunchCal = 0.0
    private var dinnerCal = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        normCal = intent.getDoubleExtra("CALORIES", 2000.0)
        normProt = intent.getDoubleExtra("PROTEIN", 120.0)
        normFat = intent.getDoubleExtra("FAT", 60.0)
        normCarb = intent.getDoubleExtra("CARBS", 250.0)

        val txtCal = findViewById<TextView>(R.id.txtCalories)
        val circleProg = findViewById<ProgressBar>(R.id.progressCircle)
        val barProt = findViewById<ProgressBar>(R.id.barProtein)
        val barFat = findViewById<ProgressBar>(R.id.barFat)
        val barCarb = findViewById<ProgressBar>(R.id.barCarbs)

        val tvBreakfastCal = findViewById<TextView>(R.id.tvBreakfastCal)
        val tvLunchCal = findViewById<TextView>(R.id.tvLunchCal)
        val tvDinnerCal = findViewById<TextView>(R.id.tvDinnerCal)

        val btnBreakfast = findViewById<Button>(R.id.btnBreakfast)
        val btnLunch = findViewById<Button>(R.id.btnLunch)
        val btnDinner = findViewById<Button>(R.id.btnDinner)
        val btnDiary = findViewById<Button>(R.id.btnDiary)
        val btnProfile = findViewById<Button>(R.id.btnProfile)

        circleProg.max = normCal.toInt()
        barProt.max = normProt.toInt()
        barFat.max = normFat.toInt()
        barCarb.max = normCarb.toInt()

        searchLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                val data = result.data
                val calories = data?.getDoubleExtra("TOTAL_KCAL", 0.0) ?: 0.0
                val prot = data?.getDoubleExtra("TOTAL_PROT", 0.0) ?: 0.0
                val fat = data?.getDoubleExtra("TOTAL_FAT", 0.0) ?: 0.0
                val carb = data?.getDoubleExtra("TOTAL_CARB", 0.0) ?: 0.0
                val mealType = data?.getStringExtra("MEAL_TYPE") ?: "breakfast"
                val productName = data?.getStringExtra("PRODUCT_NAME") ?: "Продукт"
                val weight = data?.getDoubleExtra("WEIGHT", 0.0) ?: 0.0

                val entry = DiaryEntry(productName, weight, calories, prot, fat, carb)
                diaryEntries.add(entry)

                when (mealType) {
                    "breakfast" -> {
                        breakfastCal += calories
                        tvBreakfastCal.text = "${breakfastCal.toInt()} / 500 ккал"
                    }
                    "lunch" -> {
                        lunchCal += calories
                        tvLunchCal.text = "${lunchCal.toInt()} / 700 ккал"
                    }
                    "dinner" -> {
                        dinnerCal += calories
                        tvDinnerCal.text = "${dinnerCal.toInt()} / 550 ккал"
                    }
                }

                consumedCal += calories
                consumedProt += prot
                consumedFat += fat
                consumedCarb += carb

                updateUI(txtCal, circleProg, barProt, barFat, barCarb)
            }
        }

        btnBreakfast.setOnClickListener { startSearch("breakfast") }
        btnLunch.setOnClickListener { startSearch("lunch") }
        btnDinner.setOnClickListener { startSearch("dinner") }

        btnDiary.setOnClickListener {
            val intent = Intent(this, DiaryActivity::class.java)
            intent.putExtra("DIARY_ENTRIES", ArrayList(diaryEntries))
            startActivity(intent)
        }

        btnProfile.setOnClickListener { Toast.makeText(this, "Профиль (в разработке)", Toast.LENGTH_SHORT).show() }

        updateUI(txtCal, circleProg, barProt, barFat, barCarb)
    }

    private fun startSearch(type: String) {
        val intent = Intent(this, SearchActivity::class.java)
        intent.putExtra("MEAL_TYPE", type)
        searchLauncher.launch(intent)
    }

    private fun updateUI(txtCal: TextView, circle: ProgressBar, p: ProgressBar, f: ProgressBar, c: ProgressBar) {
        txtCal.text = "${consumedCal.toInt()} / ${normCal.toInt()} ккал"
        circle.progress = consumedCal.toInt()
        p.progress = consumedProt.toInt()
        f.progress = consumedFat.toInt()
        c.progress = consumedCarb.toInt()
    }
}