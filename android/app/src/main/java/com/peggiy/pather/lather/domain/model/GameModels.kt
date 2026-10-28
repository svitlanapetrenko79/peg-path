package com.peggiy.pather.lather.domain.model

import com.peggiy.pather.lather.core.config.GameConfig

enum class Phase {
    IDLE,
    FALLING,
    SETTLING,
    ROUND_TAIL,
    PAUSED
}

data class DropResult(
    val path: List<Boolean>,
    val slot: Int,
    val multiplier: Float
)

data class BoardLayout(
    val id: Int,
    val nameRes: Int,
    val unlockLevel: Int
)

data class TokenSkin(
    val id: Int,
    val nameRes: Int,
    val unlockLevel: Int
)

data class Challenge(
    val id: Int,
    val titleRes: Int,
    val target: Int
)

data class PlayerProgress(
    val balance: Int,
    val bestScore: Int,
    val bestMultiplier: Float,
    val lifetimeDrops: Int,
    val xp: Int,
    val bet: Int,
    val selectedToken: Int,
    val selectedLayout: Int,
    val ownedTokens: Set<Int>,
    val challengeProgress: List<Int>,
    val claimedChallenges: Set<Int>,
    val welcomeSeen: Boolean,
    val lastBonusDay: Long,
    val bonusStreak: Int
) {
    val level: Int get() = xp / GameConfig.XP_PER_LEVEL + 1
    val xpFraction: Float get() = (xp % GameConfig.XP_PER_LEVEL) / GameConfig.XP_PER_LEVEL.toFloat()
}

data class RoundSummary(
    val roundScore: Int,
    val sparksWon: Int,
    val bestSlot: Int,
    val bestMultiplier: Float,
    val dropsUsed: Int,
    val xpGained: Int,
    val newBest: Boolean,
    val rewards: List<Int>
)

data class AppSettings(
    val soundOn: Boolean,
    val hapticsOn: Boolean,
    val reducedMotion: Boolean
)
