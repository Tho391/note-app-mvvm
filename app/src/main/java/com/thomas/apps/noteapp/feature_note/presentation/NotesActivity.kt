package com.thomas.apps.noteapp.feature_note.presentation

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.thomas.apps.noteapp.databinding.ActivityNotesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}