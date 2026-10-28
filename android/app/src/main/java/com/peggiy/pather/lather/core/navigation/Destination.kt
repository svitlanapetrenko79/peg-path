package com.peggiy.pather.lather.core.navigation

sealed interface Screen {
    data object Splash : Screen
    data object Menu : Screen
    data object Game : Screen
    data class GameOver(val roundScore: Int, val bestSlot: Int, val newBest: Boolean) : Screen
}
