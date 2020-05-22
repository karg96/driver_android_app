package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityLoginBinding
import cos.tuk_tuk_driver.models.RegisterModal
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding

    private val apiInterface = Comman.getApi()
    private var mobileNumber: String = ""
    private var isDocsUpload: Int = 0
    // private var IsApproved: Int = 0
    private var IsBanned: Int = 0

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
                binding.edtPhoneNumber.isCursorVisible = true
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
            intent.putExtra("from", "register")
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

        } else if (password.length < 7 || password.length > 16) {

            Comman.makeToast(
                applicationContext,
                "Password must be greater than 7 characters and less than 16 characters long"
            )

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

            apiInterface!!.Login(mobileNumber, password, "123", "android")
                .enqueue(object : Callback<RegisterModal> {
                    override fun onFailure(call: Call<RegisterModal>, t: Throwable) {
                        pd.dismiss()
                        Comman.makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<RegisterModal>,
                        response: Response<RegisterModal>
                    ) {

                        try {

                            pd.dismiss()
                            if (response.code() == 200) {
                                if (response.body()!!.status) {

                                    Prefs.putKey(
                                        applicationContext,
                                        "Authorization",
                                        response.body()!!.data.access_token
                                    )

                                    Prefs.putKey(
                                        applicationContext,
                                        "driveName",
                                        response.body()!!.data.first_name + " " + response.body()!!.data.last_name
                                    )
                                    Prefs.putKey(
                                        applicationContext,
                                        "driveImage",
                                        response.body()!!.data.avatar
                                    )
                                    Prefs.putKey(
                                        applicationContext,
                                        "driveMobile",
                                        response.body()!!.data.mobile
                                    )
                                    Prefs.putKey(
                                        applicationContext,
                                        "driveRating",
                                        response.body()!!.data.rating
                                    )


                                    if (response.body()!!.documents.driverDocuments.size != 0) {

                                        for (x in 0 until response.body()!!.documents.driverDocuments.size) {

                                            if (response.body()!!.documents.driverDocuments.get(x).document_id == "1") {
                                                isDocsUpload += 1
                                                Prefs.putKey(applicationContext, "driverLicenceImage", response.body()!!.documents.driverDocuments.get(x).url)
                                            }

                                            if (response.body()!!.documents.driverDocuments.get(x).document_id == "8") {
                                                isDocsUpload += 1
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverPassportFrontImage",
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).url
                                                )
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverPassportBackImage",
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).additonal_doc
                                                )
                                            }

                                            if (response.body()!!.documents.driverDocuments.get(x).document_id == "9") {
                                                isDocsUpload += 1
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverAddImage",
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).url
                                                )
                                            }

                                            /*if (response.body()!!.documents.driverDocuments.get(x).status.equals(
                                                    "approved",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                IsApproved += 1
                                            }*/
                                            if (response.body()!!.documents.driverDocuments.get(x).status.equals(
                                                    "banned",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                IsBanned += 1
                                            }

                                        }

                                        if (IsBanned > 0) {
                                            Comman.makeToast(
                                                applicationContext,
                                                "Your account is Suspended"
                                            )
                                            return
                                        }

                                        if (isDocsUpload == 3) {
                                            if (response.body()!!.data.status.equals(
                                                    "approved",
                                                    ignoreCase = true
                                                )
                                            ) {

                                                Comman.makeToast(
                                                    applicationContext,
                                                    "Login Success"
                                                )

                                                Prefs.putKey(applicationContext, "isLogin", "true")
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "payment_mode",
                                                    response.body()!!.data.payment_mode
                                                )
                                                val intent = Intent(
                                                    applicationContext,
                                                    HomeActivity::class.java
                                                ) //not application context
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(intent)
                                            } else {
                                                Comman.makeToast(
                                                    applicationContext,
                                                    "Please wait your documents is not approved yet"
                                                )
                                                val intent = Intent(
                                                    applicationContext,
                                                    AddDocumentActivity::class.java
                                                ) //not application context
                                                intent.putExtra(
                                                    "name",
                                                    response.body()!!.data.first_name + " " + response.body()!!.data.last_name
                                                )
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                startActivity(intent)
                                            }

                                        } else {

                                            Comman.makeToast(
                                                applicationContext,
                                                "Please upload your documents"
                                            )

                                            val intent = Intent(
                                                applicationContext,
                                                AddDocumentActivity::class.java
                                            ) //not application context
                                            intent.putExtra(
                                                "name",
                                                response.body()!!.data.first_name + " " + response.body()!!.data.last_name
                                            )
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        }

                                    }


                                } else {
                                    Comman.makeToast(
                                        applicationContext,
                                        getString(R.string.error_login)
                                    )
                                }

                            } else if (response.code() == 401) {
                                Comman.makeToast(
                                    applicationContext,
                                    "The mobile number or password you entered is incorrect!"
                                )


                            } else {

                                Comman.makeToast(applicationContext, "Please try again later")

                            }
                        } catch (Ex: java.lang.Exception) {

                        }


                    }

                })

        } catch (Ex: java.lang.Exception) {

        }

    }


}
