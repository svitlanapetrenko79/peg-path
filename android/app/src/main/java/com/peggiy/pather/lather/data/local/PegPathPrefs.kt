package com.peggiy.pather.lather.data.local

import android.content.Context
import android.content.SharedPreferences

class PegPathPrefs(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(STORE, Context.MODE_PRIVATE)

    fun int(key: String, fallback: Int): Int = prefs.getInt(key, fallback)

    fun putInt(key: String, value: Int) {
        prefs.edit().putInt(key, value).apply()
    }

    fun long(key: String, fallback: Long): Long = prefs.getLong(key, fallback)

    fun putLong(key: String, value: Long) {
        prefs.edit().putLong(key, value).apply()
    }

    fun float(key: String, fallback: Float): Float = prefs.getFloat(key, fallback)

    fun putFloat(key: String, value: Float) {
        prefs.edit().putFloat(key, value).apply()
    }

    fun bool(key: String, fallback: Boolean): Boolean = prefs.getBoolean(key, fallback)

    fun putBool(key: String, value: Boolean) {
        prefs.edit().putBoolean(key, value).apply()
    }

    fun ints(key: String): Set<Int> =
        prefs.getStringSet(key, emptySet()).orEmpty().mapNotNull { it.toIntOrNull() }.toSet()

    fun putInts(key: String, value: Set<Int>) {
        prefs.edit().putStringSet(key, value.map { it.toString() }.toSet()).apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    companion object {
        private const val STORE = "pegpath_store"

        const val BALANCE = "balance"
        const val BEST_SCORE = "best_score"
        const val BEST_MULTIPLIER = "best_multiplier"
        const val LIFETIME_DROPS = "lifetime_drops"
        const val XP = "xp"
        const val BET = "bet"
        const val SELECTED_TOKEN = "selected_token"
        const val SELECTED_LAYOUT = "selected_layout"
        const val OWNED_TOKENS = "owned_tokens"
        const val CLAIMED_CHALLENGES = "claimed_challenges"
        const val CHALLENGE_SIX = "challenge_six"
        const val CHALLENGE_SCORE = "challenge_score"
        const val CHALLENGE_DROPS = "challenge_drops"
        const val WELCOME_SEEN = "welcome_seen"
        const val LAST_BONUS_DAY = "last_bonus_day"
        const val BONUS_STREAK = "bonus_streak"
        const val SOUND = "sound_on"
        const val HAPTICS = "haptics_on"
        const val REDUCED_MOTION = "reduced_motion"
        const val ROUND_SCORE = "round_score"
        const val ROUND_WON = "round_won"
        const val ROUND_BEST_SLOT = "round_best_slot"
        const val ROUND_BEST_MULT = "round_best_mult"
        const val ROUND_DROPS_USED = "round_drops_used"
        const val ROUND_NEW_BEST = "round_new_best"
        const val ROUND_XP = "round_xp"
        const val ROUND_REWARDS = "round_rewards"
    }
}
