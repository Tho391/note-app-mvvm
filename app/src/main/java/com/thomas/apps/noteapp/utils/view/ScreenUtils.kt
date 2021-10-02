package com.thomas.apps.noteapp.utils.view

import android.content.Context
import android.util.TypedValue
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object ScreenUtils {

    private const val RATIO_4_3_VALUE = 4.0 / 3.0
    private const val RATIO_16_9_VALUE = 16.0 / 9.0

    fun Context.getScreenHeight() = resources.displayMetrics.heightPixels
    fun Context.getScreenWidth() = resources.displayMetrics.widthPixels
    fun Context.getStatusBarHeight(): Int {
        var result = 0
        val resourceId: Int =
            resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resourceId > 0) {
            result = resources.getDimensionPixelSize(resourceId)
        }
        return result
    }

    private fun Context.getActionbarHeight(): Int {
        val tv = TypedValue()
        val actionBarHeight = if (theme.resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            TypedValue.complexToDimensionPixelSize(tv.data, resources.displayMetrics)
        } else {
            0
        }
        return actionBarHeight
    }
}