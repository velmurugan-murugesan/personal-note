package com.velmurugan.personalnote.di

import android.content.Context
import androidx.room.Room
import com.velmurugan.personalnote.data.AppDatabase
import com.velmurugan.personalnote.data.dao.RoutineDao
import com.velmurugan.personalnote.data.dao.SettingsDao
import com.velmurugan.personalnote.data.dao.ShoppingDao
import com.velmurugan.personalnote.data.dao.TaskDao
import com.velmurugan.personalnote.data.repository.RoutineRepository
import com.velmurugan.personalnote.data.repository.SettingsRepository
import com.velmurugan.personalnote.data.repository.ShoppingRepository
import com.velmurugan.personalnote.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext: Context): AppDatabase {
        return Room.databaseBuilder(
            appContext,
            AppDatabase::class.java,
            "personal_note"
        ).allowMainThreadQueries()
            .build()
    }

    @Provides
    fun provideTaskDao(database: AppDatabase): TaskDao {
        return database.taskDao()
    }

    @Provides
    fun provideRoutineDao(database: AppDatabase): RoutineDao {
        return database.routineDao()
    }

    @Provides
    fun provideShoppingDao(database: AppDatabase): ShoppingDao {
        return database.shoppingDao()
    }

    @Provides
    fun provideSettingsDao(database: AppDatabase): SettingsDao {
        return database.settingsDao()
    }

    @Provides
    fun provideRoutineRepository(routineDao: RoutineDao): RoutineRepository {
        return RoutineRepository(routineDao)
    }

    @Provides
    fun provideShoppingRepository(shoppingDao: ShoppingDao): ShoppingRepository {
        return ShoppingRepository(shoppingDao)
    }

    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }

    @Provides
    fun provideSettingsRepository(settingsDao: SettingsDao): SettingsRepository {
        return SettingsRepository(settingsDao)
    }


}