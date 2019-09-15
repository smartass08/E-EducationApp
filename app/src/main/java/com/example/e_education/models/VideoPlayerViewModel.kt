package com.example.e_education.models

import android.content.Context
import android.net.Uri
import android.util.TimeUtils
import androidx.lifecycle.ViewModel
import com.example.e_education.utils.MediaSourceFactory
import com.example.e_education.utils.log
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import java.util.concurrent.TimeUnit


class VideoPlayerViewModel(context: Context) : ViewModel() {
    private var playedDuration: Long = 0 // in ms
    private val mPlayer = ExoPlayerFactory.newSimpleInstance(context)
    private val media = MediaSourceFactory.build(
        Uri.parse("https://archive.org/download/Pbtestfilemp4videotestmp4/video_test_512kb.mp4"),
        context
    )

    init {

        mPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT_WITH_CROPPING
        mPlayer.prepare(media)
    }

    fun getPlayer(): ExoPlayer = mPlayer

    fun pause() {
        playedDuration = mPlayer.currentPosition
        playedDuration.log("Played duration")
        mPlayer.playWhenReady = false
    }

    fun resume() {
        mPlayer.seekTo(playedDuration)
        mPlayer.currentPosition.log("Current Position")
        mPlayer.playWhenReady = true
    }

    fun rewind() {
        mPlayer.seekTo(mPlayer.currentPosition - TimeUnit.SECONDS.toMillis(10))
    }

    fun forward() {
        mPlayer.seekTo(mPlayer.currentPosition + TimeUnit.SECONDS.toMillis(10))
    }
}