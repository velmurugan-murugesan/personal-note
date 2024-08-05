package com.velmurugan.personalnote.data.repository

import com.velmurugan.personalnote.data.dao.SettingsDao
import com.velmurugan.personalnote.data.entity.Settings

class SettingsRepository(
    private val settingsDao: SettingsDao
) {
    suspend fun addSettings(settings: Settings) {
        settingsDao.insertSettings(settings)
    }

    fun getSettings(): Settings? {
        return settingsDao.getSettings()
    }

}