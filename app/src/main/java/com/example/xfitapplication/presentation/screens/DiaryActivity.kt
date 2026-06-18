package com.example.xfitapplication.presentation.screens

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import com.example.xfitapplication.domain.model.FoodEntry
import com.example.xfitapplication.presentation.ViewModelFactory
import com.example.xfitapplication.presentation.util.DateUtils
import com.example.xfitapplication.presentation.viewmodel.DiaryViewModel

class DiaryActivity : AppCompatActivity() {

    private val viewModel: DiaryViewModel by viewModels {
        ViewModelFactory { DiaryViewModel(application) }
    }

    private val entries = mutableListOf<FoodEntry>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var displayItems: MutableList<String>

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

        displayItems = mutableListOf()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayItems)
        lvDiaryItems.adapter = adapter

        viewModel.entries.observe(this) { list ->
            entries.clear()
            entries.addAll(list)
            displayItems.clear()
            displayItems.addAll(list.map { entry ->
                "${entry.productName} - ${entry.weightGrams.toInt()}г (${entry.caloriesTotal.toInt()} ккал)"
            })
            adapter.notifyDataSetChanged()
        }

        viewModel.totals.observe(this) { totals ->
            tvTotalCal.text = "${totals.consumedCalories.toInt()} ккал"
            tvTotalProt.text = "${totals.consumedProtein.toInt()} г"
            tvTotalFat.text = "${totals.consumedFat.toInt()} г"
            tvTotalCarb.text = "${totals.consumedCarbs.toInt()} г"
        }

        lvDiaryItems.setOnItemLongClickListener { _, _, position, _ ->
            val entry = entries.getOrNull(position) ?: return@setOnItemLongClickListener true
            AlertDialog.Builder(this)
                .setTitle("Удалить продукт")
                .setMessage("Удалить \"${entry.productName}\" из дневника?")
                .setPositiveButton("Удалить") { _, _ ->
                    viewModel.deleteEntry(entry)
                    Toast.makeText(this, "Продукт удалён", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена", null)
                .show()
            true
        }

        findViewById<com.google.android.material.button.MaterialButton>(R.id.btnClearDiary)
            .setOnClickListener {
                if (entries.isEmpty()) {
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
}
