package cos.tuk_tuk_driver.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.databinding.ActivityCreatePasswordBinding
import cos.tuk_tuk_driver.utils.Prefs
import cos.tuk_tuk_driver.utils.Validation.isValidPassword

class CreatePasswordActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreatePasswordBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreatePasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)


        binding.btnNext.setOnClickListener {

            validate()

        }

        binding.ivBack.setOnClickListener {

            finish()
        }


    }

    private fun validate() {

        var Password: String = binding.password.text.toString()
        var ConfirmPassword: String = binding.confirmPassword.text.toString()

        if (Password.isEmpty() || ConfirmPassword.isEmpty()) {

            Comman.makeToast(applicationContext, "Please enter fields")

        } else if (!isValidPassword(Password) || !isValidPassword(ConfirmPassword)) {
            Toast.makeText(this, "Both Password must 6 chracter", Toast.LENGTH_SHORT).show()
        } else if (Password != ConfirmPassword) {
            Toast.makeText(this, "Password & Confirm Password are not same", Toast.LENGTH_SHORT)
                .show()
        } else {

            Prefs.putKey(applicationContext, "DriverPassword", Password)
            Prefs.putKey(applicationContext, "DriverConfirmPassword", ConfirmPassword)

            val intent = Intent(this@CreatePasswordActivity, TermsAndPolicyActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }
}
