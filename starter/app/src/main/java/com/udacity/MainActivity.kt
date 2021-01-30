package com.udacity

import android.app.DownloadManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.udacity.utils.sendNotification
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_main.view.*


class MainActivity : AppCompatActivity() {

    private var downloadID: Long = 0
    private var downloadUrl: String? = null
    private lateinit var fileName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        registerReceiver(
            downloadManagerReceiver,
            IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE)
        )

        custom_button.setOnClickListener {
            if (downloadUrl.isNullOrEmpty()) {
                Toast.makeText(
                    applicationContext,
                    getString(R.string.choose_download_file),
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                custom_button.buttonState = ButtonState.Loading
                download()
            }
        }

        createNotificationChannel(
            CHANNEL_ID,
            getString(R.string.download_channel_name),
            getString(R.string.download_channel_description)
        )
    }


    private val downloadManagerReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val id = intent?.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1)

            val notificationManager = getSystemService(NotificationManager::class.java)
            var status: String? = null
            var downloadStatus: Int? = null

            val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
            val query = id?.let { DownloadManager.Query().setFilterById(it) }
            val cursor = downloadManager.query(query)

            if (cursor.moveToFirst()) {
                downloadStatus = cursor.getInt(cursor.getColumnIndex(DownloadManager.COLUMN_STATUS))
            }

            when (downloadStatus) {
                DownloadManager.STATUS_SUCCESSFUL -> {
                    status = "SUCCESS"
                }
                DownloadManager.STATUS_FAILED -> {
                    status = "FAIL"
                }
            }

            if (context != null) {
                notificationManager.sendNotification(
                    fileName,
                    status,
                    getString(R.string.notification_text_content),
                    getString(R.string.download_channel_name),
                    CHANNEL_ID,
                    context
                )
            }

            custom_button.buttonState = ButtonState.Completed

        }
    }

    private fun download() {
        val request =
            DownloadManager.Request(Uri.parse(downloadUrl))
                .setTitle(getString(R.string.app_name))
                .setDescription(getString(R.string.app_description))
                .setRequiresCharging(false)
                .setAllowedOverMetered(true)
                .setAllowedOverRoaming(true)

        val downloadManager = getSystemService(DOWNLOAD_SERVICE) as DownloadManager
        downloadID =
            downloadManager.enqueue(request)// enqueue puts the download request in the queue.
    }

    companion object {
        private const val CHANNEL_ID = "download_status_channel"
    }

    fun onRadioButtonClicked(view: View) {
        if (view is RadioButton) {
            val checked = view.isChecked

            when (view.getId()) {
                R.id.radio_glide ->
                    if (checked) {
                        fileName = getString(R.string.glide_radiobutton_text)
                        downloadUrl =
                            getString(R.string.glide_url)
                    }
                R.id.radio_loadapp ->
                    if (checked) {
                        fileName = getString(R.string.radio_udacity)
                        downloadUrl =
                            getString(R.string.loadapp_url)
                    }
                R.id.radio_retrofit ->
                    if (checked) {
                        fileName = getString(R.string.radio_retrofit)
                        downloadUrl =
                            getString(R.string.retrofit_url)
                    }
            }
        }
    }

    private fun createNotificationChannel(
        channelId: String, channelName: String,
        channelDescription: String
    ) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                channelName,
                NotificationManager.IMPORTANCE_HIGH
            )
                .apply {
                    setShowBadge(false)
                }

            notificationChannel.description = channelDescription

            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(notificationChannel)

        }

    }

    override fun onDestroy() {
        super.onDestroy()
        unregisterReceiver(downloadManagerReceiver)
    }
}
