package com.velmurugan.personalnote.utils

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.velmurugan.personalnote.base.PRIORITY_HIGH_COLOR
import com.velmurugan.personalnote.base.PRIORITY_LOW_COLOR
import com.velmurugan.personalnote.base.PRIORITY_MEDIUM_COLOR


@Composable
fun getPriorityColor(priority: String): Color {
    return when (priority) {
        "HIGH" -> PRIORITY_HIGH_COLOR
        "MEDIUM" -> PRIORITY_MEDIUM_COLOR
        "LOW" -> PRIORITY_LOW_COLOR
        else -> MaterialTheme.colorScheme.primary
    }
}

enum class Priority(val displayName: String) {
    HIGH("High"), MEDIUM("Medium"), LOW("Low");

    fun getPriorityColor(): Color {
        return when (this) {
            HIGH -> PRIORITY_HIGH_COLOR
            MEDIUM -> PRIORITY_MEDIUM_COLOR
            LOW -> PRIORITY_LOW_COLOR
        }
    }
}