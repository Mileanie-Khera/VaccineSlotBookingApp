package com.study.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.text.TextUtils
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.study.learndagger.CowinHomeActivity
import com.study.learndagger.R
import com.study.retrofit.Constants.NOTIFICATION_CHANNEL_ID
import java.util.*

class MyFireBaseMessagingService : FirebaseMessagingService() {
    private var notificationManager: NotificationManager? = null

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. Note that this is called when the InstanceID token
     * is initially generated so this is where you would retrieve the token.
     */
    override fun onNewToken(token: String) {
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        sendNotification(remoteMessage)
    }

    /**
     * @param message
     */
    private fun sendNotification(message: RemoteMessage) {
        if (!TextUtils.isEmpty(message.notification?.body) && !TextUtils.isEmpty(message.notification?.title)) {
            if (notificationManager == null) {
                notificationManager =
                    getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            }
            commonPushCreate(message)
        }
    }

    private fun commonPushCreate(message: RemoteMessage) {
        val CHANNEL_ID = NOTIFICATION_CHANNEL_ID
        val name: CharSequence = getString(R.string.app_name)
        var importance = 0
        var mChannel: NotificationChannel? = null
        if (Build.VERSION.SDK_INT >= VERSION_CODES.N) {
            importance = NotificationManager.IMPORTANCE_HIGH
        }
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            mChannel = NotificationChannel(CHANNEL_ID, name, importance)
        }
        val intent = Intent(this, CowinHomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP or Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val requestCode = 0
        val pendingIntent =
            PendingIntent.getActivity(this, requestCode, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val noBuilder = NotificationCompat.Builder(this, CHANNEL_ID)
            .setLargeIcon(BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher))
            .setContentTitle(message.notification?.title ?: "Cricket Line Guru")
            .setStyle(NotificationCompat.BigTextStyle().bigText(message.notification?.body))
            .setContentText(message.notification?.body)
            .setDefaults(Notification.DEFAULT_ALL)
            .setAutoCancel(true)
            .setChannelId(CHANNEL_ID)
            .setContentIntent(pendingIntent)
            .setPriority(Notification.PRIORITY_HIGH)
        if (Build.VERSION.SDK_INT >= VERSION_CODES.LOLLIPOP) {
            noBuilder.setSmallIcon(R.mipmap.ic_launcher)
        } else {
            noBuilder.setSmallIcon(R.mipmap.ic_launcher)
        }
        messageAlert(noBuilder, mChannel)
        notificationManager?.notify(10.toString(), Random().nextInt(), noBuilder.build())
    }

    /**
     * @param noBuilder
     * @param mChannel
     */
    private fun messageAlert(
        noBuilder: NotificationCompat.Builder,
        mChannel: NotificationChannel?
    ) {
        val audio =
            (this.applicationContext.getSystemService(Context.AUDIO_SERVICE) as AudioManager)
        when (audio.ringerMode) {

            AudioManager.RINGER_MODE_NORMAL -> {
                if (audio.ringerMode == AudioManager.RINGER_MODE_VIBRATE) {
                    noBuilder.setVibrate(longArrayOf(0, VIBRATOR_DURATION))
                }
                if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
                    notificationManager?.createNotificationChannel(mChannel!!)
                }
            }

            AudioManager.RINGER_MODE_SILENT -> if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
                notificationManager?.createNotificationChannel(mChannel!!)
            }

            AudioManager.RINGER_MODE_VIBRATE -> {
                noBuilder.setVibrate(longArrayOf(0, VIBRATOR_DURATION))
                if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
                    notificationManager?.createNotificationChannel(mChannel!!)
                }
            }
        }
    }

    companion object {
        //Vibrate duration when notification receive.
        private const val VIBRATOR_DURATION: Long = 200
    }
}