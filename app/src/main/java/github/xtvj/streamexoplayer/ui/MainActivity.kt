package github.xtvj.streamexoplayer.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import dagger.hilt.EntryPoint
import dagger.hilt.android.AndroidEntryPoint
import github.xtvj.baselibrary.ToastUtils.toast
import github.xtvj.streamexoplayer.R
import github.xtvj.streamexoplayer.databinding.ActivityMainBinding
import github.xtvj.streamexoplayer.ui.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.button.setOnClickListener {
            lifecycleScope.launch {
                val url = viewModel.getRealUrl(binding.etRoom.text.toString())
                if (url.isNotBlank()){
                    startActivity(Intent(this@MainActivity,VideoPlayerActivity::class.java).apply {
                        putExtra("URL",url)
                    })
                }else{
                    applicationContext.toast("网址为空！")
                }
            }
        }

        lifecycleScope.launch {
            viewModel.loading.collectLatest {
                binding.pgbLoading.visibility = if (it) View.VISIBLE else View.INVISIBLE
            }
        }

    }
}