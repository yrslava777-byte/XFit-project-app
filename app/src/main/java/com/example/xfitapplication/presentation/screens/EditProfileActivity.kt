package com.example.xfitapplication.presentation.screens

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.domain.model.User
import com.example.xfitapplication.domain.usecase.CalculateDailyNormUseCase
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.domain.usecase.ValidateAgeUseCase
import com.example.xfitapplication.domain.usecase.ValidateBodyWeightUseCase
import com.example.xfitapplication.domain.usecase.ValidateCaloriesUseCase
import com.example.xfitapplication.domain.usecase.ValidateMacroUseCase
import com.example.xfitapplication.domain.usecase.ValidateTargetWeightUseCase
import com.example.xfitapplication.presentation.util.applyIntFieldErrors
import com.example.xfitapplication.presentation.util.clearErrorOnInput
import com.example.xfitapplication.presentation.util.IntFieldSpec
import com.example.xfitapplication.presentation.util.setFieldError
import com.example.xfitapplication.presentation.util.showFormError
import com.example.xfitapplication.presentation.util.validateIntFields
import com.example.xfitapplication.presentation.viewmodel.EditProfileViewModel
import com.google.android.material.textfield.TextInputEditText
import kotlin.math.roundToInt

class EditProfileActivity : AppCompatActivity() {

    private val viewModel: EditProfileViewModel by viewModels {
        ViewModelFactory { EditProfileViewModel(application) }
    }

    private var isFormLoaded = false
    private var currentUser: User? = null
    private var isUpdatingNutrition = false

    private val calculateDailyNorm = CalculateDailyNormUseCase()
    private val validateBodyWeight = ValidateBodyWeightUseCase()
    private val validateAge = ValidateAgeUseCase()
    private val validateCalories = ValidateCaloriesUseCase()
    private val validateMacro = ValidateMacroUseCase()
    private val validateTargetWeight = ValidateTargetWeightUseCase()

    private lateinit var etHeight: TextInputEditText
    private lateinit var etWeight: TextInputEditText
    private lateinit var etAge: TextInputEditText
    private lateinit var etTargetWeight: TextInputEditText
    private lateinit var etCalories: TextInputEditText
    private lateinit var etProtein: TextInputEditText
    private lateinit var etFat: TextInputEditText
    private lateinit var etCarbs: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        etHeight = findViewById(R.id.etHeight)
        etWeight = findViewById(R.id.etWeight)
        etAge = findViewById(R.id.etAge)
        etTargetWeight = findViewById(R.id.etTargetWeight)
        etCalories = findViewById(R.id.etCalories)
        etProtein = findViewById(R.id.etProtein)
        etFat = findViewById(R.id.etFat)
        etCarbs = findViewById(R.id.etCarbs)

