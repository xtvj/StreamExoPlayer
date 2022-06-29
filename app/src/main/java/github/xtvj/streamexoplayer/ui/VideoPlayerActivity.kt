package github.xtvj.streamexoplayer.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import dagger.hilt.android.AndroidEntryPoint
import github.xtvj.streamexoplayer.databinding.ActivityVideoplayerBinding

@AndroidEntryPoint
class VideoPlayerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVideoplayerBinding
    private lateinit var exoPlayer: ExoPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVideoplayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        STREAM_URL = intent.getStringExtra("URL")!!
        initializePlayer()

        window.decorView.apply {
            systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
        }
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
        var STREAM_URL =
            "https://hw.flv.huya.com/src/1394575534-1394575534-5989656310331736064-2789274524-10057-A-0-1-imgplus.flv?wsSecret=e2e768d5f30c09ae01a6f0834f17decb&wsTime=62bc0f3e&u=0&seqid=16564054381903460&fm=RFdxOEJjSjNoNkRKdDZUWV8kMF8kMV8kMl8kMw%3D%3D&ctype=tars_mobile&txyp=o%3Aw1%3B&fs=bgct&&sphdcdn=al_7-tx_3-js_3-ws_7-bd_2-hw_2&sphdDC=huya&sphd=264_*-265_*&t=103"
    }

}