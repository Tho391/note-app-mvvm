package com.thomas.apps.noteapp.feature_note.domain.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Note(
    val title: String,
    val content: String,
    val timeStamp: Long,
    val color: Int,

    @PrimaryKey
    val id: Long = 0,
) {
    companion object {
       private val colorString = listOf(
            "#ff9800", //orange
            "#ffc107", //amber
            "#ffeb3b", //yellow
            "#cddc39", //lime
            "#8bc34a", //light green
            "#009688", //teal
            "#00bcd4", //cyan
            "#03a9f4", //light blue
            "#2196f3", //blue
            "#3f51b5", //Indigo
            "#673ab7", //Deep Purple
            "#9c27b0", //Purple
            "#e91e63", //Pink
            "#f44336", //Red
        )
        val noteColors = colorString.mapNotNull {
            try {
                Color.parseColor(it)
            } catch (e: Exception) {
                null
            }
        }
    }
}

class InvalidNoteException(message: String): Exception()