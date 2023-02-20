package com.hardik.zujudigital.ui.fragments

import android.content.pm.ActivityInfo
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.MediaController
import androidx.fragment.app.Fragment
import com.hardik.zujudigital.R
import com.hardik.zujudigital.models.matches.Previous
import kotlinx.android.synthetic.main.fragment_video_player.*

class VideoPlayerFragment : Fragment(R.layout.fragment_video_player) {

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
        //Initialize your views here.
    }
}