package com.peggiy.pather.lather.domain.usecase

import kotlin.math.abs
import kotlin.math.roundToInt

object SparksFormatter {

    fun grouped(value: Int): String {
        val negative = value < 0
        val digits = abs(value).toString()
        val builder = StringBuilder()
        for (index in digits.indices) {
            if (index > 0 && (digits.length - index) % 3 == 0) {
                builder.append(',')
            }
            builder.append(digits[index])
        }
        return if (negative) "-" + builder.toString() else builder.toString()
    }

    fun multiplier(value: Float): String {
        val scaled = (value * 10f).roundToInt()
        return if (scaled % 10 == 0) (scaled / 10).toString() else (scaled / 10).toString() + "." + (scaled % 10)
    }
}
