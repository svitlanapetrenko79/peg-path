package com.peggiy.pather.lather.presentation.gameover

import androidx.lifecycle.ViewModel
import com.peggiy.pather.lather.domain.repository.ProgressRepository
import com.peggiy.pather.lather.domain.usecase.SparksFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GameOverUiState(
    val roundScoreText: String = "",
    val sparksWonText: String = "",
    val bestMultiplierText: String = "",
    val bestMultiplier: Float = 0f,
    val dropsUsed: Int = 0,
    val balanceText: String = "",
    val level: Int = 1,
    val newBest: Boolean = false,
    val rewards: List<Int> = emptyList()
)

class GameOverViewModel(
    private val progressRepository: ProgressRepository
) : ViewModel() {

    private val _state = MutableStateFlow(GameOverUiState())
    val state: StateFlow<GameOverUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.progress()
        val round = progressRepository.lastRound()
        _state.update {
            it.copy(
                roundScoreText = SparksFormatter.grouped(round.roundScore),
                sparksWonText = SparksFormatter.grouped(round.sparksWon),
                bestMultiplierText = SparksFormatter.multiplier(round.bestMultiplier),
                bestMultiplier = round.bestMultiplier,
                dropsUsed = round.dropsUsed,
                balanceText = SparksFormatter.grouped(progress.balance),
                level = progress.level,
                newBest = round.newBest,
                rewards = round.rewards
            )
        }
    }
}
