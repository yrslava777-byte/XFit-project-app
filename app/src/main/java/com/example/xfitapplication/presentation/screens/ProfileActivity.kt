package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.viewmodel.ProfileViewModel

class ProfileActivity : AppCompatActivity() {

    private val viewModel: ProfileViewModel by viewModels {
        ViewModelFactory { ProfileViewModel(application) }
    }

    private val editLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { /* данные обновятся через Room и Flow на Dashboard */ }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val tvHeight = findViewById<TextView>(R.id.tvHeight)
        val tvWeight = findViewById<TextView>(R.id.tvWeight)
        val tvAge = findViewById<TextView>(R.id.tvAge)
        val tvTargetWeight = findViewById<TextView>(R.id.tvTargetWeight)
        val tvCalories = findViewById<TextView>(R.id.tvCalories)
        val tvProtein = findViewById<TextView>(R.id.tvProtein)
        val tvFat = findViewById<TextView>(R.id.tvFat)
        val tvCarbs = findViewById<TextView>(R.id.tvCarbs)

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBack)
            .setOnClickListener { finish() }

        viewModel.user.observe(this) { user ->
            if (user == null) return@observe
            tvHeight.text = "${user.heightCm.toInt()} см"
            tvWeight.text = "${user.weightKg.toInt()} кг"
            tvAge.text = "${user.ageYears} лет"
            tvTargetWeight.text = "${user.targetWeightKg?.toInt() ?: user.weightKg.toInt()} кг"
            tvCalories.text = "${user.dailyCalories.toInt()} ккал"
            tvProtein.text = "${user.dailyProtein.toInt()} г"
            tvFat.text = "${user.dailyFat.toInt()} г"
            tvCarbs.text = "${user.dailyCarbs.toInt()} г"
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnEditParams)
            .setOnClickListener {
                editLauncher.launch(Intent(this, EditProfileActivity::class.java))
            }
    }
}
