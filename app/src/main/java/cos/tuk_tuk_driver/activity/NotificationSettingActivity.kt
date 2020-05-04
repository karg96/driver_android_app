package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityNotificationBinding
import cos.tuk_tuk_driver.databinding.ActivityNotificationSettingBinding

class NotificationSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNotificationSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotificationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.ivBack.setOnClickListener {
            finish()
        }


    }
}
