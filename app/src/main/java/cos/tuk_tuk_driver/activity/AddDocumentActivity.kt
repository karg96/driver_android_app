package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityAddDocumentBinding
import cos.tuk_tuk_driver.databinding.ActivityCreateNameBinding

class AddDocumentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDocumentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()

    }

    private fun init() {


        binding.cardLicense.setOnClickListener {

            val intent = Intent(this@AddDocumentActivity, AddDrivingLicenseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.cardRegDocument.setOnClickListener {

            val intent = Intent(this@AddDocumentActivity, AddVehicleRegistrationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.cardIdentity.setOnClickListener {

            val intent = Intent(this@AddDocumentActivity, AddIdentityCardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.cardPhoto.setOnClickListener {

            val intent = Intent(this@AddDocumentActivity, AddPhotoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }
binding.btnNext.setOnClickListener {

    val intent = Intent(this@AddDocumentActivity, HomeActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

    }
}
