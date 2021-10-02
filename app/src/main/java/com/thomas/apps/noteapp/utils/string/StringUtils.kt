package com.thomas.apps.noteapp.utils.string

object StringUtils {
    @ExperimentalStdlibApi
    fun String.capitalizeFirst(): String {
        if (this.isNotEmpty()) {
            return this.replaceFirstChar { it.uppercaseChar() }
        }
        return this
    }

    @ExperimentalStdlibApi
    fun String.capitalizeAllFirst(): String {
        if (this.isNotEmpty()) {
            var text = this.split(" ")
            text = text.map { it.capitalizeFirst() }
            return text.joinToString(" ")
        }
        return this
    }
}