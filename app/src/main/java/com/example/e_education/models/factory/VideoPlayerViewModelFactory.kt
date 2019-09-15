package com.example.e_education.models.factory

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_education.models.VideoPlayerViewModel

@Suppress("UNCHECKED_CAST")
class VideoPlayerViewModelFactory(private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideoPlayerViewModel(context) as T
    }
}