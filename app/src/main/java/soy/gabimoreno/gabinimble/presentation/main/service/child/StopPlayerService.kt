package soy.gabimoreno.gabinimble.presentation.main.service.child

import android.app.Service
import android.content.Intent
import android.os.IBinder
import org.koin.android.ext.android.inject
import soy.gabimoreno.gabinimble.libplayer.Player

class StopPlayerService : Service() {

    private val player: Player by inject()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        player.stop()
        return START_STICKY
    }
}
