package com.peggiy.pather.lather.data.repository

import com.peggiy.pather.lather.data.local.PegPathPrefs
import com.peggiy.pather.lather.domain.model.AppSettings
import com.peggiy.pather.lather.domain.repository.SettingsRepository

class SettingsRepositoryImpl(private val prefs: PegPathPrefs) : SettingsRepository {

    override fun settings(): AppSettings = AppSettings(
        soundOn = prefs.bool(PegPathPrefs.SOUND, true),
        hapticsOn = prefs.bool(PegPathPrefs.HAPTICS, true),
        reducedMotion = prefs.bool(PegPathPrefs.REDUCED_MOTION, false)
    )

    override fun setSound(enabled: Boolean) = prefs.putBool(PegPathPrefs.SOUND, enabled)

    override fun setHaptics(enabled: Boolean) = prefs.putBool(PegPathPrefs.HAPTICS, enabled)

    override fun setReducedMotion(enabled: Boolean) = prefs.putBool(PegPathPrefs.REDUCED_MOTION, enabled)
}
