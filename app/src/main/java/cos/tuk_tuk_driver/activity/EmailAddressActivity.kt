package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityEmailAddressBinding
import cos.tuk_tuk_driver.databinding.ActivitySplashBinding

class EmailAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailAddressBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener{
            val intent = Intent(this@EmailAddressActivity, CreateNameActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
