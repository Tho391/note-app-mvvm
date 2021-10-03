package com.thomas.apps.noteapp.feature_note.presentation.add_edit_note.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.thomas.apps.noteapp.R
import com.thomas.apps.noteapp.databinding.FragmentAddEditNoteBinding
import com.thomas.apps.noteapp.feature_note.domain.model.Note
import com.thomas.apps.noteapp.feature_note.presentation.add_edit_note.AddEditNoteEvent
import com.thomas.apps.noteapp.feature_note.presentation.add_edit_note.AddEditNoteViewModel
import com.thomas.apps.noteapp.feature_note.presentation.utils.IntentKeys
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class AddEditNoteFragment : Fragment() {

    private lateinit var binding: FragmentAddEditNoteBinding

    private val viewModel: AddEditNoteViewModel by viewModels()

    private val itemColorAdapter by lazy {
        ItemColorAdapter().apply {
            colorSelectListener = object : ItemColorAdapter.ColorSelectListener {
                override fun onSelect(color: ItemColorAdapter.ItemColor) {
                    viewModel.onEvent(AddEditNoteEvent.ChangeColor(color.value))
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
        binding = FragmentAddEditNoteBinding.inflate(layoutInflater)

        setUpRecyclerView()

        setUpTextInput()

        observe()

        return binding.root
    }

    private fun setUpTextInput() {
        with(binding) {
            textInputTitle.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditNoteEvent.EnteredTitle(text.toString()))
            }
            textInputContent.editText?.doOnTextChanged { text, _, _, _ ->
                viewModel.onEvent(AddEditNoteEvent.EnteredContent(text.toString()))
            }
        }
    }

    private fun observe() {
        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AddEditNoteViewModel.UiEvent.ShowSnackbar -> {
                        showErrorSnackbar(event.message)
                    }
                    is AddEditNoteViewModel.UiEvent.SaveNote -> {
                        val navController = findNavController()
                        navController.previousBackStackEntry?.savedStateHandle?.set(
                            IntentKeys.SCROLL_TOP,
                            true
                        )
                        navController.popBackStack()
                    }
                }
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.noteColor.collect {
                val color = requireContext().getColor(it)
                binding.root.setBackgroundColor(color)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.noteTitle.collect {
                binding.textInputTitle.editText?.setText(it.text, TextView.BufferType.EDITABLE)
            }
        }

        lifecycleScope.launchWhenCreated {
            viewModel.noteContent.collect {
                binding.textInputContent.editText?.setText(it.text, TextView.BufferType.EDITABLE)
            }
        }
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }

    private fun setUpRecyclerView() {
        binding.recyclerViewColors.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = itemColorAdapter
        }
        val items = ItemColorAdapter.ItemColor.from(Note.noteColors)
        itemColorAdapter.submitList(items)
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
                R.id.item_save_note -> {
                    viewModel.onEvent(AddEditNoteEvent.SaveNote)
                    true
                }
                else -> false
            }
        }
    }
}