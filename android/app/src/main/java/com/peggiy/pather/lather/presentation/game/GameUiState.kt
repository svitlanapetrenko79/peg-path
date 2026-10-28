package com.peggiy.pather.lather.presentation.game

import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.domain.model.Phase

data class GameUiState(
    val phase: Phase = Phase.IDLE,
    val balance: Int = 0,
    val balanceText: String = "",
    val roundScore: Int = 0,
    val roundScoreText: String = "",
    val bet: Int = GameConfig.BETS[1],
    val dropsUsed: Int = 0,
    val tokenColumn: Float = 6f,
    val tokenRow: Float = 0f,
    val tokenVisible: Boolean = false,
    val hitRow: Int = -1,
    val landedSlots: List<Int> = emptyList(),
    val activeSlot: Int = -1,
    val selectedToken: Int = 1,
    val reducedMotion: Boolean = false,
    val pauseVisible: Boolean = false,
    val quitVisible: Boolean = false
) {
    val canDrop: Boolean
        get() = phase == Phase.IDLE &&
            dropsUsed < GameConfig.ROUND_DROPS &&
            balance >= bet

    val controlsEnabled: Boolean
        get() = phase == Phase.IDLE
}
