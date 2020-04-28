package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityLocationSettingBinding
import cos.tuk_tuk_driver.databinding.ActivityNotificationSettingBinding

class LocationSettingActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLocationSettingBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLocationSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }
}
