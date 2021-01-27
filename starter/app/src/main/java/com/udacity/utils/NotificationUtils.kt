package com.udacity.utils

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import androidx.core.app.NotificationCompat
import com.udacity.DetailActivity
import com.udacity.MainActivity
import com.udacity.R

private val NOTIFICATION_ID = 0
private const val CHECK_STATUS_ACTION = "Check Status"

fun NotificationManager.sendNotification(
    downloadStatus: String,
    notificationTitle: String,
    channelId: String,
    applicationContext: Context
) {
    val contentIntent = Intent(applicationContext, MainActivity::class.java)
    val contentPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        contentIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )


    val statusIntent = Intent(applicationContext, DetailActivity::class.java)
    val statusPendingIntent = PendingIntent.getActivity(
        applicationContext,
        NOTIFICATION_ID,
        statusIntent,
        PendingIntent.FLAG_UPDATE_CURRENT
    )

    val builder = NotificationCompat.Builder(applicationContext, channelId)
        .setSmallIcon(R.drawable.ic_cloud_download_black_24dp)
        .setContentTitle(notificationTitle)
        .setContentText(downloadStatus)
        .setContentIntent(contentPendingIntent)
        .setAutoCancel(true)
        .addAction(
            R.drawable.ic_assistant_black_24dp,
            CHECK_STATUS_ACTION,
            statusPendingIntent
        )
        .setPriority(NotificationCompat.PRIORITY_HIGH)

    notify(NOTIFICATION_ID, builder.build())
}