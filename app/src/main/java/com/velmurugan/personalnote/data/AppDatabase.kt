package com.velmurugan.personalnote.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.velmurugan.personalnote.data.dao.RoutineDao
import com.velmurugan.personalnote.data.dao.SettingsDao
import com.velmurugan.personalnote.data.dao.ShoppingDao
import com.velmurugan.personalnote.data.dao.TaskDao
import com.velmurugan.personalnote.data.entity.Routine
import com.velmurugan.personalnote.data.entity.Settings
import com.velmurugan.personalnote.data.entity.Shopping
import com.velmurugan.personalnote.data.entity.Task

@Database(
    entities = [Task::class, Routine::class, Shopping::class, Settings::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao
    abstract fun routineDao(): RoutineDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun settingsDao(): SettingsDao
}