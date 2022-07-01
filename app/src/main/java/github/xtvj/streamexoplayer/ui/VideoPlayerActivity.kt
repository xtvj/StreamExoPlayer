package github.xtvj.streamexoplayer.ui

import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.hardware.SensorManager
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.ui.StyledPlayerView
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dagger.hilt.android.AndroidEntryPoint
import github.xtvj.streamexoplayer.R
import github.xtvj.streamexoplayer.databinding.ActivityVideoplayerBinding
import github.xtvj.streamexoplayer.ui.viewmodel.VideoPlayerViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoplayerBinding
    private lateinit var exoPlayer: ExoPlayer
    private val viewModel by viewModels<VideoPlayerViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        STREAM_URL = intent.getStringExtra("URL")!!
        initializePlayer()

        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
        initView()
    }

    private fun initView() {
        binding.btZoom.setOnClickListener {
            requestedOrientation = if (viewModel.lockSensor.value) {
                viewModel.lockSensor.value = false
                ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR
            } else {
                viewModel.lockSensor.value = true
                ActivityInfo.SCREEN_ORIENTATION_LOCKED
            }
        }
        lifecycleScope.launch {
            viewModel.lockSensor.collectLatest {
                binding.btZoom.setImageResource(if (it) R.drawable.ic_lock else R.drawable.ic_un_lock)
            }
        }
        binding.videoPlayer.setControllerVisibilityListener(StyledPlayerView.ControllerVisibilityListener {
            binding.btZoom.visibility = it
        })
    }


    override fun onDestroy() {
        super.onDestroy()
        exoPlayer.release()
    }

    private fun initializePlayer() {

        val mediaSource = if (STREAM_URL.endsWith("m3u8")) {
            HlsMediaSource.Factory(DefaultHttpDataSource.Factory())
                .createMediaSource(MediaItem.fromUri(STREAM_URL))
        } else {
            ProgressiveMediaSource.Factory(DefaultDataSource.Factory(this))
                .createMediaSource(MediaItem.fromUri(STREAM_URL))
        }
        val mediaSourceFactory = DefaultMediaSourceFactory(this)

        exoPlayer = ExoPlayer.Builder(this)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

        exoPlayer.setMediaSource(mediaSource, false)

        exoPlayer.playWhenReady = true
        binding.videoPlayer.player = exoPlayer
        binding.videoPlayer.requestFocus()
        exoPlayer.prepare()
    }

    companion object {
        var STREAM_URL = ""
    }

}