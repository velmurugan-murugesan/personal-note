package com.velmurugan.personalnote.data.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["itemName"], unique = true)])
data class Shopping(
    @PrimaryKey(autoGenerate = true) val itemId: Int = 0,
    val itemName: String,
    val isPurchased: Boolean = false
)