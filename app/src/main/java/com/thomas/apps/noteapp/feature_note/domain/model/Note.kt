package com.thomas.apps.noteapp.feature_note.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.thomas.apps.noteapp.R

@Entity
data class Note(
    val title: String,
    val content: String,
    val timeStamp: Long,
    val color: Int,

    @PrimaryKey
    val id: Long? = null,
) {
    companion object {
        val noteColors = listOf(
            R.color.yellow,
            R.color.teal,
            R.color.indigo,
            R.color.deep_purple,
            R.color.purple,
            R.color.red,
        )
    }
}

class InvalidNoteException(message: String) : Exception(message)