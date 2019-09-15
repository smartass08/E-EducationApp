package com.example.e_education.models

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.e_education.utils.MediaSourceFactory
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory


class VideoPlayerViewModel(private val context: Context) : ViewModel() {
    private val playedDuration: Long = 0 // in ms
    private val mPlayer = ExoPlayerFactory.newSimpleInstance(context)

    fun getPlayer(): ExoPlayer = mPlayer

    fun prepareExoPlayer() {
        val media = MediaSourceFactory.build(
            Uri.parse("https://archive.org/download/Pbtestfilemp4videotestmp4/video_test_512kb.mp4"),
            context
        )
        mPlayer.playWhenReady = true
        mPlayer.seekTo(playedDuration)
        mPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        mPlayer.prepare(media)
    }
}