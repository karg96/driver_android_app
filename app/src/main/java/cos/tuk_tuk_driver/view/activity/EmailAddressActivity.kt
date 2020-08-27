package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

import cos.tuk_tuk_driver.databinding.ActivityEmailAddressBinding
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Prefs
import cos.tuk_tuk_driver.utils.Validation.isValidEmail

class EmailAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEmailAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {

            validateEmail()

        }

       /* binding.edtEmail.onFocusChangeListener = View.OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.edtEmail.hint = ""
            }
        }*/

        binding.ivBack.setOnClickListener {

            finish()
        }
        binding.edtEmail.setOnClickListener {

            binding.edtEmail.hint = ""
        }
    }


    override fun onResume() {
        super.onResume()
    }

    private fun validateEmail() {

        var email: String = binding.edtEmail.text.toString()

        if (email.isEmpty()) {

            Comman.makeToast(applicationContext, "Please enter email")

        } else if (!isValidEmail(email)) {

            Comman.makeToast(applicationContext, "Please enter valid email")

        } else {

            Prefs.putKey(applicationContext, "DriverEmail", email)
            val intent = Intent(this@EmailAddressActivity, CreateNameActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

        }

    }
}
