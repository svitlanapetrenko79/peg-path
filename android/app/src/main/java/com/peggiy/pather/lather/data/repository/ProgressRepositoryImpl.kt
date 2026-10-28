package com.peggiy.pather.lather.data.repository

import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.data.local.PegPathPrefs
import com.peggiy.pather.lather.domain.model.PlayerProgress
import com.peggiy.pather.lather.domain.model.RoundSummary
import com.peggiy.pather.lather.domain.repository.ProgressRepository
import kotlin.math.max

class ProgressRepositoryImpl(private val prefs: PegPathPrefs) : ProgressRepository {

    override fun progress(): PlayerProgress = PlayerProgress(
        balance = prefs.int(PegPathPrefs.BALANCE, GameConfig.START_BALANCE),
        bestScore = prefs.int(PegPathPrefs.BEST_SCORE, 0),
        bestMultiplier = prefs.float(PegPathPrefs.BEST_MULTIPLIER, 0f),
        lifetimeDrops = prefs.int(PegPathPrefs.LIFETIME_DROPS, 0),
        xp = prefs.int(PegPathPrefs.XP, 0),
        bet = prefs.int(PegPathPrefs.BET, GameConfig.BETS[1]),
        selectedToken = prefs.int(PegPathPrefs.SELECTED_TOKEN, 1),
        selectedLayout = prefs.int(PegPathPrefs.SELECTED_LAYOUT, 0),
        ownedTokens = prefs.ints(PegPathPrefs.OWNED_TOKENS).ifEmpty { setOf(1) },
        challengeProgress = listOf(
            prefs.int(PegPathPrefs.CHALLENGE_SIX, 0),
            prefs.int(PegPathPrefs.CHALLENGE_SCORE, 0),
            prefs.int(PegPathPrefs.CHALLENGE_DROPS, 0)
        ),
        claimedChallenges = prefs.ints(PegPathPrefs.CLAIMED_CHALLENGES),
        welcomeSeen = prefs.bool(PegPathPrefs.WELCOME_SEEN, false),
        lastBonusDay = prefs.long(PegPathPrefs.LAST_BONUS_DAY, -1L),
        bonusStreak = prefs.int(PegPathPrefs.BONUS_STREAK, 0)
    )

    override fun saveBalance(balance: Int) = prefs.putInt(PegPathPrefs.BALANCE, max(0, balance))

    override fun saveBet(bet: Int) = prefs.putInt(PegPathPrefs.BET, bet)

    override fun saveSelectedToken(id: Int) = prefs.putInt(PegPathPrefs.SELECTED_TOKEN, id)

    override fun saveSelectedLayout(id: Int) = prefs.putInt(PegPathPrefs.SELECTED_LAYOUT, id)

    override fun unlockToken(id: Int) {
        prefs.putInts(PegPathPrefs.OWNED_TOKENS, prefs.ints(PegPathPrefs.OWNED_TOKENS) + setOf(1, id))
    }

    override fun markWelcomeSeen() = prefs.putBool(PegPathPrefs.WELCOME_SEEN, true)

    override fun applyRound(summary: RoundSummary) {
        val current = progress()
        prefs.putInt(PegPathPrefs.BEST_SCORE, max(current.bestScore, summary.roundScore))
        prefs.putFloat(PegPathPrefs.BEST_MULTIPLIER, max(current.bestMultiplier, summary.bestMultiplier))
        prefs.putInt(PegPathPrefs.LIFETIME_DROPS, current.lifetimeDrops + summary.dropsUsed)
        prefs.putInt(PegPathPrefs.XP, current.xp + summary.xpGained)
        prefs.putInt(PegPathPrefs.CHALLENGE_DROPS, current.challengeProgress[2] + summary.dropsUsed)
        prefs.putInt(PegPathPrefs.CHALLENGE_SCORE, max(current.challengeProgress[1], summary.roundScore))
        if (summary.bestMultiplier >= 6.0f) {
            prefs.putInt(PegPathPrefs.CHALLENGE_SIX, current.challengeProgress[0] + 1)
        }
    }

    override fun saveLastRound(summary: RoundSummary) {
        prefs.putInt(PegPathPrefs.ROUND_SCORE, summary.roundScore)
        prefs.putInt(PegPathPrefs.ROUND_WON, summary.sparksWon)
        prefs.putInt(PegPathPrefs.ROUND_BEST_SLOT, summary.bestSlot)
        prefs.putFloat(PegPathPrefs.ROUND_BEST_MULT, summary.bestMultiplier)
        prefs.putInt(PegPathPrefs.ROUND_DROPS_USED, summary.dropsUsed)
        prefs.putInt(PegPathPrefs.ROUND_XP, summary.xpGained)
        prefs.putBool(PegPathPrefs.ROUND_NEW_BEST, summary.newBest)
        prefs.putInts(PegPathPrefs.ROUND_REWARDS, summary.rewards.toSet())
    }

    override fun lastRound(): RoundSummary = RoundSummary(
        roundScore = prefs.int(PegPathPrefs.ROUND_SCORE, 0),
        sparksWon = prefs.int(PegPathPrefs.ROUND_WON, 0),
        bestSlot = prefs.int(PegPathPrefs.ROUND_BEST_SLOT, 6),
        bestMultiplier = prefs.float(PegPathPrefs.ROUND_BEST_MULT, 0f),
        dropsUsed = prefs.int(PegPathPrefs.ROUND_DROPS_USED, 0),
        xpGained = prefs.int(PegPathPrefs.ROUND_XP, 0),
        newBest = prefs.bool(PegPathPrefs.ROUND_NEW_BEST, false),
        rewards = prefs.ints(PegPathPrefs.ROUND_REWARDS).sorted()
    )

    override fun claimChallenge(id: Int) {
        prefs.putInts(PegPathPrefs.CLAIMED_CHALLENGES, prefs.ints(PegPathPrefs.CLAIMED_CHALLENGES) + id)
    }

    override fun claimDailyBonus(day: Long, streak: Int, amount: Int) {
        prefs.putLong(PegPathPrefs.LAST_BONUS_DAY, day)
        prefs.putInt(PegPathPrefs.BONUS_STREAK, streak)
        prefs.putInt(PegPathPrefs.BALANCE, progress().balance + amount)
    }

    override fun reset() = prefs.clear()
}
