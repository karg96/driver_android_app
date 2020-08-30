package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.FirebaseApp
import com.google.firebase.iid.FirebaseInstanceId
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityLoginBinding
import cos.tuk_tuk_driver.model.RegisterModal
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Constants
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val apiInterface = Comman.getApi()
    private var mobileNumber: String = ""
    private var isDocsUpload: Int = 0
    private var IsApproved: Int = 0
    private var IsBanned: Int = 0

    private var deviceToken: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {


            mobileNumber = intent.getStringExtra("mobileNumber")
            FirebaseApp.initializeApp(this)
            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }

                    // Get new Instance ID token
                    deviceToken = task.result?.token.toString()
                    Log.e("device token",deviceToken)

                })


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
            Comman.makeToast(applicationContext, "" + Ex)

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
        // listener()

    }


    private fun validate() {

        val mobileNumber: String = binding.edtPhoneNumber.text.toString()
        val password: String = binding.edtPasswordNumber.text.toString()

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


                                                if (response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).status.equals(
                                                        "ACTIVE",
                                                        ignoreCase = true
                                                    ) ||
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).status.equals(
                                                        "NEARTOEXPIRE",
                                                        ignoreCase = true
                                                    ) ||
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).status.equals(
                                                        "EXPIRETODAY",
                                                        ignoreCase = true
                                                    )
                                                ) {
//                                                if (response.body()!!.driverDocuments.get(x).status.equals(
//                                                        "ACTIVE",
//                                                        ignoreCase = true
//                                                    )
//                                                ) {
                                                    IsApproved += 1

                                                }


                                                isDocsUpload += 1
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverLicenceFrontImage",
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).url
                                                )
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverLicenceBackImage",
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).additonal_doc
                                                )
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverLicenceExpiry",
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).expiry
                                                )
                                            }

                                            if (response.body()!!.documents.driverDocuments.get(x).document_id == "8") {


                                                if (response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).status.equals(
                                                        "ACTIVE",
                                                        ignoreCase = true
                                                    ) ||
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).status.equals(
                                                        "NEARTOEXPIRE",
                                                        ignoreCase = true
                                                    ) ||
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).status.equals(
                                                        "EXPIRETODAY",
                                                        ignoreCase = true
                                                    )
                                                ) {
//                                                if (response.body()!!.driverDocuments.get(x).status.equals(
//                                                        "ACTIVE",
//                                                        ignoreCase = true
//                                                    )
//                                                ) {
                                                    IsApproved += 1

                                                }


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
                                                    "driverPassportExpiry",
                                                    response.body()!!.documents.driverDocuments.get(
                                                        x
                                                    ).expiry
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


//                                            if (response.body()!!.documents.driverDocuments.get(x).status.equals(
//                                                    "active",
//                                                    ignoreCase = true
//                                                )
//                                            ) {
//                                                IsApproved += 1
//                                            }
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
                                            if (IsApproved == 2) {

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
                                                intent.flags =
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP
                                                startActivity(intent)
                                                finish()
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
                                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
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
                                            Constants.NAME,
                                            response.body()!!.data.first_name + " " + response.body()!!.data.last_name
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
//
//                                    val intent = Intent(
//                                        applicationContext,
//                                        AddDocumentActivity::class.java
//                                    ) //not application context
//                                    intent.putExtra(
//                                        "name",
//                                        response.body()!!.data.first_name + " " + response.body()!!.data.last_name
//                                    )
//                                    intent.flags =
//                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                    startActivity(intent)


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
                            Comman.makeToast(applicationContext, "" + Ex)

                        }


                    }

                })

        } catch (Ex: java.lang.Exception) {
            Comman.makeToast(applicationContext, "" + Ex)

        }

    }


}
