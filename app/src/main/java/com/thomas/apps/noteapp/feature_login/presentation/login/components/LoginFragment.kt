package com.thomas.apps.noteapp.feature_login.presentation.login.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.thomas.apps.noteapp.databinding.FragmentLoginBinding
import com.thomas.apps.noteapp.feature_login.presentation.login.LoginEvent
import com.thomas.apps.noteapp.feature_login.presentation.login.LoginViewModel
import com.thomas.apps.noteapp.utils.view.ViewUtils.showIf
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import timber.log.Timber

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding

    private val viewModel: LoginViewModel by viewModels()

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
        binding = FragmentLoginBinding.inflate(layoutInflater)

        binding.lifecycleOwner = viewLifecycleOwner
        binding.viewModel = viewModel

        setUpButton()

        observe()

        return binding.root
    }

    private fun setUpButton() {
        binding.buttonLogin.setOnClickListener {
            viewModel.onEvent(LoginEvent.Login)
        }
    }

    private fun observe() {
        lifecycleScope.launchWhenCreated {
            viewModel.eventFlow.collect { uiEvent ->
                when (uiEvent) {
                    is LoginViewModel.UIEvent.LoginSuccess -> {
                        val action = LoginFragmentDirections.actionLoginFragmentToNotesFragment()
                        findNavController().navigate(action)
                    }
                    is LoginViewModel.UIEvent.ShowSnackbar -> {
                        showErrorSnackbar(uiEvent.message)
                    }
                    is LoginViewModel.UIEvent.LoggingIn -> {
                        Timber.i("show loading ${uiEvent.isLoggingIn}")
                        binding.loading.showIf(uiEvent.isLoggingIn)
                        binding.textInputLayoutUsername.isEnabled = uiEvent.isLoggingIn.not()
                        binding.textInputLayoutPassword.isEnabled = uiEvent.isLoggingIn.not()
                        binding.buttonLogin.isEnabled = uiEvent.isLoggingIn.not()
                    }
                }
            }
        }
    }

    private fun showErrorSnackbar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}