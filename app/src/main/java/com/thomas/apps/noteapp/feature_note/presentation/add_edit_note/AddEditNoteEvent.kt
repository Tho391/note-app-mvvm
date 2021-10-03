package com.thomas.apps.noteapp.feature_note.presentation.add_edit_note

sealed class AddEditNoteEvent {
    data class ChangeColor(val color: Int) : AddEditNoteEvent()
    object SaveNote : AddEditNoteEvent()
}