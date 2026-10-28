package com.peggiy.pather.lather.presentation.menu

import androidx.lifecycle.ViewModel
import com.peggiy.pather.lather.data.sample.SampleData
import com.peggiy.pather.lather.domain.model.PlayerProgress
import com.peggiy.pather.lather.domain.repository.ProgressRepository
import com.peggiy.pather.lather.domain.repository.SettingsRepository
import com.peggiy.pather.lather.domain.usecase.DailyBonusUseCase
import com.peggiy.pather.lather.domain.usecase.SparksFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class MenuViewModel(
    private val progressRepository: ProgressRepository,
    private val settingsRepository: SettingsRepository,
    private val dailyBonus: DailyBonusUseCase
) : ViewModel() {

    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        refresh()
    }

    fun refresh() {
        val progress = progressRepository.progress()
        val settings = settingsRepository.settings()
        val now = System.currentTimeMillis()
        _state.update {
            it.copy(
                level = progress.level,
                xpFraction = progress.xpFraction,
                balance = progress.balance,
                balanceText = SparksFormatter.grouped(progress.balance),
                bestScore = progress.bestScore,
                bestScoreText = SparksFormatter.grouped(progress.bestScore),
                lifetimeDrops = progress.lifetimeDrops,
                lifetimeDropsText = SparksFormatter.grouped(progress.lifetimeDrops),
                bestMultiplier = progress.bestMultiplier,
                bestMultiplierText = SparksFormatter.multiplier(progress.bestMultiplier),
                welcomeVisible = !progress.welcomeSeen,
                tokens = tokenCards(progress),
                layouts = layoutCards(progress),
                challenges = challengeCards(progress),
                selectedToken = progress.selectedToken,
                bonusClaimable = dailyBonus.claimable(progress, now),
                bonusStreak = if (progress.bonusStreak <= 0) 1 else progress.bonusStreak,
                soundOn = settings.soundOn,
                hapticsOn = settings.hapticsOn,
                reducedMotion = settings.reducedMotion,
                challengesCompleted = progress.claimedChallenges.size,
                tokensOwned = progress.ownedTokens.size
            )
        }
    }

    fun onPlay() {
        progressRepository.markWelcomeSeen()
        _state.update { it.copy(welcomeVisible = false) }
    }

    fun openChallenges() = _state.update { it.copy(challengePanelVisible = true) }

    fun openCollection() = _state.update { it.copy(collectionPanelVisible = true) }

    fun openProfile() = _state.update { it.copy(profilePanelVisible = true) }

    fun openBonus() = _state.update { it.copy(bonusDialogVisible = true) }

    fun openSettings() = _state.update { it.copy(settingsDialogVisible = true) }

    fun openResetConfirm() = _state.update { it.copy(resetDialogVisible = true) }

    fun dismissPanels() = _state.update {
        it.copy(
            challengePanelVisible = false,
            collectionPanelVisible = false,
            profilePanelVisible = false,
            bonusDialogVisible = false,
            settingsDialogVisible = false,
            resetDialogVisible = false
        )
    }

    fun dismissResetConfirm() = _state.update { it.copy(resetDialogVisible = false) }

    fun selectToken(id: Int) {
        val progress = progressRepository.progress()
        if (!progress.ownedTokens.contains(id)) return
        progressRepository.saveSelectedToken(id)
        refresh()
    }

    fun selectLayout(id: Int) {
        val progress = progressRepository.progress()
        val layout = SampleData.boardLayouts.firstOrNull { it.id == id } ?: return
        if (progress.level < layout.unlockLevel) return
        progressRepository.saveSelectedLayout(id)
        refresh()
    }

    fun claimChallenge(id: Int) {
        val progress = progressRepository.progress()
        val challenge = SampleData.challenges.firstOrNull { it.id == id } ?: return
        val current = progress.challengeProgress.getOrElse(id) { 0 }
        if (current < challenge.target || progress.claimedChallenges.contains(id)) return
        progressRepository.claimChallenge(id)
        progressRepository.saveBalance(progress.balance + REWARD_SPARKS)
        val unlockable = SampleData.tokenSkins.firstOrNull { !progress.ownedTokens.contains(it.id) }
        if (unlockable != null) progressRepository.unlockToken(unlockable.id)
        refresh()
    }

    fun claimDailyBonus() {
        val progress = progressRepository.progress()
        val now = System.currentTimeMillis()
        if (!dailyBonus.claimable(progress, now)) return
        progressRepository.claimDailyBonus(
            day = dailyBonus.dayIndex(now),
            streak = dailyBonus.nextStreak(progress, now),
            amount = dailyBonus.amount()
        )
        refresh()
    }

    fun setSound(enabled: Boolean) {
        settingsRepository.setSound(enabled)
        refresh()
    }

    fun setHaptics(enabled: Boolean) {
        settingsRepository.setHaptics(enabled)
        refresh()
    }

    fun setReducedMotion(enabled: Boolean) {
        settingsRepository.setReducedMotion(enabled)
        refresh()
    }

    fun resetProgress() {
        progressRepository.reset()
        _state.update { it.copy(resetDialogVisible = false, profilePanelVisible = false) }
        refresh()
    }

    private fun tokenCards(progress: PlayerProgress): List<TokenCardUi> =
        SampleData.tokenSkins.map { skin ->
            TokenCardUi(
                id = skin.id,
                nameRes = skin.nameRes,
                unlockLevel = skin.unlockLevel,
                owned = progress.ownedTokens.contains(skin.id) || progress.level >= skin.unlockLevel,
                equipped = progress.selectedToken == skin.id
            )
        }

    private fun layoutCards(progress: PlayerProgress): List<LayoutCardUi> =
        SampleData.boardLayouts.map { layout ->
            LayoutCardUi(
                id = layout.id,
                nameRes = layout.nameRes,
                unlockLevel = layout.unlockLevel,
                unlocked = progress.level >= layout.unlockLevel,
                active = progress.selectedLayout == layout.id
            )
        }

    private fun challengeCards(progress: PlayerProgress): List<ChallengeCardUi> =
        SampleData.challenges.map { challenge ->
            val current = progress.challengeProgress.getOrElse(challenge.id) { 0 }
            ChallengeCardUi(
                id = challenge.id,
                titleRes = challenge.titleRes,
                current = current.coerceAtMost(challenge.target),
                target = challenge.target,
                complete = current >= challenge.target,
                claimed = progress.claimedChallenges.contains(challenge.id)
            )
        }

    private companion object {
        const val REWARD_SPARKS = 150
    }
}
