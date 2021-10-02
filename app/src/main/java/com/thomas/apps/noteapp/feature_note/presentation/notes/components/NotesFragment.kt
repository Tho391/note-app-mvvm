package com.thomas.apps.noteapp.feature_note.presentation.notes.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thomas.apps.noteapp.R
import com.thomas.apps.noteapp.databinding.FragmentNotesBinding
import com.thomas.apps.noteapp.feature_note.domain.model.Note
import com.thomas.apps.noteapp.feature_note.domain.utils.NoteOrder
import com.thomas.apps.noteapp.feature_note.domain.utils.OrderType
import com.thomas.apps.noteapp.feature_note.presentation.notes.NotesEvent
import com.thomas.apps.noteapp.feature_note.presentation.notes.NotesState
import com.thomas.apps.noteapp.feature_note.presentation.notes.NotesViewModel
import com.thomas.apps.noteapp.utils.view.ViewUtils.viewGone
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class NotesFragment : Fragment() {

    private lateinit var binding: FragmentNotesBinding

    private val notesViewModel: NotesViewModel by activityViewModels()

    private val noteItemAdapter by lazy {
        NoteItemAdapter().apply {
            deleteClickListener = object : NoteItemAdapter.DeleteClickListener {
                override fun onDelete(item: Note) {
                    notesViewModel.onEvent(NotesEvent.DeleteNote(item))
                    showDeleteSnackbar()
                }
            }
            itemClickListener = object : NoteItemAdapter.ItemClickListener {
                override fun onClick(item: Note) {

                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotesBinding.inflate(layoutInflater, container, false)

        setUpRadioButton()

        setUpRecyclerView()

        setUpFab()

        observe()

        return binding.root
    }

    private fun setUpFab() {
        binding.fabAddNote.setOnClickListener {
            Timber.i("fab click ${notesViewModel.hashCode()}")
        }
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewNotes.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = noteItemAdapter
        }
    }

    private fun observe() {
        lifecycleScope.launchWhenCreated {
            notesViewModel.state.collect {
                updateUI(it)
            }
        }
    }

    private fun updateUI(state: NotesState) {
        Timber.i("state $state")

        noteItemAdapter.submitList(state.notes)

        binding.radioGroupOrderProperty.viewGone(state.isOrderSectionVisible.not())
        binding.radioGroupOrderType.viewGone(state.isOrderSectionVisible.not())

        val checkedPropertyId = when (state.noteOrder) {
            is NoteOrder.Title -> R.id.radio_title
            is NoteOrder.Date -> R.id.radio_date
            is NoteOrder.Color -> R.id.radio_color
        }
        binding.radioGroupOrderProperty.check(checkedPropertyId)

        val checkedTypeId = when (state.noteOrder.orderType) {
            is OrderType.Ascending -> R.id.radio_ascending
            is OrderType.Descending -> R.id.radio_descending
        }
        binding.radioGroupOrderType.check(checkedTypeId)
    }

    private fun setUpRadioButton() {
        with(binding) {
            radioGroupOrderProperty.setOnCheckedChangeListener { _, checkedId ->
                val noteOderType = notesViewModel.state.value.noteOrder.orderType
                when (checkedId) {
                    R.id.radio_title -> notesViewModel.onEvent(
                        NotesEvent.Order(NoteOrder.Title(noteOderType))
                    )
                    R.id.radio_date -> notesViewModel.onEvent(
                        NotesEvent.Order(NoteOrder.Date(noteOderType))
                    )
                    R.id.radio_color -> notesViewModel.onEvent(
                        NotesEvent.Order(NoteOrder.Color(noteOderType))
                    )
                }
            }
            radioGroupOrderType.setOnCheckedChangeListener { _, checkedId ->
                val noteOder = notesViewModel.state.value.noteOrder
                when (checkedId) {
                    R.id.radio_ascending -> notesViewModel.onEvent(
                        NotesEvent.Order(noteOder.copy(OrderType.Ascending))
                    )
                    R.id.radio_descending -> notesViewModel.onEvent(
                        NotesEvent.Order(noteOder.copy(OrderType.Descending))
                    )
                }
            }
        }
    }

    private fun showDeleteSnackbar() {
        Snackbar.make(binding.root, R.string.note_deleted, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) {
                notesViewModel.onEvent(NotesEvent.RestoreNote)
            }
            .show()
    }
}