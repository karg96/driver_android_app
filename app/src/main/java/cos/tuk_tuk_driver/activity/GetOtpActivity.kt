package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.os.Bundle
import android.content.Intent
import com.tuktuk.models.RegisterModal
import com.tuktuk.utils.BaseActivity
import com.tuktuk.utils.Comman
import com.tuktuk.utils.Comman.makeToast
import com.tuktuk.utils.Constants
import cos.tuk_tuk_driver.databinding.ActivityGetOtpBinding
import kotlinx.android.synthetic.main.activity_get_otp.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GetOtpActivity : BaseActivity() {

    lateinit var binding: ActivityGetOtpBinding
    private val COUNTRY_CODE_ACT = 32
    private var from: String? = ""

    private val apiInterface = Comman.getApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            from = intent.getStringExtra("from")

            init()

            if (from == "login") {

                binding.btnGetOtp.text = "Next"
                binding.question.text = "Don't have an account? "
                binding.action.text = "Sign Up "

                binding.action.setOnClickListener {

                    val intent = Intent(this@GetOtpActivity, GetOtpActivity::class.java)
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                    intent.putExtra("from", "register")
                    startActivity(intent)

                }
            }


        } catch (ex: Exception) {

        }
    }

    private fun init() {
        listener()
    }

    private fun listener() {

        /* binding.tvCountryCode.setOnClickListener {
             startActivityForResult(Intent(this, CountryCodeActivity::class.java), COUNTRY_CODE_ACT)
         }*/

        binding.btnGetOtp.setOnClickListener {

            validate()

        }

        binding.edtPhoneNumber.setOnClickListener {

            binding.edtPhoneNumber.hint = ""
        }

        binding.action.setOnClickListener {

            val intent = Intent(
                applicationContext,
                LoginActivity::class.java
            ) //not application context
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("mobileNumber", "")

            startActivity(intent)
        }
    }

    private fun doMobileLogin(mobileNumber: String, mobileWithSpace: String) {

        try {

            val pd = ProgressDialog(this)
            pd.setMessage("Please wait....")
            pd.setCancelable(false)
            pd.show()

            apiInterface!!.MobileLogin(mobileNumber, "type", "android")
                .enqueue(object : Callback<RegisterModal> {
                    override fun onFailure(call: Call<RegisterModal>, t: Throwable) {
                        pd.dismiss()

                        makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<RegisterModal>,
                        response: Response<RegisterModal>
                    ) {

                        try {

                            pd.dismiss()

                            if (response.code() == 200) {
                                if (response.body()!!.status) {


                                    if (response.body()!!.password) {

                                        val intent = Intent(
                                            applicationContext,
                                            LoginActivity::class.java
                                        ) //not application context
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("mobileNumber", mobileNumber)
                                        startActivity(intent)

                                    } else if (!response.body()!!.password) {

                                        val intent = Intent(
                                            applicationContext,
                                            EnterOtpActivity::class.java
                                        ) //not application context

                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        intent.putExtra("mobile", mobileNumber)
                                        intent.putExtra("mobileWithSpace", mobileWithSpace)
                                        startActivity(intent)

                                    }


                                } else if (!response.body()!!.status) {

                                    makeToast(
                                        applicationContext,
                                        response.body()!!.error.get(0).mobile
                                    )

                                    /*  val intent = Intent(
                                          applicationContext,
                                          EnterOtpActivity::class.java
                                      ) //not application context

                                      intent.flags =
                                          Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                      intent.putExtra("mobile", mobileNumber)
                                      intent.putExtra("mobileWithSpace", mobileWithSpace)
                                      startActivity(intent)*/
                                }
                            } else {

//                                makeToast(applicationContext, response.body()!!.error.get(0).mobile)
                                makeToast(applicationContext, "Please try again later")

                            }


                        } catch (Ex: java.lang.Exception) {

                        }
                    }

                })


        } catch (Ex: Exception) {

        }
    }

    private fun validate() {

        var mobileNumber: String = edt_phone_number.text.toString()
        var countryCode: String = tv_country_code.text.toString()

        if (mobileNumber.isEmpty()) {

            makeToast(applicationContext, "Please enter mobile number")

        } else if (mobileNumber.length != 10) {
            makeToast(applicationContext, "Enter valid mobile number")

//            Toast.makeText(this, "Enter valid mobile number", Toast.LENGTH_SHORT).show()
        } else {
//            registerMobile(mobileNumber, countryCode + " " + mobileNumber)


            if (from == "login") {

                doMobileLogin(mobileNumber, countryCode + " " + mobileNumber)

            } else if (from == "register") {

                registerMobile(mobileNumber, countryCode + " " + mobileNumber)


            }

        }

    }

    private fun registerMobile(mobileNumber: String, mobileWithSpace: String) {

        try {

            val pd = ProgressDialog(this)
            pd.setMessage("Please wait....")
            pd.setCancelable(true)
            pd.show()



            apiInterface!!.RegisterMobiles(mobileNumber, "type", "android")
                .enqueue(object : Callback<RegisterModal> {
                    override fun onFailure(call: Call<RegisterModal>, t: Throwable) {
                        pd.dismiss()

                        makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<RegisterModal>,
                        response: Response<RegisterModal>
                    ) {

                        try {

                            pd.dismiss()

                            if (response.code() == 500) {
                                makeToast(applicationContext, "The mobile has already been taken")
                            } else if (response.code() == 200) {
                                if (response.body()?.status!!) {

                                    val intent = Intent(
                                        applicationContext,
                                        EnterOtpActivity::class.java
                                    ) //not application context
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    intent.putExtra("mobile", mobileNumber)
                                    intent.putExtra("mobileWithSpace", mobileWithSpace)
                                    startActivity(intent)

                                } else {

//                                makeToast(applicationContext, "Please try again later")
                                    makeToast(
                                        applicationContext,
                                        response.body()!!.error.get(0).error
                                    )
                                }
                            }


                        } catch (Ex: java.lang.Exception) {

                        }
                    }

                })


        } catch (Ex: Exception) {

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == COUNTRY_CODE_ACT) {
            if (data != null) {
                val countryCode = data.getStringExtra(Constants.COUNTRY_CODE)
                binding.tvCountryCode.text = countryCode
            }
        }
    }

    private fun gotoNext() {
        moveToNextScreen(this, EnterOtpActivity::class.java as Class<Any>, null, true)
    }

}