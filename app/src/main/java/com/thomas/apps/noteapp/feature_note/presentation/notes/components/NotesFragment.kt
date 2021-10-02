package com.thomas.apps.noteapp.feature_note.presentation.notes.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
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

    private val viewModel: NotesViewModel by viewModels()

    private val noteItemAdapter by lazy {
        NoteItemAdapter().apply {
            deleteClickListener = object : NoteItemAdapter.DeleteClickListener {
                override fun onDelete(note: Note) {
                    viewModel.onEvent(NotesEvent.DeleteNote(note))
                    showDeleteSnackbar()
                }
            }
            itemClickListener = object : NoteItemAdapter.ItemClickListener {
                override fun onClick(note: Note) {
                    val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(
                        noteId = note.id ?: -1,
                        noteColor = note.color
                    )
                    findNavController().navigate(action)
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolbar()
    }

    private fun setUpToolbar() {
        val navController = findNavController()
        val appBarConfiguration = AppBarConfiguration(navController.graph)

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_sort -> {
                    viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    true
                }
                else -> false
            }
        }
    }

    private fun setUpFab() {
        binding.fabAddNote.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(
                noteId = -1,
                noteColor = -1
            )
            findNavController().navigate(action)
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
            viewModel.state.collect {
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
                val noteOderType = viewModel.state.value.noteOrder.orderType
                when (checkedId) {
                    R.id.radio_title -> viewModel.onEvent(
                        NotesEvent.Order(NoteOrder.Title(noteOderType))
                    )
                    R.id.radio_date -> viewModel.onEvent(
                        NotesEvent.Order(NoteOrder.Date(noteOderType))
                    )
                    R.id.radio_color -> viewModel.onEvent(
                        NotesEvent.Order(NoteOrder.Color(noteOderType))
                    )
                }
            }
            radioGroupOrderType.setOnCheckedChangeListener { _, checkedId ->
                val noteOder = viewModel.state.value.noteOrder
                when (checkedId) {
                    R.id.radio_ascending -> viewModel.onEvent(
                        NotesEvent.Order(noteOder.copy(OrderType.Ascending))
                    )
                    R.id.radio_descending -> viewModel.onEvent(
                        NotesEvent.Order(noteOder.copy(OrderType.Descending))
                    )
                }
            }
        }
    }

    private fun showDeleteSnackbar() {
        Snackbar.make(binding.root, R.string.note_deleted, Snackbar.LENGTH_SHORT)
            .setAction(R.string.undo) {
                viewModel.onEvent(NotesEvent.RestoreNote)
            }
            .show()
    }
}