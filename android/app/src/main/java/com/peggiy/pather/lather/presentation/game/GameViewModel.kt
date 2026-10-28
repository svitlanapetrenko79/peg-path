package com.peggiy.pather.lather.presentation.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.domain.model.Phase
import com.peggiy.pather.lather.domain.model.RoundSummary
import com.peggiy.pather.lather.domain.repository.ProgressRepository
import com.peggiy.pather.lather.domain.repository.SettingsRepository
import com.peggiy.pather.lather.domain.usecase.DropEngine
import com.peggiy.pather.lather.domain.usecase.ScoreCalculator
import com.peggiy.pather.lather.domain.usecase.SparksFormatter
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.floor
import kotlin.math.max
import kotlin.random.Random

class GameViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val dropEngine: DropEngine,
    private val scoreCalculator: ScoreCalculator
) : ViewModel() {

    private val _state = MutableStateFlow(GameUiState())
    val state: StateFlow<GameUiState> = _state.asStateFlow()

    private val _events = Channel<RoundFinished>(Channel.BUFFERED)
    val events: Flow<RoundFinished> = _events.receiveAsFlow()

    private var flightJob: Job? = null
    private var autoJob: Job? = null
    private var tailJob: Job? = null

    private var activeToken = -1
    private var mountedAt = 0L
    private var roundStartedAt = 0L
    private var bestMultiplier = 0f
    private var bestSlot = 6
    private var sparksWon = 0
    private var xpGained = 0
    private var resumePhase = Phase.IDLE

    fun newRound(token: Int) {
        if (token == activeToken) return
        activeToken = token
        cancelJobs()
        val progress = progressRepository.progress()
        val settings = settingsRepository.settings()
        bestMultiplier = 0f
        bestSlot = 6
        sparksWon = 0
        xpGained = 0
        resumePhase = Phase.IDLE
        mountedAt = System.currentTimeMillis()
        roundStartedAt = mountedAt
        val bet = if (GameConfig.BETS.contains(progress.bet)) progress.bet else GameConfig.BETS[1]
        _state.value = GameUiState(
            phase = Phase.IDLE,
            balance = progress.balance,
            balanceText = SparksFormatter.grouped(progress.balance),
            roundScore = 0,
            roundScoreText = SparksFormatter.grouped(0),
            bet = bet,
            dropsUsed = 0,
            selectedToken = progress.selectedToken,
            reducedMotion = settings.reducedMotion
        )
        armAutoDrop()
    }

    fun selectBet(value: Int) {
        val current = _state.value
        if (current.phase != Phase.IDLE || value > current.balance) return
        progressRepository.saveBet(value)
        _state.update { it.copy(bet = value) }
    }

    fun drop() {
        val current = _state.value
        if (!current.canDrop) return
        autoJob?.cancel()
        val result = dropEngine.drop(Random(System.nanoTime()))
        val balanceAfterBet = current.balance - current.bet
        progressRepository.saveBalance(balanceAfterBet)
        _state.update {
            it.copy(
                phase = Phase.FALLING,
                balance = balanceAfterBet,
                balanceText = SparksFormatter.grouped(balanceAfterBet),
                tokenVisible = true,
                tokenColumn = CENTER_COLUMN,
                tokenRow = 0f,
                hitRow = -1,
                activeSlot = -1
            )
        }
        flightJob = viewModelScope.launch {
            val flightMs = if (current.reducedMotion) {
                GameConfig.REDUCED_FLIGHT_MS
            } else {
                GameConfig.DROP_FLIGHT_MS
            }
            val startedAt = System.currentTimeMillis()
            var pausedMs = 0L
            while (isActive) {
                if (_state.value.phase == Phase.PAUSED) {
                    delay(GameConfig.TICK_MS)
                    pausedMs += GameConfig.TICK_MS
                    continue
                }
                val elapsed = System.currentTimeMillis() - startedAt - pausedMs
                val fraction = (elapsed.toFloat() / flightMs).coerceIn(0f, 1f)
                applyFlight(result.path, fraction)
                if (fraction >= 1f) break
                delay(GameConfig.TICK_MS)
            }
            settle(result.slot, result.multiplier)
        }
    }

    fun openPause() {
        if (_state.value.phase == Phase.PAUSED) return
        resumePhase = _state.value.phase
        _state.update { it.copy(phase = Phase.PAUSED, pauseVisible = true) }
        autoJob?.cancel()
    }

    fun closePause() {
        _state.update { it.copy(phase = resumePhase, pauseVisible = false) }
        if (resumePhase == Phase.IDLE) armAutoDrop()
    }

    fun openQuit() {
        if (_state.value.phase != Phase.PAUSED) {
            resumePhase = _state.value.phase
            _state.update { it.copy(phase = Phase.PAUSED) }
        }
        _state.update { it.copy(quitVisible = true, pauseVisible = false) }
        autoJob?.cancel()
    }

    fun closeQuit() {
        _state.update { it.copy(quitVisible = false, phase = resumePhase) }
        if (resumePhase == Phase.IDLE) armAutoDrop()
    }

    fun pause() {
        cancelJobs()
    }

    private fun applyFlight(path: List<Boolean>, fraction: Float) {
        val rows = GameConfig.PEG_ROWS
        val rowFloat = fraction * rows
        val row = floor(rowFloat).toInt().coerceIn(0, rows - 1)
        val within = (rowFloat - row).coerceIn(0f, 1f)
        val eased = within * within * (3f - 2f * within)
        var column = CENTER_COLUMN
        for (index in 0 until row) {
            column += if (path[index]) 0.5f else -0.5f
        }
        column += (if (path[row]) 0.5f else -0.5f) * eased
        _state.update {
            it.copy(
                tokenColumn = column,
                tokenRow = rowFloat,
                hitRow = row
            )
        }
    }

    private suspend fun settle(slot: Int, multiplier: Float) {
        val current = _state.value
        val payout = scoreCalculator.payout(current.bet, multiplier)
        val newBalance = current.balance + payout
        val newScore = current.roundScore + payout
        val drops = current.dropsUsed + 1
        bestMultiplier = max(bestMultiplier, multiplier)
        if (multiplier >= bestMultiplier) bestSlot = slot
        sparksWon += payout
        xpGained += scoreCalculator.xpForDrop(multiplier)
        progressRepository.saveBalance(newBalance)
        _state.update {
            it.copy(
                phase = Phase.SETTLING,
                balance = newBalance,
                balanceText = SparksFormatter.grouped(newBalance),
                roundScore = newScore,
                roundScoreText = SparksFormatter.grouped(newScore),
                dropsUsed = drops,
                activeSlot = slot,
                landedSlots = it.landedSlots + slot,
                tokenColumn = slot.toFloat(),
                tokenRow = GameConfig.PEG_ROWS.toFloat()
            )
        }
        delay(GameConfig.SETTLE_MS)
        _state.update { it.copy(tokenVisible = false, hitRow = -1) }
        val outOfDrops = drops >= GameConfig.ROUND_DROPS
        val outOfSparks = newBalance < (GameConfig.BETS.minOrNull() ?: 0)
        if (outOfDrops || outOfSparks) {
            finishRound(newScore)
        } else {
            _state.update { it.copy(phase = Phase.IDLE) }
            armAutoDrop()
        }
    }

    private fun finishRound(roundScore: Int) {
        _state.update { it.copy(phase = Phase.ROUND_TAIL) }
        tailJob?.cancel()
        tailJob = viewModelScope.launch {
            val elapsed = System.currentTimeMillis() - roundStartedAt
            delay(max(0L, GameConfig.MIN_ROUND_MS - elapsed))
            val progress = progressRepository.progress()
            val newBest = scoreCalculator.isNewBest(roundScore, progress.bestScore)
            val summary = RoundSummary(
                roundScore = roundScore,
                sparksWon = sparksWon,
                bestSlot = bestSlot,
                bestMultiplier = bestMultiplier,
                dropsUsed = _state.value.dropsUsed,
                xpGained = xpGained,
                newBest = newBest,
                rewards = rewardsFor(newBest)
            )
            progressRepository.applyRound(summary)
            progressRepository.saveLastRound(summary)
            _events.send(RoundFinished(roundScore, bestSlot, newBest))
        }
    }

    private fun rewardsFor(newBest: Boolean): List<Int> {
        val rewards = mutableListOf<Int>()
        if (xpGained > 0) rewards.add(0)
        if (bestMultiplier >= 6.0f) rewards.add(1)
        if (newBest) rewards.add(2)
        return rewards
    }

    private fun armAutoDrop() {
        autoJob?.cancel()
        autoJob = viewModelScope.launch {
            val sinceMount = System.currentTimeMillis() - mountedAt
            val wait = max(
                GameConfig.AUTO_DROP_ENGAGED_MS,
                GameConfig.AUTO_DROP_MOUNT_FLOOR_MS - sinceMount
            )
            delay(wait)
            drop()
        }
    }

    private fun cancelJobs() {
        flightJob?.cancel()
        autoJob?.cancel()
        tailJob?.cancel()
        flightJob = null
        autoJob = null
        tailJob = null
    }

    override fun onCleared() {
        cancelJobs()
        super.onCleared()
    }

    private companion object {
        const val CENTER_COLUMN = 6f
    }
}

data class RoundFinished(
    val roundScore: Int,
    val bestSlot: Int,
    val newBest: Boolean
)
