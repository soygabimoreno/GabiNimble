package soy.gabimoreno.gabinimble

import android.app.Application
import com.google.firebase.FirebaseApp
import org.koin.android.ext.koin.androidContext
import org.koin.android.logger.AndroidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import soy.gabimoreno.gabinimble.di.serviceLocator
import soy.gabimoreno.gabinimble.libframework.KLog

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        KLog.launch(BuildConfig.DEBUG)
        initKoin()
        initFirebase()
    }

    private fun initKoin() {
        startKoin {
            if (BuildConfig.DEBUG) logger(AndroidLogger(Level.ERROR))
            androidContext(this@App)
            modules(serviceLocator)
        }
    }

    private fun initFirebase() {
        FirebaseApp.initializeApp(this)
    }
}
