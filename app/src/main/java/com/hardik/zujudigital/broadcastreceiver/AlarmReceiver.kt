package com.hardik.zujudigital.broadcastreceiver

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.hardik.zujudigital.R
import com.hardik.zujudigital.ui.MainActivity


class AlarmReceiver : BroadcastReceiver() {
    private val TAG = AlarmReceiver::class.java.simpleName

    private var notificationManager: NotificationManagerCompat? = null

    private fun createNotificationChannel(context: Context) {
        val mNotificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager?

        // The id of the channel.
        val id = "match_notifications"

        // The user-visible name of the channel.
        val name: CharSequence = "Match Notifications"

        // The user-visible description of the channel.
        val description = "Channel to show the notification for match events"

        val importance = NotificationManager.IMPORTANCE_LOW

        val mChannel = NotificationChannel(id, name, importance)

        // Configure the notification channel.
        mChannel.description = description

        mChannel.enableLights(true)

        // Sets the notification light color for notifications posted to this
        // channel, if the device supports this feature.
        mChannel.lightColor = Color.RED

        mChannel.enableVibration(true)
        mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)

        mNotificationManager!!.createNotificationChannel(mChannel)
    }

    override fun onReceive(p0: Context, p1: Intent) {
        Log.d(TAG, "On Broadcast Received")

        val matchDescription = p1.getStringExtra(
            "description"
        )

        val matchDate = p1.getStringExtra(
            "date"
        )

        createNotificationChannel(p0)

        val tapResultIntent = Intent(p0, MainActivity::class.java)
        tapResultIntent.flags = Intent.FLAG_ACTIVITY_SINGLE_TOP
        val pendingIntent: PendingIntent =
            PendingIntent.getActivity(
                p0,
                0,
                tapResultIntent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )

        var channelId = "match_notifications"

        val notification = p0?.let {
            NotificationCompat.Builder(it, channelId)
                .setContentTitle("The game is on!")
                .setContentText(matchDescription)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setContentIntent(pendingIntent)
                .build()
        }

        Log.d(TAG, "Match - $matchDescription")
        notificationManager = p0.let { NotificationManagerCompat.from(it) }
        notification?.let {
            matchDate.let { it1 ->
                notificationManager?.notify(
                    it1.hashCode(),
                    it
                )
            }
        }
    }
}