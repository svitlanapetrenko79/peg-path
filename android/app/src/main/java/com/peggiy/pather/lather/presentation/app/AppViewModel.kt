package com.peggiy.pather.lather.presentation.app

import androidx.lifecycle.ViewModel
import com.peggiy.pather.lather.core.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel : ViewModel() {

    private val _screen = MutableStateFlow<Screen>(Screen.Splash)
    val screen: StateFlow<Screen> = _screen.asStateFlow()

    private val _backStack = MutableStateFlow<List<Screen>>(emptyList())
    val backStack: StateFlow<List<Screen>> = _backStack.asStateFlow()

    private val _roundToken = MutableStateFlow(0)
    val roundToken: StateFlow<Int> = _roundToken.asStateFlow()

    fun toMenu() {
        _backStack.value = emptyList()
        _screen.value = Screen.Menu
    }

    fun toGame() {
        _roundToken.value = _roundToken.value + 1
        _backStack.value = listOf(Screen.Menu)
        _screen.value = Screen.Game
    }

    fun toGameOver(roundScore: Int, bestSlot: Int, newBest: Boolean) {
        _backStack.value = listOf(Screen.Menu)
        _screen.value = Screen.GameOver(roundScore, bestSlot, newBest)
    }

    fun back(): Boolean {
        val stack = _backStack.value
        if (stack.isEmpty()) return false
        _screen.value = stack.last()
        _backStack.value = stack.dropLast(1)
        return true
    }
}
