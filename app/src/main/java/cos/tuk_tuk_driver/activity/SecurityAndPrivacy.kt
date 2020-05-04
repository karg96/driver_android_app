package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide.init
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityNotificationBinding
import cos.tuk_tuk_driver.databinding.ActivitySecurityAndPrivacyBinding

class SecurityAndPrivacy : AppCompatActivity() {
    private lateinit var binding: ActivitySecurityAndPrivacyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySecurityAndPrivacyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()

        } catch (Ex: Exception) {

        }

    }

    private fun init() {

        binding.close.setOnClickListener {
            finish()

        }

        binding.locationSetting.setOnClickListener {
            val intent = Intent(this@SecurityAndPrivacy, LocationSettingActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
            startActivity(intent)

        }
        binding.notificationSetting.setOnClickListener {
            val intent = Intent(this@SecurityAndPrivacy, NotificationSettingActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
            startActivity(intent)
        }

    }
}
