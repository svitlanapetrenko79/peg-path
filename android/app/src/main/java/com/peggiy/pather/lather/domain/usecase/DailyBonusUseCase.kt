package com.peggiy.pather.lather.domain.usecase

import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.domain.model.PlayerProgress

class DailyBonusUseCase {

    fun dayIndex(nowMillis: Long): Long = nowMillis / 86_400_000L

    fun claimable(progress: PlayerProgress, nowMillis: Long): Boolean =
        dayIndex(nowMillis) > progress.lastBonusDay

    fun nextStreak(progress: PlayerProgress, nowMillis: Long): Int {
        val today = dayIndex(nowMillis)
        val continued = today - progress.lastBonusDay == 1L
        val next = if (continued) progress.bonusStreak + 1 else 1
        return if (next > GameConfig.BONUS_STREAK_DAYS) 1 else next
    }

    fun amount(): Int = GameConfig.DAILY_BONUS
}
