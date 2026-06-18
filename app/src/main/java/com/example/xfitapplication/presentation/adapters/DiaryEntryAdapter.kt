package com.example.xfitapplication.presentation.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageButton
import android.widget.TextView
import com.example.xfitapplication.R
import com.example.xfitapplication.domain.model.FoodEntry
import java.util.Locale

class DiaryEntryAdapter(
    private val onDelete: (FoodEntry) -> Unit
) : BaseAdapter() {

    private val entries = mutableListOf<FoodEntry>()

    fun submitList(list: List<FoodEntry>) {
        entries.clear()
        entries.addAll(list)
        notifyDataSetChanged()
    }

    override fun getCount(): Int = entries.size

    override fun getItem(position: Int): FoodEntry = entries[position]

    override fun getItemId(position: Int): Long = entries[position].id.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(parent.context)
            .inflate(R.layout.item_diary_entry, parent, false)

        val entry = getItem(position)
        view.findViewById<TextView>(R.id.tvProductName).text = entry.productName
        view.findViewById<TextView>(R.id.tvWeight).text = "${entry.weightGrams.toInt()} г"
        view.findViewById<TextView>(R.id.tvCalories).text = "${entry.caloriesTotal.toInt()} ккал"
        view.findViewById<TextView>(R.id.tvMacros).text = String.format(
            Locale.getDefault(),
            "Б:%.0fг Ж:%.0fг У:%.0fг",
            entry.proteinTotal,
            entry.fatTotal,
            entry.carbsTotal
        )
        view.findViewById<ImageButton>(R.id.btnDelete).setOnClickListener {
            onDelete(entry)
        }
        return view
    }
}
