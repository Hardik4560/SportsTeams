package com.hardik.zujudigital.ui.fragments

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import android.widget.VideoView
import androidx.fragment.app.Fragment
import com.hardik.zujudigital.R
import com.hardik.zujudigital.models.matches.Previous

class VideoPlayerFragment : Fragment(R.layout.fragment_video_player) {

    private lateinit var videoView: VideoView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView(view)

        //TODO: Try using navArgs for getting the values.
        var match = arguments?.getSerializable("match") as Previous
        var url = match.highlights

        var uri = Uri.parse(url)
        videoView.setVideoURI(uri)

        var mediaController = MediaController(activity)
        mediaController.apply {
            setAnchorView(videoView)
            setMediaPlayer(videoView)
        }

        videoView.setMediaController(mediaController)
        videoView.start()
    }

    override fun onResume() {
        super.onResume()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
    }

    override fun onPause() {
        super.onPause()
        activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    }

    private fun initView(view: View) {
        videoView = view.findViewById(R.id.videoView)
    }
}