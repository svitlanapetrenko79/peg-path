package com.peggiy.pather.lather.domain.repository

import com.peggiy.pather.lather.domain.model.AppSettings

interface SettingsRepository {
    fun settings(): AppSettings
    fun setSound(enabled: Boolean)
    fun setHaptics(enabled: Boolean)
    fun setReducedMotion(enabled: Boolean)
}
