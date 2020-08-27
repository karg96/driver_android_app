package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityVehicleBinding

class VehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVehicleBinding
    private var from: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        from = intent.getStringExtra("from").toString()
        binding.btnAddVehicle.setOnClickListener {
            val intent = Intent(this@VehicleActivity, AddVehicleDetailsActiivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
            intent.putExtra("from", "addNew")
            startActivity(intent)
        }

        binding.close.setOnClickListener {
            if (from.equals("listEmpty")) {
                val intent = Intent(this@VehicleActivity, HomeActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                finish()
            }


        }

    }
}
