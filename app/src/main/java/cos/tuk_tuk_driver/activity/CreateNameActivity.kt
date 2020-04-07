package cos.tuk_tuk_driver.activity


import android.content.Intent
import android.os.Bundle
import android.view.View.OnFocusChangeListener
import androidx.appcompat.app.AppCompatActivity
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.databinding.ActivityCreateNameBinding
import cos.tuk_tuk_driver.utils.Prefs

class CreateNameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCreateNameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCreateNameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {

            validate()

        }

        binding.ivBack.setOnClickListener {

            finish()
        }


        binding.firstName.setOnClickListener {
            binding.firstName.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    binding.firstName.hint = ""
                    binding.lastName.hint = "Last Name"
                }
            }

            binding.firstName.hint = ""
            binding.lastName.hint = "Last Name"
        }
       /* binding.lastName.setOnClickListener {

            binding.firstName.hint = "First Name"
            binding.lastName.hint = ""

        }*/
        binding.lastName.onFocusChangeListener = OnFocusChangeListener { v, hasFocus ->
            if (hasFocus) {
                binding.firstName.hint = "First Name"
                binding.lastName.hint = ""
            }
        }
        /* binding.lastName.setOnClickListener {
             binding.firstName.hint = "First Name"
             binding.lastName.hint = ""
         }*/

    }

    override fun onResume() {
        super.onResume()
    }


    private fun validate() {

        var firstName: String = binding.firstName.text.toString()
        var lastName: String = binding.lastName.text.toString()

        if (firstName.isEmpty() || lastName.isEmpty()) {

            Comman.makeToast(applicationContext, "Please enter fields")

        } else if (firstName.length < 2 || lastName.length < 2) {

            Comman.makeToast(applicationContext, "Please fill minimum 2 letters  in both  fields")

        } else {

            Prefs.putKey(applicationContext, "DriverFirstName", firstName)
            Prefs.putKey(applicationContext, "DriverLastName", lastName)

            val intent = Intent(this@CreateNameActivity, CreatePasswordActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

    }

}
