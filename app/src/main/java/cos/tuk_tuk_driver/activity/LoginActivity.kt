package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tuktuk.models.RegisterModal
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityLoginBinding
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val apiInterface = Comman.getApi()
    private var mobileNumber: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            mobileNumber = intent.getStringExtra("mobileNumber")
            init()

            if (mobileNumber != "") {
                binding.edtPhoneNumber.setText(mobileNumber)
                binding.edtPhoneNumber.isCursorVisible = false

            }

            binding.edtPhoneNumber.setOnClickListener {
                binding.edtPhoneNumber.onFocusChangeListener =
                    View.OnFocusChangeListener { v, hasFocus ->
                        if (hasFocus) {
                            binding.edtPhoneNumber.hint = ""
                            binding.edtPasswordNumber.hint = "Enter Password"
                        }
                    }

                binding.edtPhoneNumber.hint = ""
                binding.edtPasswordNumber.hint = "Enter Password"
            }

            /* binding.edtPasswordNumber.setOnClickListener {
                 binding.edtPhoneNumber.hint = "Enter Number"
                 binding.edtPasswordNumber.hint = ""

             }*/

            binding.edtPasswordNumber.onFocusChangeListener =
                View.OnFocusChangeListener { v, hasFocus ->
                    if (hasFocus) {
                        binding.edtPhoneNumber.hint = "Enter Number"
                        binding.edtPasswordNumber.hint = ""
                    }
                }


        } catch (Ex: Exception) {

        }

    }

    private fun init() {

        /* binding.forgotTitle.setOnClickListener {

             val intent = Intent(this@LoginActivity, CreatePasswordActivity::class.java)
             intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
             startActivity(intent)
         }*/

        binding.btnLogin.setOnClickListener {

            /*val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)*/
            validate()
        }

        binding.signUp.setOnClickListener {

            val intent = Intent(this@LoginActivity, GetOtpActivity::class.java)
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("from", "login")

            startActivity(intent)

        }

    }

    private fun validate() {

        var mobileNumber: String = binding.edtPhoneNumber.text.toString()
        var password: String = binding.edtPasswordNumber.text.toString()

        if (mobileNumber.isEmpty()) {

            Comman.makeToast(applicationContext, "Please enter mobile number")

        } else if (password.isEmpty()) {

            Comman.makeToast(applicationContext, "Please enter Password")

        } else if (mobileNumber.length != 10) {
            Comman.makeToast(applicationContext, "Enter valid mobile number")

//            Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()
        } else {

            doLogin(mobileNumber, password)
        }

    }

    private fun doLogin(mobileNumber: String, password: String) {

        try {

            val pd = ProgressDialog(this)
            pd.setMessage("Please wait....")
            pd.setCancelable(false)
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            pd.show()

            apiInterface!!.Login(mobileNumber, password, "", "")
                .enqueue(object : Callback<RegisterModal> {
                    override fun onFailure(call: Call<RegisterModal>, t: Throwable) {
                        pd.dismiss()
                        Comman.makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<RegisterModal>,
                        response: Response<RegisterModal>
                    ) {
                        pd.dismiss()
                        if (response.code() == 200) {
                            if (response.body()!!.status) {
                                Prefs.putKey(applicationContext, "isLogin", "true")

                                val intent = Intent(
                                    applicationContext,
                                    HomeActivity::class.java
                                ) //not application context
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                            } else {
                                Comman.makeToast(
                                    applicationContext,
                                    getString(R.string.error_login)
                                )
                            }

                        } else if (response.code() == 401) {
                            Comman.makeToast(
                                applicationContext,
                                response.body()!!.error.get(0).invalid
                            )

                        } else {
                            Comman.makeToast(applicationContext, "Please try again later")

                        }

                    }

                })

        } catch (Ex: java.lang.Exception) {

        }

    }


}
