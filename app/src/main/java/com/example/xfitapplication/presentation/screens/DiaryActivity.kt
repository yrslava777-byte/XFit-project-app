package com.example.xfitapplication.presentation.screens

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.xfitapplication.R
import java.text.SimpleDateFormat
import java.util.*

class DiaryActivity : AppCompatActivity() {

    private val diaryEntries = mutableListOf<DiaryEntry>()
    private lateinit var adapter: ArrayAdapter<String>
    private lateinit var displayItems: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)

        val btnBack = findViewById<Button>(R.id.btnBack)
        btnBack.setOnClickListener {
            finish()
        }

        val tvDate = findViewById<TextView>(R.id.tvDate)
        val tvTotalCal = findViewById<TextView>(R.id.tvTotalCal)
        val tvTotalProt = findViewById<TextView>(R.id.tvTotalProt)
        val tvTotalFat = findViewById<TextView>(R.id.tvTotalFat)
        val tvTotalCarb = findViewById<TextView>(R.id.tvTotalCarb)
        val lvDiaryItems = findViewById<ListView>(R.id.lvDiaryItems)
        val btnClearDiary = findViewById<Button>(R.id.btnClearDiary)

        val dateFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))
        tvDate.text = "Сегодня, ${dateFormat.format(Date())}"

        @Suppress("UNCHECKED_CAST")
        val entries = intent.getSerializableExtra("DIARY_ENTRIES") as? ArrayList<DiaryEntry>

        if (entries != null) {
            diaryEntries.clear()
            diaryEntries.addAll(entries)
        }

        displayItems = diaryEntries.map { entry ->
            "${entry.productName} - ${entry.weight.toInt()}г (${entry.calories.toInt()} ккал)"
        }.toMutableList()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, displayItems)
        lvDiaryItems.adapter = adapter

        updateTotals(tvTotalCal, tvTotalProt, tvTotalFat, tvTotalCarb)

        lvDiaryItems.setOnItemLongClickListener { _, _, position, _ ->
            AlertDialog.Builder(this)
                .setTitle("Удалить продукт")
                .setMessage("Удалить \"${diaryEntries[position].productName}\" из дневника?")
                .setPositiveButton("Удалить") { _, _ ->
                    diaryEntries.removeAt(position)
                    displayItems.removeAt(position)
                    adapter.notifyDataSetChanged()
                    updateTotals(tvTotalCal, tvTotalProt, tvTotalFat, tvTotalCarb)
                    Toast.makeText(this, "Продукт удалён", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена", null)
                .show()
            true
        }

        btnClearDiary.setOnClickListener {
            if (diaryEntries.isEmpty()) {
                Toast.makeText(this, "Дневник уже пуст", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            AlertDialog.Builder(this)
                .setTitle("Очистить дневник")
                .setMessage("Удалить все продукты из дневника?")
                .setPositiveButton("Очистить") { _, _ ->
                    diaryEntries.clear()
                    displayItems.clear()
                    adapter.notifyDataSetChanged()
                    updateTotals(tvTotalCal, tvTotalProt, tvTotalFat, tvTotalCarb)
                    Toast.makeText(this, "Дневник очищен", Toast.LENGTH_SHORT).show()
                }
                .setNegativeButton("Отмена", null)
                .show()
        }
    }

    private fun updateTotals(
        tvCal: TextView, tvProt: TextView, tvFat: TextView, tvCarb: TextView
    ) {
        val totalCal = diaryEntries.sumOf { it.calories }
        val totalProt = diaryEntries.sumOf { it.protein }
        val totalFat = diaryEntries.sumOf { it.fat }
        val totalCarb = diaryEntries.sumOf { it.carbs }

        tvCal.text = "${totalCal.toInt()} ккал"
        tvProt.text = "${totalProt.toInt()} г"
        tvFat.text = "${totalFat.toInt()} г"
        tvCarb.text = "${totalCarb.toInt()} г"
    }
}