package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityCreatePasswordBinding
import cos.tuk_tuk_driver.databinding.ActivityEmailAddressBinding

class CreatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnNext.setOnClickListener{
            val intent = Intent(this@CreatePasswordActivity, AddDocumentActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }
}
