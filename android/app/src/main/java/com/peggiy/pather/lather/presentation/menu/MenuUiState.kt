package com.peggiy.pather.lather.presentation.menu

data class TokenCardUi(
    val id: Int,
    val nameRes: Int,
    val unlockLevel: Int,
    val owned: Boolean,
    val equipped: Boolean
)

data class LayoutCardUi(
    val id: Int,
    val nameRes: Int,
    val unlockLevel: Int,
    val unlocked: Boolean,
    val active: Boolean
)

data class ChallengeCardUi(
    val id: Int,
    val titleRes: Int,
    val current: Int,
    val target: Int,
    val complete: Boolean,
    val claimed: Boolean
) {
    val fraction: Float get() = if (target <= 0) 0f else (current.toFloat() / target).coerceIn(0f, 1f)
}

data class MenuUiState(
    val level: Int = 1,
    val xpFraction: Float = 0f,
    val balance: Int = 0,
    val balanceText: String = "",
    val bestScore: Int = 0,
    val bestScoreText: String = "",
    val lifetimeDrops: Int = 0,
    val lifetimeDropsText: String = "",
    val bestMultiplier: Float = 0f,
    val bestMultiplierText: String = "",
    val welcomeVisible: Boolean = false,
    val challengePanelVisible: Boolean = false,
    val collectionPanelVisible: Boolean = false,
    val profilePanelVisible: Boolean = false,
    val bonusDialogVisible: Boolean = false,
    val settingsDialogVisible: Boolean = false,
    val resetDialogVisible: Boolean = false,
    val tokens: List<TokenCardUi> = emptyList(),
    val layouts: List<LayoutCardUi> = emptyList(),
    val challenges: List<ChallengeCardUi> = emptyList(),
    val selectedToken: Int = 1,
    val bonusClaimable: Boolean = false,
    val bonusStreak: Int = 1,
    val soundOn: Boolean = true,
    val hapticsOn: Boolean = true,
    val reducedMotion: Boolean = false,
    val challengesCompleted: Int = 0,
    val tokensOwned: Int = 1
) {
    val statCardsVisible: Boolean
        get() = listOf(bestScore > 0, lifetimeDrops > 0, bestMultiplier > 0f).count { it } >= 2
}
