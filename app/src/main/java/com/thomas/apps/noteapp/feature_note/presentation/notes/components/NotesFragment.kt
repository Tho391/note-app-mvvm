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
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import com.thomas.apps.noteapp.R
import com.thomas.apps.noteapp.databinding.FragmentNotesBinding
import com.thomas.apps.noteapp.feature_note.domain.model.Note
import com.thomas.apps.noteapp.feature_note.domain.utils.NoteOrder
import com.thomas.apps.noteapp.feature_note.domain.utils.OrderType
import com.thomas.apps.noteapp.feature_note.presentation.notes.NotesEvent
import com.thomas.apps.noteapp.feature_note.presentation.notes.NotesState
import com.thomas.apps.noteapp.feature_note.presentation.notes.NotesViewModel
import com.thomas.apps.noteapp.feature_note.presentation.utils.IntentKeys.SCROLL_TOP
import com.thomas.apps.noteapp.feature_note.presentation.utils.SpacingItemDecorator
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("onCreate ${this.hashCode()}")
    }

    override fun onDestroy() {
        Timber.i("onDestroy ${this.hashCode()}")
        super.onDestroy()
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
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.splashFragment, R.id.loginFragment, R.id.notesFragment))

        binding.toolbar.setupWithNavController(navController, appBarConfiguration)
        binding.toolbar.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.item_sort -> {
                    viewModel.onEvent(NotesEvent.ToggleOrderSection)
                    true
                }
                R.id.item_sign_out -> {
                    showSignOutDialog()
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
            addItemDecoration(SpacingItemDecorator.DEFAULT)
            setOnScrollChangeListener { _, _, scrollY, _, oldScrollY ->
                when {
                    scrollY > oldScrollY -> {
                        binding.fabAddNote.hide()
                    }
                    scrollY < oldScrollY -> {
                        binding.fabAddNote.show()
                    }
                }
            }
        }
    }

    private fun observe() {
        lifecycleScope.launchWhenCreated {
            viewModel.state.collect {
                updateUI(it)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is NotesViewModel.UIEvent.SignOut -> {
                        val action = NotesFragmentDirections.actionNotesFragmentToSplashFragment()
                        findNavController().navigate(action)
                    }
                }
            }
        }

        findNavController().currentBackStackEntry?.savedStateHandle?.getLiveData<Boolean>(SCROLL_TOP)
            ?.observe(viewLifecycleOwner) {
                binding.recyclerViewNotes.apply {
                    if (it) post { smoothScrollToPosition(0) }
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
        Snackbar.make(binding.root, R.string.note_deleted, Snackbar.LENGTH_LONG)
            .setAction(R.string.undo) {
                viewModel.onEvent(NotesEvent.RestoreNote)
            }
            .setAnchorView(binding.fabAddNote)
            .show()
    }

    private fun showSignOutDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(R.string.logout)
            .setMessage(R.string.sign_out_message)
            .setPositiveButton(R.string.ok) { _, _ ->
                viewModel.onEvent(NotesEvent.SignOut)
            }
            .setNegativeButton(R.string.cancel, null)
            .show()
    }
}