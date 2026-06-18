package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.RadioGroup
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.viewmodel.OnboardingViewModel
import com.google.android.material.textfield.TextInputEditText
import com.example.xfitapplication.MainActivity

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewModel: OnboardingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewModel = ViewModelProvider(this).get(OnboardingViewModel::class.java)

        val etHeight = findViewById<TextInputEditText>(R.id.etHeight)
        val etWeight = findViewById<TextInputEditText>(R.id.etWeight)
        val etAge = findViewById<TextInputEditText>(R.id.etAge)
        val rgGender = findViewById<RadioGroup>(R.id.rgGender)
        val sbActivity = findViewById<SeekBar>(R.id.sbActivity)
        val tvActivity = findViewById<TextView>(R.id.tvActivity)
        val btnCalculate = findViewById<Button>(R.id.btnCalculate)

        sbActivity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvActivity.text = "Активность: $progress"
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })

        btnCalculate.setOnClickListener {
            val height = etHeight.text.toString().toDoubleOrNull()
            val weight = etWeight.text.toString().toDoubleOrNull()
            val age = etAge.text.toString().toIntOrNull()
            val gender = if (rgGender.checkedRadioButtonId == R.id.rbMale) "male" else "female"
            val activity = sbActivity.progress

            if (height == null || weight == null || age == null) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.calculateAndSave(height, weight, age, gender, activity) {
                Toast.makeText(this, "Норма рассчитана и сохранена!", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }
}