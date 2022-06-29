package github.xtvj.streamexoplayer.initializer

import android.content.Context
import androidx.startup.Initializer
import github.xtvj.streamexoplayer.utils.initLog

class StartInitializer : Initializer<Unit> {
    override fun create(context: Context) {
        //初始化操作

        //初始化log工具
        initLog()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}