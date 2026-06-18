package com.example.xfitapplication.presentation.util

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

object DateUtils {
    private val dbFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
    private val displayFormat = SimpleDateFormat("dd MMMM yyyy", Locale("ru"))

    fun todayDb(): String = dbFormat.format(Date())

    fun todayDisplay(): String = "Сегодня, ${displayFormat.format(Date())}"
}
