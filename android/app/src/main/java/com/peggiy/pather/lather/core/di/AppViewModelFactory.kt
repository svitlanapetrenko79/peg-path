package com.peggiy.pather.lather.core.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.peggiy.pather.lather.presentation.app.AppViewModel
import com.peggiy.pather.lather.presentation.game.GameViewModel
import com.peggiy.pather.lather.presentation.gameover.GameOverViewModel
import com.peggiy.pather.lather.presentation.menu.MenuViewModel
import com.peggiy.pather.lather.presentation.splash.SplashViewModel

object AppViewModelFactory : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val created: ViewModel = when {
            modelClass.isAssignableFrom(AppViewModel::class.java) -> AppViewModel()
            modelClass.isAssignableFrom(SplashViewModel::class.java) -> SplashViewModel()
            modelClass.isAssignableFrom(MenuViewModel::class.java) -> MenuViewModel(
                ServiceLocator.progressRepository(),
                ServiceLocator.settingsRepository(),
                ServiceLocator.dailyBonus
            )
            modelClass.isAssignableFrom(GameViewModel::class.java) -> GameViewModel(
                ServiceLocator.progressRepository(),
                ServiceLocator.settingsRepository(),
                ServiceLocator.dropEngine,
                ServiceLocator.scoreCalculator
            )
            modelClass.isAssignableFrom(GameOverViewModel::class.java) -> GameOverViewModel(
                ServiceLocator.progressRepository()
            )
            else -> throw IllegalArgumentException(modelClass.name)
        }
        return created as T
    }
}
