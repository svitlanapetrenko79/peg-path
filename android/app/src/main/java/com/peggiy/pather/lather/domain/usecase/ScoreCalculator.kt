package com.peggiy.pather.lather.domain.usecase

import kotlin.math.roundToInt

class ScoreCalculator {

    fun payout(bet: Int, multiplier: Float): Int = (bet * multiplier).roundToInt()

    fun xpForDrop(multiplier: Float): Int = if (multiplier >= 6.0f) 6 else 1

    fun isNewBest(roundScore: Int, bestScore: Int): Boolean = roundScore > bestScore
}
