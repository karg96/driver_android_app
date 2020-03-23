package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.databinding.ActivityCreateNameBinding
import cos.tuk_tuk_driver.utils.Prefs
import cos.tuk_tuk_driver.utils.Validation

class CreateNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {

            validate()

        }

    }

    private fun validate() {

        var firstName: String = binding.firstName.text.toString()
        var lastName: String = binding.lastName.text.toString()

        if (firstName.isEmpty() || lastName.isEmpty()) {

            Comman.makeToast(applicationContext, "Please enter fields")

        } else {

            Prefs.putKey(applicationContext, "DriverFirstName", firstName)
            Prefs.putKey(applicationContext, "DriverLastName", lastName)

            val intent = Intent(this@CreateNameActivity, CreatePasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }


}
