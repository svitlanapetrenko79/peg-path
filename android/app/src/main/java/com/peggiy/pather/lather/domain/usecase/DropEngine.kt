package com.peggiy.pather.lather.domain.usecase

import com.peggiy.pather.lather.core.config.GameConfig
import com.peggiy.pather.lather.domain.model.DropResult
import kotlin.random.Random

class DropEngine {

    fun drop(random: Random): DropResult {
        val path = List(GameConfig.PEG_ROWS) { random.nextBoolean() }
        val slot = path.count { it }.coerceIn(0, GameConfig.SLOT_COUNT - 1)
        return DropResult(path = path, slot = slot, multiplier = GameConfig.MULTIPLIERS[slot])
    }

    fun multiplierAt(slot: Int): Float =
        GameConfig.MULTIPLIERS.getOrElse(slot) { 1f }
}
