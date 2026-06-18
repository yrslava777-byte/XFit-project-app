package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.RadioGroup
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.content.ContextCompat
import com.example.xfitapplication.R
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.domain.usecase.ValidateAgeUseCase
import com.example.xfitapplication.domain.usecase.ValidateBodyWeightUseCase
import com.example.xfitapplication.domain.usecase.ValidateTargetWeightUseCase
import com.example.xfitapplication.presentation.util.clearErrorOnInput
import com.example.xfitapplication.presentation.util.IntFieldSpec
import com.example.xfitapplication.presentation.util.setFieldError
import com.example.xfitapplication.presentation.util.showFormError
import com.example.xfitapplication.presentation.util.validateIntFields
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

    private val validateBodyWeight = ValidateBodyWeightUseCase()
    private val validateAge = ValidateAgeUseCase()
    private val validateTargetWeight = ValidateTargetWeightUseCase()

    override fun onCreate(savedInstanceState: Bundle?) {
        installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        etTargetWeight = findViewById(R.id.etTargetWeight)
        etAge = findViewById(R.id.etAge)
        rgGender = findViewById(R.id.rgGender)
        containerActivity = findViewById(R.id.containerActivity)

        listOf(etHeight, etWeight, etTargetWeight, etAge).forEach {
            it.clearErrorOnInput()
        }

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
        val validation = validateIntFields(
            this,
            listOf(
                IntFieldSpec(etHeight, "Рост"),
                IntFieldSpec(etWeight, "Текущий вес") { value ->
                    validateBodyWeight.execute(value).message
                },
                IntFieldSpec(etTargetWeight, "Целевой вес") { value ->
                    validateBodyWeight.execute(value).message
                },
                IntFieldSpec(etAge, "Возраст") { value ->
                    validateAge.execute(value).message
                }
            )
        )
        if (validation.hasErrors) return

        val height = validation.values[0]!!
        val weight = validation.values[1]!!
        val targetWeight = validation.values[2]!!
        val age = validation.values[3]!!

        val targetValidation = validateTargetWeight.execute(height.toDouble(), targetWeight.toDouble())
        if (!targetValidation.isValid) {
            etTargetWeight.setFieldError(targetValidation.message)
            showFormError(this, targetValidation.message ?: "Некорректный целевой вес")
            return
        }

        val gender = if (rgGender.checkedRadioButtonId == R.id.rbMale) "male" else "female"

        viewModel.calculateAndSave(
            height, weight, targetWeight, age, gender, selectedActivityLevel
        ) {
            openDashboard()
        }
    }

    private fun openDashboard() {
        startActivity(Intent(this, DashboardActivity::class.java))
        finish()
    }
}
