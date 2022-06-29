package github.xtvj.streamexoplayer.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import github.xtvj.streamexoplayer.data.MainRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val mainRepository: MainRepository
) : ViewModel() {

    val loading = MutableStateFlow(false)


    suspend fun getRealUrl(room: String): String {
        loading.value = true
        val url = withContext(Dispatchers.IO) {
            mainRepository.getUrl(room)
        }
        loading.value = false
        return url
    }

}