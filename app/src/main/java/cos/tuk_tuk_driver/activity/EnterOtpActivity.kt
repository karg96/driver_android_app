package cos.tuk_tuk_driver.activity


import android.os.Bundle
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.CountDownTimer
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.tuktuk.models.OtpModal
import com.tuktuk.models.RegisterModal
import com.tuktuk.utils.BaseActivity
import com.tuktuk.utils.Comman
import com.tuktuk.utils.Comman.makeToast
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityEnterOtpBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class EnterOtpActivity : BaseActivity() {

    lateinit var binding: ActivityEnterOtpBinding
    private val apiInterface = Comman.getApi()

    private var mobile: String = ""
    private var mobileWithSpace: String = ""
    private var isSendCount: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEnterOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            mobile = intent.getStringExtra("mobile")
            mobileWithSpace = intent.getStringExtra("mobileWithSpace")
//            mobile = "9876543210"

            init()

        } catch (ex: Exception) {

        }

    }

    private fun init() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.tvResendCode.setOnClickListener {

            isSendCount = +1

            if (binding.tvResendCode.text == "Resend code") {
                resendCode()
            }

        }

        binding.edtOtp1.setOnClickListener {
            binding.edtOtp1.setBackgroundResource(R.drawable.edt_round_orange)
            binding.edtOtp2.setBackgroundResource(R.drawable.edt_round)
            binding.edtOtp3.setBackgroundResource(R.drawable.edt_round)
            binding.edtOtp4.setBackgroundResource(R.drawable.edt_round)

        }

        binding.edtOtp4.setOnClickListener {
            binding.edtOtp4.setBackgroundResource(R.drawable.edt_round_orange)
        }


        binding.edtOtp1.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // TODO Auto-generated method stub
                if (s.length == 1) //size as per your requirement
                {

                    binding.edtOtp2.requestFocus()
                    binding.edtOtp2.setBackgroundResource(R.drawable.edt_round_orange)

                    binding.edtOtp1.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp3.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round)

                } else {

                    val inputManager: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        currentFocus?.windowToken,
                        InputMethodManager.SHOW_FORCED
                    ) // It can be done by show_forced too


                    binding.edtOtp1.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp2.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp3.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round)

                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) {

            }

            override fun afterTextChanged(s: Editable) { // TODO Auto-generated method stub
            }
        })

        binding.edtOtp2.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // TODO Auto-generated method stub
                if (s.length == 1) //size as per your requirement
                {
                    binding.edtOtp3.requestFocus()
                    binding.edtOtp3.setBackgroundResource(R.drawable.edt_round_orange)

                    binding.edtOtp1.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp2.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round)

                } else if (s.length == 0) //size as per your requirement
                {
                    binding.edtOtp1.requestFocus()
                    binding.edtOtp1.setBackgroundResource(R.drawable.edt_round_orange)

                    binding.edtOtp2.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp3.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round)

                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // TODO Auto-generated method stub
            }
        })

        binding.edtOtp3.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // TODO Auto-generated method stub
                if (s.length == 1) //size as per your requirement
                {
                    binding.edtOtp4.requestFocus()
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round_orange)

                    binding.edtOtp1.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp2.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp3.setBackgroundResource(R.drawable.edt_round)


                } else if (s.length == 0) //size as per your requirement
                {
                    binding.edtOtp2.requestFocus()
                    binding.edtOtp2.setBackgroundResource(R.drawable.edt_round_orange)

                    binding.edtOtp1.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp3.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round)

                }
            }

            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // TODO Auto-generated method stub
            }
        })

        binding.edtOtp4.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(
                s: CharSequence,
                start: Int,
                before: Int,
                count: Int
            ) { // TODO Auto-generated method stub
                if (s.length == 1) //size as per your requirement
                {
                    val inputManager: InputMethodManager =
                        getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    inputManager.hideSoftInputFromWindow(
                        currentFocus?.windowToken,
                        InputMethodManager.SHOW_FORCED
                    ) // It can be done by show_forced too

                    binding.edtOtp1.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp2.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp3.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round)

                } else if (s.length == 0) //size as per your requirement
                {
                    binding.edtOtp3.requestFocus()
                    binding.edtOtp3.setBackgroundResource(R.drawable.edt_round_orange)
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round_orange)

                    binding.edtOtp1.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp2.setBackgroundResource(R.drawable.edt_round)
                    binding.edtOtp4.setBackgroundResource(R.drawable.edt_round)

                }
            }


            override fun beforeTextChanged(
                s: CharSequence, start: Int,
                count: Int, after: Int
            ) { // TODO Auto-generated method stub
            }

            override fun afterTextChanged(s: Editable) { // TODO Auto-generated method stub
            }
        })


        binding.tvDigitCode.text =
            Html.fromHtml("Enter the 4 digit code we sent you <br> at <b STYLE= 'color: #ffffff' >" + mobileWithSpace + "</b>")
        runTimer()

        binding.btnOtp.setOnClickListener {
            validate()
        }

    }


    private fun resendCode() {

        try {

            apiInterface!!.ResendOTP(mobile, "token", "android")
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

                                makeToast(applicationContext, "OTP send Successfully")
                                runTimer()


                            } else {

                                makeToast(
                                    applicationContext,
                                    response.body()!!.error.mobile.toString()
                                )
                            }


                        } catch (Ex: java.lang.Exception) {

                        }

                    }

                })


        } catch (Ex: Exception) {

        }

    }

    private fun hideKeyboard(view: View) {
        view.apply {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun validate() {

        // binding.otpView.isCursorVisible = false

        var OtpOne: String = binding.edtOtp1.text.toString()
        var OtpTwo: String = binding.edtOtp2.text.toString()
        var OtpThree: String = binding.edtOtp3.text.toString()
        var OtpFour: String = binding.edtOtp4.text.toString()


        if (OtpOne.isEmpty() || OtpTwo.isEmpty() || OtpThree.isEmpty() || OtpFour.isEmpty()) {

            makeToast(applicationContext, "Please fill  OTP")

        } else {

            validateOTP(OtpOne + OtpTwo + OtpThree + OtpFour)

        }

    }

    private fun validateOTP(otp: String) {

        try {

            apiInterface!!.VealidateOtp(mobile, "token", otp)
                .enqueue(object : Callback<OtpModal> {
                    override fun onFailure(call: Call<OtpModal>, t: Throwable) {
                        makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<OtpModal>,
                        response: Response<OtpModal>
                    ) {
                        try {

                            if (response.body()?.status!!) {
                                makeToast(applicationContext, "Login Success")
                                binding.tvTimer.visibility = View.GONE
                                val intent = Intent(
                                    applicationContext,
                                    EmailAddressActivity::class.java
                                ) //not application context
                                startActivity(intent)

                            } else {

                                makeToast(applicationContext, "Invalid OTP")
                            }


                        } catch (Ex: java.lang.Exception) {

                        }

                    }

                })


        } catch (Ex: Exception) {

        }

    }

    private fun runTimer() {

        binding.tvResendCode.text = "Resend code in"
        binding.tvResendCode.setTextColor(Color.parseColor("#808080"))
        binding.tvTimer.visibility = View.VISIBLE

        object : CountDownTimer(30000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

                binding.tvTimer.text = "00:" + checkDigit(millisUntilFinished / 1000)

            }

            override fun onFinish() {

                binding.tvResendCode.text = "Resend code"
                binding.tvResendCode.setTextColor(Color.parseColor("#00B9FF"))
                binding.tvTimer.visibility = View.GONE

                if (isSendCount == 1) {
                    val intent = Intent(
                        applicationContext,
                        GetOtpActivity::class.java
                    ) //not application context
                    startActivity(intent)
                }

//                makeToast(applicationContext, "hello ")
            }
        }.start()
    }

    fun checkDigit(number: Long): String? {
        return if (number <= 9) "0$number" else number.toString()
    }


}