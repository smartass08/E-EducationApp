package com.example.e_education.models.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_education.models.AuthenticationViewModel
import com.example.e_education.models.AuthenticationViewModel.AuthListener

@Suppress("UNCHECKED_CAST")
class AuthViewModelFactory(private val listener: AuthListener) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(p0: Class<T>): T {
        return AuthenticationViewModel(listener) as T
    }
}