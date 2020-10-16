package soy.gabimoreno.gabinimble.presentation.main.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import androidx.core.content.res.ResourcesCompat
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import org.koin.android.ext.android.inject
import soy.gabimoreno.gabinimble.R
import soy.gabimoreno.gabinimble.coredomain.Song
import soy.gabimoreno.gabinimble.libimageloader.toBitmap
import soy.gabimoreno.gabinimble.libplayer.Player
import soy.gabimoreno.gabinimble.presentation.main.MainActivity

class PlayerService : Service() {

    private var songTitle: String? = null
    private var songDescription: String? = null
    private var songThumbnail: String? = null

    companion object {
        private const val CHANNEL_ID = "CHANNEL_ID"
        private const val NOTIFICATION_ID = 1

        private const val EXTRA_SONG_NAME = "EXTRA_SONG_NAME"
        private const val EXTRA_SONG_DESCRIPTION = "EXTRA_SONG_DESCRIPTION"
        private const val EXTRA_SONG_THUMBNAIL = "EXTRA_SONG_THUMBNAIL"

        fun start(context: Context, song: Song) {
            context.startForegroundService(
                Intent(context, PlayerService::class.java).apply {
                    putExtra(EXTRA_SONG_NAME, song.name)
                    putExtra(EXTRA_SONG_DESCRIPTION, song.description)
                    putExtra(EXTRA_SONG_THUMBNAIL, song.thumbnailUrl)
                })
        }
    }

    private val player: Player by inject()

    private val binder: MediaServiceBinder by lazy { MediaServiceBinder() }

    private lateinit var notificationManager: PlayerNotificationManager

    private val mediaDescriptionAdapter =
        object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: com.google.android.exoplayer2.Player): CharSequence {
                return songTitle ?: "Default title"
            }

            override fun getCurrentContentText(player: com.google.android.exoplayer2.Player): CharSequence? {
                return songDescription
            }

            override fun getCurrentLargeIcon(
                player: com.google.android.exoplayer2.Player,
                bitmapCallback: PlayerNotificationManager.BitmapCallback
            ): Bitmap? {
                songThumbnail.toBitmap(applicationContext) { bitmap ->
                    bitmapCallback.onBitmap(bitmap)
                }
                return null
            }

            override fun createCurrentContentIntent(player: com.google.android.exoplayer2.Player): PendingIntent? {
                val intent = Intent(applicationContext, MainActivity::class.java)
                return PendingIntent.getActivity(applicationContext, 0, intent, 0)
            }
        }

    override fun onBind(intent: Intent?): IBinder? {
        return binder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        songTitle = intent?.getStringExtra(EXTRA_SONG_NAME)
        songDescription = intent?.getStringExtra(EXTRA_SONG_DESCRIPTION)
        songThumbnail = intent?.getStringExtra(EXTRA_SONG_THUMBNAIL)

        buildNotification()

        return START_STICKY
    }

    private fun buildNotification() {
        notificationManager = PlayerNotificationManager.createWithNotificationChannel(
            this,
            CHANNEL_ID,
            R.string.app_name,
            ResourcesCompat.ID_NULL,
            NOTIFICATION_ID,
            mediaDescriptionAdapter,
            object : PlayerNotificationManager.NotificationListener {
                override fun onNotificationPosted(
                    notificationId: Int,
                    notification: Notification,
                    ongoing: Boolean
                ) {
                    startForeground(notificationId, notification)
                }

                override fun onNotificationCancelled(
                    notificationId: Int,
                    dismissedByUser: Boolean
                ) {
                    stopSelf()
                }
            }
        )

        notificationManager.setUseNavigationActions(false)
        notificationManager.setFastForwardIncrementMs(0)
        notificationManager.setRewindIncrementMs(0)
        notificationManager.setUseStopAction(false)
        notificationManager.setUseChronometer(false)

        notificationManager.setSmallIcon(R.drawable.ic_notification)

        notificationManager.setPlayer(player.getExoPlayer())
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::notificationManager.isInitialized) {
            notificationManager.setPlayer(null)
        }
    }

    inner class MediaServiceBinder : Binder() {
        val service: PlayerService get() = this@PlayerService
    }
}