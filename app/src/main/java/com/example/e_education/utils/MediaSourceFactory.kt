package com.example.e_education.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.DynamicConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.dash.DashMediaSource
import com.google.android.exoplayer2.source.dash.DefaultDashChunkSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory
import com.google.android.exoplayer2.util.Util

object MediaSourceFactory {
    private val Tag = "MediaSourceFactory"
    fun build(uri: Uri, context: Context): MediaSource {
        val lastPath = uri.lastPathSegment ?: ""
        val userAgent = Util.getUserAgent(context, context.applicationInfo.name)
        val defaultDataSourceFactory = DefaultHttpDataSourceFactory(userAgent)
        return if (lastPath.contains("mp3") || lastPath.contains("mp4")) {
            Log.d(Tag, "mp3||mp4")
            ProgressiveMediaSource.Factory(defaultDataSourceFactory)
                .createMediaSource(uri)
        } else {
            Log.d(Tag, "Others")
            val dashChunkSourceFactory = DefaultDashChunkSource.Factory(defaultDataSourceFactory)

            DashMediaSource.Factory(dashChunkSourceFactory, defaultDataSourceFactory)
                .createMediaSource(uri)
        }
    }

    fun build(uriList: Array<Uri>, context: Context): MediaSource {
        val playlistMediaSource = ConcatenatingMediaSource()
        uriList.forEach { playlistMediaSource.addMediaSource(build(it, context)) }
        return playlistMediaSource
    }

}
