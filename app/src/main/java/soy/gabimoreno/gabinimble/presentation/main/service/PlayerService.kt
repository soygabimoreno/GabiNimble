package soy.gabimoreno.gabinimble.presentation.main.service

import android.app.Notification
import android.app.NotificationManager
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
import soy.gabimoreno.gabinimble.presentation.main.service.child.PausePlayerService
import soy.gabimoreno.gabinimble.presentation.main.service.child.PlayPlayerService

class PlayerService : Service() {

    companion object {
        const val EXTRA_SONG_NAME = "EXTRA_SONG_NAME"
    }

    private val player: Player by inject()

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val songName = intent?.getStringExtra(EXTRA_SONG_NAME) ?: "Unknown song name"

        val notificationIntent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            notificationIntent,
            0
        )

        val notification: Notification = NotificationCompat.Builder(this, App.CHANNEL_ID)
            .setContentTitle(getString(R.string.app_name))
            .setContentText(songName)
            .setSmallIcon(R.drawable.ic_notification)
            .setPriority(NotificationManager.IMPORTANCE_LOW)
            .setContentIntent(pendingIntent)
            .addAction(R.drawable.exo_icon_play, getString(R.string.play), buildPendingIntent<PlayPlayerService>())
            .addAction(R.drawable.exo_icon_pause, getString(R.string.pause), buildPendingIntent<PausePlayerService>())
//            .addAction(R.drawable.exo_icon_stop, getString(R.string.stop), buildPendingIntent<StopPlayerService>())
            .build()

        startForeground(1, notification)
        return START_STICKY
    }

    private inline fun <reified T : Service> buildPendingIntent(): PendingIntent? = PendingIntent.getService(
        this,
        0,
        Intent(this, T::class.java),
        0
    )

    override fun onDestroy() {
        player.stop()
        super.onDestroy()
    }
}
