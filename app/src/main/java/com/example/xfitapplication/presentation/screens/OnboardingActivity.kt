package com.example.xfitapplication.presentation.screens

import com.example.xfitapplication.MainActivity
import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.viewmodel.OnboardingViewModel
import com.google.android.material.textfield.TextInputEditText

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewModel: OnboardingViewModel

    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var rgGender: RadioGroup
    private lateinit var sbActivity: SeekBar
    private lateinit var tvActivity: TextView
    private lateinit var btnCalculate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        // Инициализация ViewModel
        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java)

        // Инициализация View
        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        etAge = findViewById(R.id.etAge)
        rgGender = findViewById(R.id.rgGender)
        sbActivity = findViewById(R.id.sbActivity)
        tvActivity = findViewById(R.id.tvActivity)
        btnCalculate = findViewById(R.id.btnCalculate)

        // Логика SeekBar
        sbActivity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvActivity.text = getString(R.string.activity_level, progress + 1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        // Логика кнопки
        btnCalculate.setOnClickListener {
            val heightText = etHeight.text.toString()
            val weightText = etWeight.text.toString()
            val ageText = etAge.text.toString()

            if (heightText.isEmpty() || weightText.isEmpty() || ageText.isEmpty()) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val height = heightText.toDoubleOrNull()
            val weight = weightText.toDoubleOrNull()
            val age = ageText.toIntOrNull()

            if (height == null || weight == null || age == null) {
                Toast.makeText(this, "Некорректные данные", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = if (rgGender.checkedRadioButtonId == R.id.rbMale) "male" else "female"
            val activity = sbActivity.progress + 1

            // Вызов ViewModel
            viewModel.calculateAndSave(
                height = height,
                weight = weight,
                age = age,
                gender = gender,
                activityLevel = activity,
                onSuccess = { user ->
                    val intent = Intent(this, DashboardActivity::class.java)
                    intent.putExtra("CALORIES", user.dailyCalories)
                    intent.putExtra("PROTEIN", user.dailyProtein)
                    intent.putExtra("FAT", user.dailyFat)
                    intent.putExtra("CARBS", user.dailyCarbs)

                    startActivity(intent)
                    finish()
                }
            )
        }
    }
}