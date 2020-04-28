package cos.tuk_tuk_driver.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.adapter.NotificationAdapter
import cos.tuk_tuk_driver.databinding.ActivityNotificationBinding

class NotificationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            init()

        } catch (Ex: Exception) {

        }

    }

    @SuppressLint("WrongConstant")
    private fun init() {

        val adapter = NotificationAdapter(this)

        binding.notificationRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        binding.notificationRecyclerview.adapter = adapter
        adapter.notifyDataSetChanged()

    }


}
