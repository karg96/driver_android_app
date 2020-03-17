package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityAddIdentityCardBinding
import cos.tuk_tuk_driver.databinding.ActivityAddPhotoBinding

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}
