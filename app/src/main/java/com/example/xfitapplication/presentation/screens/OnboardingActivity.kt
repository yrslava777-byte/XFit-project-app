package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.SeekBar
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.viewmodel.OnboardingViewModel
import com.google.android.material.textfield.TextInputEditText

class OnboardingActivity : AppCompatActivity() {

    private val viewModel: OnboardingViewModel by viewModels {
        ViewModelFactory { OnboardingViewModel(application) }
    }

    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var rgGender: android.widget.RadioGroup
    private lateinit var sbActivity: SeekBar
    private lateinit var tvActivity: android.widget.TextView
    private lateinit var btnCalculate: com.google.android.material.button.MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        etAge = findViewById(R.id.etAge)
        rgGender = findViewById(R.id.rgGender)
        sbActivity = findViewById(R.id.sbActivity)
        tvActivity = findViewById(R.id.tvActivity)
        btnCalculate = findViewById(R.id.btnCalculate)

        viewModel.navigateToDashboard.observe(this) { go ->
            if (go) openDashboard()
        }

        sbActivity.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                tvActivity.text = getString(R.string.activity_level, progress + 1)
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {}
            override fun onStopTrackingTouch(seekBar: SeekBar?) {}
        })
        tvActivity.text = getString(R.string.activity_level, sbActivity.progress + 1)

        btnCalculate.setOnClickListener {
            val height = etHeight.text.toString().toDoubleOrNull()
            val weight = etWeight.text.toString().toDoubleOrNull()
            val age = etAge.text.toString().toIntOrNull()

            if (height == null || weight == null || age == null) {
                Toast.makeText(this, "Заполните все поля", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val gender = if (rgGender.checkedRadioButtonId == R.id.rbMale) "male" else "female"
            val activity = sbActivity.progress + 1

            viewModel.calculateAndSave(height, weight, age, gender, activity) {
                openDashboard()
            }
        }
    }

    private fun openDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
