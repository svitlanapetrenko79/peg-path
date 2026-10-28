package com.peggiy.pather.lather.domain.repository

import com.peggiy.pather.lather.domain.model.PlayerProgress
import com.peggiy.pather.lather.domain.model.RoundSummary

interface ProgressRepository {
    fun progress(): PlayerProgress
    fun saveBalance(balance: Int)
    fun saveBet(bet: Int)
    fun saveSelectedToken(id: Int)
    fun saveSelectedLayout(id: Int)
    fun unlockToken(id: Int)
    fun markWelcomeSeen()
    fun applyRound(summary: RoundSummary)
    fun saveLastRound(summary: RoundSummary)
    fun lastRound(): RoundSummary
    fun claimChallenge(id: Int)
    fun claimDailyBonus(day: Long, streak: Int, amount: Int)
    fun reset()
}
