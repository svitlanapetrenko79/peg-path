package com.peggiy.pather.lather.data.sample

import com.peggiy.pather.lather.R
import com.peggiy.pather.lather.domain.model.BoardLayout
import com.peggiy.pather.lather.domain.model.Challenge
import com.peggiy.pather.lather.domain.model.TokenSkin

object SampleData {

    val boardLayouts = listOf(
        BoardLayout(id = 0, nameRes = R.string.layout_rookie, unlockLevel = 1),
        BoardLayout(id = 1, nameRes = R.string.layout_fork, unlockLevel = 3),
        BoardLayout(id = 2, nameRes = R.string.layout_spire, unlockLevel = 6)
    )

    val tokenSkins = listOf(
        TokenSkin(id = 1, nameRes = R.string.token_spark_core, unlockLevel = 1),
        TokenSkin(id = 2, nameRes = R.string.token_prism_bit, unlockLevel = 2),
        TokenSkin(id = 3, nameRes = R.string.token_violet_cell, unlockLevel = 4),
        TokenSkin(id = 4, nameRes = R.string.token_pulse_node, unlockLevel = 7),
        TokenSkin(id = 5, nameRes = R.string.token_apex_shard, unlockLevel = 10)
    )

    val challenges = listOf(
        Challenge(id = 0, titleRes = R.string.challenge_six_x, target = 1),
        Challenge(id = 1, titleRes = R.string.challenge_big_round, target = 900),
        Challenge(id = 2, titleRes = R.string.challenge_many_drops, target = 24)
    )
}
