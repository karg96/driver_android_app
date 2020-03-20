package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityAddDrivingLicenseBinding

class AddDrivingLicenseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDrivingLicenseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDrivingLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {

        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}
