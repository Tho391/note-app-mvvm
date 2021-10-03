package com.thomas.apps.noteapp.feature_login.domain.use_case

import com.thomas.apps.noteapp.feature_login.domain.repository.LoginRepository
import com.thomas.apps.noteapp.feature_note.domain.repository.NoteRepository

data class Logout(
    private val loginRepository: LoginRepository,
    private val noteRepository: NoteRepository,
){
    suspend operator fun invoke() {
        loginRepository.clearLocalCache()
        noteRepository.deleteAllNotes()
    }
}