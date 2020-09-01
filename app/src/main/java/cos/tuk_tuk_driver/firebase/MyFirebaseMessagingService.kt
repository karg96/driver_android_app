package cos.tuk_tuk_driver.firebase

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.media.RingtoneManager
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.activity.HomeActivity
import cos.tuk_tuk_driver.activity.MainActivity

class MyFirebaseMessagingService : FirebaseMessagingService() {

    val TAG = "FirebaseMessagingService"

    @SuppressLint("LongLogTag")
    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        Log.d(TAG, "Dikir: ${remoteMessage.from}")

        if(remoteMessage.data.isNotEmpty()){
            Log.e("Notif data", remoteMessage.data.get("title"))
        }


        if (remoteMessage.notification != null) {

            showNotification(remoteMessage.notification?.title, remoteMessage.notification?.body)
        }
    }


    private fun showNotification(title: String?, body: String?) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("Intent Data", body)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        val pendingIntent = PendingIntent.getActivity(this, 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT)

        val channelId = "Default"
        val builder = NotificationCompat.Builder(this, channelId)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setContentTitle(title)
            .setContentText(body).setAutoCancel(true)
            .setContentIntent(pendingIntent)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Default channel",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            manager.createNotificationChannel(channel)
        }
        manager.notify(0, builder.build())
    }
}