package com.velmurugan.personalnote.utils

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.Locale

object DateUtil {

    const val DATE_FORMAT = "dd-MM-yyyy"

    fun getCurrentDate(): String {
        val currentDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return currentDate.format(formatter)
    }

    fun getPreviousDate(): String {
        val currentDate = LocalDate.now()
        val previousDate = currentDate.minusDays(1)
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return previousDate.format(formatter)
    }

    fun getLast7Days(): List<String> {
        val dateFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        val today = LocalDate.now()
        return (0..7).map { daysAgo ->
            today.minusDays(daysAgo.toLong()).format(dateFormatter)
        }.reversed()
    }

    fun String.getDay(): Int {
        try {
            val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
            val date = LocalDate.parse(this, formatter)
            return date.dayOfMonth
        } catch (e: Exception) {
            e.printStackTrace()
            return 0
        }
    }

    fun getLast7DaysWithDay(): List<Pair<String, String>> {
        val formatter = DateTimeFormatter.ofPattern(DATE_FORMAT) // Formatter for DD-MM-YYYY format
        val today = LocalDate.now()
        return (0 until 7).map { i ->
            val date = today.minusDays(i.toLong())
            val dayName = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.ENGLISH).uppercase()
            val formattedDate = date.format(formatter)
            Pair(dayName, formattedDate)
        }.reversed()
    }

}