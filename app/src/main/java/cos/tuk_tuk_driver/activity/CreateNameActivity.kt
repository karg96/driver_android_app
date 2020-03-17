package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityCreateNameBinding
import cos.tuk_tuk_driver.databinding.ActivityEmailAddressBinding

class CreateNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNameBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnNext.setOnClickListener{
            val intent = Intent(this@CreateNameActivity, CreatePasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }
    }
}
