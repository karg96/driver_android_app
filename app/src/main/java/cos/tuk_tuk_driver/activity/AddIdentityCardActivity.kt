package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityAddDocumentBinding
import cos.tuk_tuk_driver.databinding.ActivityAddIdentityCardBinding

class AddIdentityCardActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddIdentityCardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding= ActivityAddIdentityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
    }

    private fun init() {
        binding.ivBack.setOnClickListener {
            finish()
        }
    }
}
