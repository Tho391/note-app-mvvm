package com.thomas.apps.noteapp.feature_note.presentation

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.thomas.apps.noteapp.R
import com.thomas.apps.noteapp.databinding.ActivityNotesBinding
import com.thomas.apps.noteapp.feature_note.presentation.notes.NotesEvent
import com.thomas.apps.noteapp.feature_note.presentation.notes.NotesViewModel
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class NotesActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotesBinding
    private val notesViewModel: NotesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityNotesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpToolbar()
    }

    private fun setUpToolbar() {
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_sort -> {
                    notesViewModel.onEvent(NotesEvent.ToggleOrderSection)
                    Timber.i("sort click ${notesViewModel.hashCode()}")
                    true
                }
                else -> false
            }
        }
    }
}