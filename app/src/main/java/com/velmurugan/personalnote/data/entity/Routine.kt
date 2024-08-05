package com.velmurugan.personalnote.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.velmurugan.personalnote.utils.DateUtil

@Entity
data class Routine(
    @PrimaryKey(autoGenerate = true)
    val routineId: Int = 0,
    val routineName: String,
    val date: String,
    val isCompleted: Boolean
)