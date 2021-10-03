package com.thomas.apps.noteapp.feature_note.presentation.add_edit_note.components

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.animation.doOnEnd
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

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setUpRecyclerView()

        observe()

        return binding.root
    }

    private fun observe() {
        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collect { event ->
                when (event) {
                    is AddEditNoteViewModel.UIEvent.ShowSnackbar -> {
                        showErrorSnackbar(event.message)
                    }
                    is AddEditNoteViewModel.UIEvent.SaveNote -> {
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
                val newColor = requireContext().getColor(it)

                val currentColor = (binding.constraintLayout.background as? ColorDrawable)?.color
                if (currentColor != null) {
                    ValueAnimator.ofObject(ArgbEvaluator(), currentColor, newColor).run {
                        duration = 250
                        addUpdateListener {
                            binding.constraintLayout.setBackgroundColor(animatedValue as Int)
                        }
                        doOnEnd {
                            binding.constraintLayout.setBackgroundColor(newColor)
                        }
                        start()
                    }
                } else {
                    binding.constraintLayout.setBackgroundColor(newColor)
                }
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
        val appBarConfiguration =
            AppBarConfiguration(setOf(R.id.splashFragment, R.id.loginFragment, R.id.notesFragment))

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