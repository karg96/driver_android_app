package cos.tuk_tuk_driver

import android.app.Application
import android.content.Context

class DriverApp : Application() {


    override fun onCreate() {
        super.onCreate()
        context = this
    }

    companion object {
        var context: Context? = null
    }

}