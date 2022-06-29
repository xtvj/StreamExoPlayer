package github.xtvj.streamexoplayer.utils

import github.xtvj.streamexoplayer.BuildConfig
import timber.log.Timber

//初始化log工具
fun initLog() {
    Timber.plant(Timber.DebugTree())
}

fun log(message: String) {
    if (BuildConfig.DEBUG) {
        Timber.d(message)
    }
}

fun log(message: String, vararg args: Any?) {
    if (BuildConfig.DEBUG) {
        Timber.d(message, args)
    }
}

