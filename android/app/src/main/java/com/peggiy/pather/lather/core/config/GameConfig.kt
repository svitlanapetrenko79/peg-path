package com.peggiy.pather.lather.core.config

object GameConfig {
    const val LOADER_DURATION_MS = 8000L
    const val PEG_ROWS = 12
    const val SLOT_COUNT = 13
    const val ROUND_DROPS = 8
    const val DROP_FLIGHT_MS = 2000L
    const val REDUCED_FLIGHT_MS = 900L
    const val SETTLE_MS = 900L
    const val MIN_ROUND_MS = 24000L
    const val AUTO_DROP_MOUNT_FLOOR_MS = 24000L
    const val AUTO_DROP_ENGAGED_MS = 7000L
    const val TICK_MS = 16L
    const val START_BALANCE = 500
    const val DAILY_BONUS = 250
    const val XP_PER_LEVEL = 120
    const val BONUS_STREAK_DAYS = 7
    const val BOARD_MAX_DP = 380
    const val BOARD_PAD_DP = 6
    const val BOARD_BORDER_DP = 2
    const val ROW_PITCH_DP = 30
    const val BOARD_TOP_OFFSET_DP = 30
    const val SLOT_STRIP_DP = 44
    const val APP_VERSION = "1.0"

    val BETS = listOf(10, 25, 50)

    val MULTIPLIERS = listOf(
        12.0f, 6.0f, 3.0f, 1.6f, 1.1f, 0.6f, 0.4f, 0.6f, 1.1f, 1.6f, 3.0f, 6.0f, 12.0f
    )
}
