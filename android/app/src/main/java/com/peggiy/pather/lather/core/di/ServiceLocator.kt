package com.peggiy.pather.lather.core.di

import android.content.Context
import com.peggiy.pather.lather.data.local.PegPathPrefs
import com.peggiy.pather.lather.data.repository.ProgressRepositoryImpl
import com.peggiy.pather.lather.data.repository.SettingsRepositoryImpl
import com.peggiy.pather.lather.domain.repository.ProgressRepository
import com.peggiy.pather.lather.domain.repository.SettingsRepository
import com.peggiy.pather.lather.domain.usecase.DailyBonusUseCase
import com.peggiy.pather.lather.domain.usecase.DropEngine
import com.peggiy.pather.lather.domain.usecase.ScoreCalculator

object ServiceLocator {

    private var progress: ProgressRepository? = null
    private var settings: SettingsRepository? = null

    val dropEngine = DropEngine()
    val scoreCalculator = ScoreCalculator()
    val dailyBonus = DailyBonusUseCase()

    fun init(context: Context) {
        if (progress != null) return
        val prefs = PegPathPrefs(context)
        progress = ProgressRepositoryImpl(prefs)
        settings = SettingsRepositoryImpl(prefs)
    }

    fun progressRepository(): ProgressRepository =
        requireNotNull(progress) { "ServiceLocator.init was not called" }

    fun settingsRepository(): SettingsRepository =
        requireNotNull(settings) { "ServiceLocator.init was not called" }
}
