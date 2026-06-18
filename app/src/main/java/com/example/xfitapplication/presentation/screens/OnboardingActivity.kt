package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.util.parseStrictInt
import com.example.xfitapplication.presentation.viewmodel.OnboardingViewModel
import com.google.android.material.card.MaterialCardView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText

class OnboardingActivity : AppCompatActivity() {

    private val viewModel: OnboardingViewModel by viewModels {
        ViewModelFactory { OnboardingViewModel(application) }
    }

    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etTargetWeight: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var rgGender: RadioGroup
    private lateinit var containerActivity: LinearLayout

    private val activityCards = mutableListOf<MaterialCardView>()
    private var selectedActivityLevel = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        etTargetWeight = findViewById(R.id.etTargetWeight)
        etAge = findViewById(R.id.etAge)
        rgGender = findViewById(R.id.rgGender)
        containerActivity = findViewById(R.id.containerActivity)

        setupActivityLevels()

        viewModel.navigateToDashboard.observe(this) { go ->
            if (go) openDashboard()
        }

        viewModel.error.observe(this) { event ->
            event?.getContentIfNotHandled()?.let { message ->
                Toast.makeText(this, message, Toast.LENGTH_LONG).show()
            }
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnCalculate)
            .setOnClickListener { calculate() }
    }

    private fun setupActivityLevels() {
        val titles = resources.getStringArray(R.array.activity_titles)
        val descriptions = resources.getStringArray(R.array.activity_descriptions)
        val inflater = LayoutInflater.from(this)

        titles.forEachIndexed { index, title ->
            val level = index + 1
            val card = inflater.inflate(R.layout.item_activity_level, containerActivity, false) as MaterialCardView
            card.findViewById<android.widget.TextView>(R.id.tvActivityTitle).text = title
            card.findViewById<android.widget.TextView>(R.id.tvActivityDesc).text = descriptions[index]

            val radio = card.findViewById<MaterialRadioButton>(R.id.rbActivity)
            card.setOnClickListener { selectActivityLevel(level) }
            radio.setOnClickListener { selectActivityLevel(level) }

            activityCards.add(card)
            containerActivity.addView(card)
        }

        selectActivityLevel(selectedActivityLevel)
    }

    private fun selectActivityLevel(level: Int) {
        selectedActivityLevel = level
        val strokeSelected = resources.getDimensionPixelSize(R.dimen.activity_card_stroke_selected)
        val strokeDefault = resources.getDimensionPixelSize(R.dimen.activity_card_stroke_default)
        val colorSelected = ContextCompat.getColor(this, R.color.primary)
        val colorDefault = ContextCompat.getColor(this, R.color.outline)

        activityCards.forEachIndexed { index, card ->
            val isSelected = index + 1 == level
            card.strokeWidth = if (isSelected) strokeSelected else strokeDefault
            card.strokeColor = if (isSelected) colorSelected else colorDefault
            card.findViewById<MaterialRadioButton>(R.id.rbActivity).isChecked = isSelected
        }
    }

    private fun calculate() {
        val height = readInt(etHeight, "Рост") ?: return
        val weight = readInt(etWeight, "Текущий вес") ?: return
        val targetWeight = readInt(etTargetWeight, "Целевой вес") ?: return
        val age = readInt(etAge, "Возраст") ?: return

        val gender = if (rgGender.checkedRadioButtonId == R.id.rbMale) "male" else "female"

        viewModel.calculateAndSave(
            height, weight, targetWeight, age, gender, selectedActivityLevel
        ) {
            openDashboard()
        }
    }

    private fun readInt(field: TextInputEditText, label: String): Int? {
        val value = field.text?.toString()?.parseStrictInt()
        if (value == null) {
            field.error = "Только целое число"
            Toast.makeText(this, "Поле «$label»: введите целое число", Toast.LENGTH_SHORT).show()
        }
        return value
    }

    private fun openDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
