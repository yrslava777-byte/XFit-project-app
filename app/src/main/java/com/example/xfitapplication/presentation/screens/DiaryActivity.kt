package com.example.xfitapplication.presentation.screens

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.adapters.DiaryEntryAdapter
import com.example.xfitapplication.presentation.util.DateUtils
import com.example.xfitapplication.presentation.viewmodel.DiaryViewModel

class DiaryActivity : AppCompatActivity() {

    private val viewModel: DiaryViewModel by viewModels {
        ViewModelFactory { DiaryViewModel(application) }
    }

    private lateinit var adapter: DiaryEntryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnBack)
            .setOnClickListener { finish() }

        findViewById<TextView>(R.id.tvDate).text = DateUtils.todayDisplay()

        val tvTotalCal = findViewById<TextView>(R.id.tvTotalCal)
        val tvTotalProt = findViewById<TextView>(R.id.tvTotalProt)
        val tvTotalFat = findViewById<TextView>(R.id.tvTotalFat)
        val tvTotalCarb = findViewById<TextView>(R.id.tvTotalCarb)
        val lvDiaryItems = findViewById<ListView>(R.id.lvDiaryItems)

        adapter = DiaryEntryAdapter { entry -> confirmDeleteEntry(entry) }
        lvDiaryItems.adapter = adapter

        viewModel.entries.observe(this) { list ->
            adapter.submitList(list)
        }

        viewModel.totals.observe(this) { totals ->
            tvTotalCal.text = "${totals.consumedCalories.toInt()} ккал"
            tvTotalProt.text = "${totals.consumedProtein.toInt()} г"
            tvTotalFat.text = "${totals.consumedFat.toInt()} г"
            tvTotalCarb.text = "${totals.consumedCarbs.toInt()} г"
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnClearDiary)
            .setOnClickListener {
                if (adapter.isEmpty) {
                    Toast.makeText(this, "Дневник уже пуст", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
                AlertDialog.Builder(this)
                    .setTitle("Очистить дневник")
                    .setMessage("Удалить все продукты из дневника?")
                    .setPositiveButton("Очистить") { _, _ ->
                        viewModel.clearAll()
                        Toast.makeText(this, "Дневник очищен", Toast.LENGTH_SHORT).show()
                    }
                    .setNegativeButton("Отмена", null)
                    .show()
            }
    }

    private fun confirmDeleteEntry(entry: FoodEntry) {
        AlertDialog.Builder(this)
            .setTitle("Удалить запись")
            .setMessage("Удалить \"${entry.productName}\" из дневника?")
            .setPositiveButton("Удалить") { _, _ ->
                viewModel.deleteEntry(entry)
                Toast.makeText(this, "Запись удалена", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Отмена", null)
            .show()
    }
}
