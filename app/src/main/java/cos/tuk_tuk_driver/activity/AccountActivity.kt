package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityAccountBinding
import java.lang.Exception

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()

        } catch (Ex: Exception) {

        }

    }

    private fun init() {

        binding.vehilce.setOnClickListener {
            val intent = Intent(this@AccountActivity, VehicleActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
            startActivity(intent)
        }
    }
}
