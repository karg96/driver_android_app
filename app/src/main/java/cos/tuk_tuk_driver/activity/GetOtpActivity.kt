package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

import android.content.Intent
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
    private val apiInterface = Comman.getApi()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGetOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            init()

        } catch (ex: Exception) {

        }
    }

    fun init() {
        listener()
    }

    private fun listener() {

        /* binding.tvCountryCode.setOnClickListener {
             startActivityForResult(Intent(this, CountryCodeActivity::class.java), COUNTRY_CODE_ACT)
         }*/

        binding.btnGetOtp.setOnClickListener {
            val intent = Intent(this@GetOtpActivity, EnterOtpActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)

//            validate()
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

            registerMobile( mobileNumber, countryCode + " " + mobileNumber)
        }

    }


    private fun registerMobile(mobileNumber: String, mobileWithSpace: String) {

        /*try {

            apiInterface!!.RegisterMobile(mobileNumber, "type", "android")
                .enqueue(object : Callback<RegisterModal> {
                    override fun onFailure(call: Call<RegisterModal>, t: Throwable) {

                        makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<RegisterModal>,
                        response: Response<RegisterModal>
                    ) {

                        try {

                            if (response.body()?.status!!) {

                                val intent = Intent(
                                    applicationContext,
                                    EnterOtpActivity::class.java
                                ) //not application context
                                intent.putExtra("mobile", mobileNumber)
                                intent.putExtra("mobileWithSpace", mobileWithSpace)
                                startActivity(intent)

                            } else {


                                makeToast(applicationContext, response.body()!!.error.mobile[0])
                            }


                        } catch (Ex: java.lang.Exception) {

                        }
                    }

                })


        } catch (Ex: Exception) {

        }*/
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