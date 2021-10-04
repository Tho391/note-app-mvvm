package com.thomas.apps.noteapp.feature_login.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.thomas.apps.noteapp.databinding.FragmentSplashBinding
import com.thomas.apps.noteapp.feature_login.presentation.utils.DataStore.getToken
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class SplashFragment : Fragment() {

    private lateinit var binding: FragmentSplashBinding

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
        binding = FragmentSplashBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        checkLogin()
    }

    private fun checkLogin() {
        lifecycleScope.launch {
            context?.getToken()?.collect { token ->
                delay(1000)
                val action = if (token.isEmpty()) {
                    SplashFragmentDirections.actionSplashFragmentToLoginFragment()
                } else {
                    SplashFragmentDirections.actionSplashFragmentToNotesFragment()
                }
                findNavController().navigate(action)
            }
        }
    }
}