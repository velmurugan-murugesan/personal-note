package com.velmurugan.personalnote.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Task(
    @PrimaryKey(autoGenerate = true) val taskId: Int = 0,
    val taskName: String,
    val priority: String,
    val isCompleted: Boolean = false
)
