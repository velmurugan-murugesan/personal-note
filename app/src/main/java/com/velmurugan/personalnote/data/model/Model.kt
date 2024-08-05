package com.velmurugan.personalnote.data.model

import com.velmurugan.personalnote.data.entity.Routine
import com.velmurugan.personalnote.data.entity.Shopping
import com.velmurugan.personalnote.data.entity.Task

data class TaskCount(
    val totalCount: Int,
    val completedCount: Int
)

data class DatabaseItems(
    val routines: List<Routine>,
    val tasks: List<Task>,
    val shoppingItems: List<Shopping>
)