        listOf(
            etHeight, etWeight, etAge, etTargetWeight,
            etCalories, etProtein, etFat, etCarbs
        ).forEach { it.clearErrorOnInput() }
        attachRecalculationWatchers()

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBack)
            .setOnClickListener { finish() }

        viewModel.user.observe(this) { user ->
            if (user == null || isFormLoaded) return@observe
            currentUser = user
            isFormLoaded = true
            fillForm(
                user.heightCm.roundToInt(),
                user.weightKg.roundToInt(),
                user.ageYears,
                (user.targetWeightKg ?: user.weightKg).roundToInt(),
                user.dailyCalories.roundToInt(),
                user.dailyProtein.roundToInt(),
                user.dailyFat.roundToInt(),
                user.dailyCarbs.roundToInt()
            )
        }

        viewModel.saveResult.observe(this) { event ->
            when (val result = event?.getContentIfNotHandled()) {
                is EditProfileViewModel.SaveResult.Success -> {
                    Toast.makeText(this, "Изменения сохранены", Toast.LENGTH_SHORT).show()
                    setResult(RESULT_OK)
                    finish()
                }
                is EditProfileViewModel.SaveResult.Error -> {
                    showFieldErrors()
                    Toast.makeText(this, result.message, Toast.LENGTH_LONG).show()
                }
                null -> Unit
            }
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnSave)
            .setOnClickListener {
                readForm()?.let { viewModel.save(it) }
            }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnReset)
            .setOnClickListener {
                viewModel.user.value?.let { user ->
                    fillForm(
                        user.heightCm.roundToInt(),
                        user.weightKg.roundToInt(),
                        user.ageYears,
                        (user.targetWeightKg ?: user.weightKg).roundToInt(),
                        user.dailyCalories.roundToInt(),
                        user.dailyProtein.roundToInt(),
                        user.dailyFat.roundToInt(),
                        user.dailyCarbs.roundToInt()
                    )
                    Toast.makeText(this, "Изменения сброшены", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun fillForm(
        height: Int, weight: Int, age: Int, targetWeight: Int,
        calories: Int, protein: Int, fat: Int, carbs: Int
    ) {
        isUpdatingNutrition = true
        etHeight.setText(height.toString())
        etWeight.setText(weight.toString())
        etAge.setText(age.toString())
        etTargetWeight.setText(targetWeight.toString())
        etCalories.setText(calories.toString())
        etProtein.setText(protein.toString())
        etFat.setText(fat.toString())
        etCarbs.setText(carbs.toString())
        isUpdatingNutrition = false
    }

    private fun attachRecalculationWatchers() {
        val watcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            override fun afterTextChanged(s: Editable?) {
                maybeRecalculateNutrition()
            }
        }

        listOf(etHeight, etWeight, etAge, etTargetWeight).forEach {
            it.addTextChangedListener(watcher)
        }
    }

    private fun maybeRecalculateNutrition() {
        if (!isFormLoaded || isUpdatingNutrition) return

        val baseUser = currentUser ?: return
        val height = etHeight.text?.toString()?.toIntOrNull() ?: return
        val weight = etWeight.text?.toString()?.toIntOrNull() ?: return
        val age = etAge.text?.toString()?.toIntOrNull() ?: return
        val targetWeight = etTargetWeight.text?.toString()?.toIntOrNull() ?: return

        if (height <= 0 || height > 250) return
        if (validateBodyWeight.execute(weight).message != null) return
        if (validateAge.execute(age).message != null) return
        if (validateBodyWeight.execute(targetWeight).message != null) return
        if (!validateTargetWeight.execute(height.toDouble(), targetWeight.toDouble()).isValid) return

        val recalculated = calculateDailyNorm.execute(
            heightCm = height.toDouble(),
            weightKg = weight.toDouble(),
            targetWeightKg = targetWeight.toDouble(),
            ageYears = age,
            gender = baseUser.gender,
            activityLevel = baseUser.activityLevel
        )

        isUpdatingNutrition = true
        etCalories.setText(recalculated.dailyCalories.roundToInt().toString())
        etProtein.setText(recalculated.dailyProtein.roundToInt().toString())
        etFat.setText(recalculated.dailyFat.roundToInt().toString())
        etCarbs.setText(recalculated.dailyCarbs.roundToInt().toString())
        isUpdatingNutrition = false
    }

    private fun fieldSpecs(): List<IntFieldSpec> = listOf(
        IntFieldSpec(etHeight, "Рост") { value ->
            if (value <= 0 || value > 250) "Укажите корректный рост (см), целое число" else null
        },
        IntFieldSpec(etWeight, "Вес") { value ->
            validateBodyWeight.execute(value).message
        },
        IntFieldSpec(etAge, "Возраст") { value ->
            validateAge.execute(value).message
        },
        IntFieldSpec(etTargetWeight, "Целевой вес") { value ->
            validateBodyWeight.execute(value).message
        },
        IntFieldSpec(etCalories, "Калории") { value ->
            validateCalories.execute(value).message
        },
        IntFieldSpec(etProtein, "Белки") { value ->
            validateMacro.execute(ValidateMacroUseCase.Macro.PROTEIN, value).message
        },
        IntFieldSpec(etFat, "Жиры") { value ->
            validateMacro.execute(ValidateMacroUseCase.Macro.FAT, value).message
        },
        IntFieldSpec(etCarbs, "Углеводы") { value ->
            validateMacro.execute(ValidateMacroUseCase.Macro.CARBS, value).message
        }
    )

    private fun showFieldErrors() {
        applyIntFieldErrors(fieldSpecs())
    }

    private fun readForm(): EditProfileViewModel.ProfileForm? {
        val validation = validateIntFields(this, fieldSpecs())
        if (validation.hasErrors) return null

        val height = validation.values[0]!!
        val weight = validation.values[1]!!
        val age = validation.values[2]!!
        val targetWeight = validation.values[3]!!
        val calories = validation.values[4]!!
        val protein = validation.values[5]!!
        val fat = validation.values[6]!!
        val carbs = validation.values[7]!!

        val targetValidation = validateTargetWeight.execute(height.toDouble(), targetWeight.toDouble())
        if (!targetValidation.isValid) {
            etTargetWeight.setFieldError(targetValidation.message)
            showFormError(this, targetValidation.message ?: "Некорректный целевой вес")
            return null
        }

        return EditProfileViewModel.ProfileForm(
            height = height,
            weight = weight,
            age = age,
            targetWeight = targetWeight,
            calories = calories,
            protein = protein,
            fat = fat,
            carbs = carbs
        )
    }
}
