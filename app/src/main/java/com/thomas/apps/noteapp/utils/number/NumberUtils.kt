package com.thomas.apps.noteapp.utils.number

import com.thomas.apps.noteapp.utils.number.NumberUtils.isEvenNumber
import kotlin.math.roundToInt

object NumberUtils {
    fun Double.roundToHundred(): Int {
        return (((this + 50) / 100.0).roundToInt() * 100)
    }

    fun Float.roundToHundred(): Int {
        return (((this + 50) / 100.0).roundToInt() * 100)
    }

    fun Int.roundToHundred(): Int {
        return (((this + 50) / 100.0).roundToInt() * 100)
    }

    fun Double.roundToTen(): Int {
        return (((this + 5) / 10.0).roundToInt() * 10)
    }

    fun Float.roundToTen(): Int {
        return (((this + 5) / 10.0).roundToInt() * 10)
    }

    fun Int.roundToTen(): Int {
        return (((this + 5) / 10.0).roundToInt() * 10)
    }

    fun Int.isEvenNumber(): Boolean {
        return this % 2 == 0
    }

    fun Int.isOddNumber(): Boolean {
        return this % 2 != 0
    }

    fun Double.round(decimals: Int): Double {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return kotlin.math.round(this * multiplier) / multiplier
    }

    fun Float.round(decimals: Int): Float {
        var multiplier = 1.0
        repeat(decimals) { multiplier *= 10 }
        return (kotlin.math.round(this * multiplier) / multiplier).toFloat()
    }
}