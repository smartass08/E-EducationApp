package com.example.e_education.models.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_education.models.ChaptersViewModel

@Suppress("UNCHECKED_CAST")
class ChaptersViewModelFactory(private val standard: String, private val subject: Int, private val chapter: Int = -1) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChaptersViewModel(standard, subject, chapter) as T
    }

}