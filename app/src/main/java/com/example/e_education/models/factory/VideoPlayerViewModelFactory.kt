package com.example.e_education.models.factory

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.e_education.models.VideoPlayerViewModel

@Suppress("UNCHECKED_CAST")
class VideoPlayerViewModelFactory(private val context: Context, private val uri: Uri) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return VideoPlayerViewModel(context, uri) as T
    }
}