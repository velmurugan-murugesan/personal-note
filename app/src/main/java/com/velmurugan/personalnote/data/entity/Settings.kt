package com.velmurugan.personalnote.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Settings (
    @PrimaryKey val id: Int = 0,
    val lastRoutineSyncDate: String = ""
)