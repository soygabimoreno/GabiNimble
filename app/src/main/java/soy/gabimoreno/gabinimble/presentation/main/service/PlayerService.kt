package soy.gabimoreno.gabinimble.presentation.main.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import org.koin.android.ext.android.inject
import soy.gabimoreno.gabinimble.App
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.libplayer.Player
import soy.gabimoreno.gabinimble.presentation.main.MainActivity

class PlayerService : Service() {

    companion object {
        const val EXTRA_TEXT = "EXTRA_TEXT"
    }

    private val player: Player by inject()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val text = intent?.getStringExtra(EXTRA_TEXT) ?: "Unknown Text"

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            0
        )
        val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setContentTitle("AudioClean Service")
            .setContentText(text)
            .setSmallIcon(R.drawable.ic_gabi_nimble_logo__2_)
            .setContentIntent(pendingIntent)
            .build()
        startForeground(1, notification)

//        player.play()

        return START_STICKY
    }

    override fun onDestroy() {
        player.release()
        super.onDestroy()
    }
}
