package com.thomas.apps.noteapp.utils.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.thomas.apps.noteapp.R

object ResourceUtils {
    fun Context.getColorOnBackground(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorOnBackground, type, true)
        return type.data
    }

    fun Context.getColorSurface(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorSurface, type, true)
        return type.data
    }

    fun Context.getColorOnSurface(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorOnSurface, type, true)
        return type.data
    }

    fun Context.getColorBackground(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.backgroundColor, type, true)
        return type.data
    }

    fun Context.getColorPrimary(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimary, type, true)
        return type.data
    }

    fun Context.getColorPrimaryVariant(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorPrimaryVariant, type, true)
        return type.data
    }

    fun Context.getColorSecondary(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorSecondary, type, true)
        return type.data
    }

    fun Context.getColorSecondaryVariant(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorSecondaryVariant, type, true)
        return type.data
    }

    fun Context.getColorError(): Int {
        val type = TypedValue()
        theme.resolveAttribute(R.attr.colorError, type, true)
        return type.data
    }
}