package com.peggiy.pather.lather.core.ui

import com.peggiy.pather.lather.R

fun tokenDrawable(id: Int): Int = when (id) {
    1 -> R.drawable.sprite_1
    2 -> R.drawable.sprite_2
    3 -> R.drawable.sprite_3
    4 -> R.drawable.sprite_4
    else -> R.drawable.sprite_5
}

fun rewardDrawable(index: Int): Int = when (index) {
    0 -> R.drawable.sprite_2
    1 -> R.drawable.sprite_3
    2 -> R.drawable.sprite_4
    else -> R.drawable.sprite_5
}
