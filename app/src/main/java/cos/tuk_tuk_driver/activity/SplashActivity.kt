package cos.tuk_tuk_driver.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.Window
import android.view.WindowManager
import com.tuktuk.utils.BaseActivity
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivitySplashBinding
import cos.tuk_tuk_driver.models.Documents
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Prefs
import cos.tuk_tuk_driver.utils.ProgressBarAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SplashActivity : BaseActivity() {


    private var i = 0
    private lateinit var binding: ActivitySplashBinding
    private var isDocsUpload: Int = 0
    private var IsApproved: Int = 0
    private var mHandler: Handler? = null

    private var mRunnable: Runnable? = null
    private val apiInterface = Comman.getApiToken()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )

        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            // Initialize other stuff
            mHandler = Handler(Looper.getMainLooper())


            runTimer()

        } catch (Ex: Exception) {

        }
    }


    private fun runTimer() {

        mRunnable = Runnable {
            try {

                if (i < 100) {

                    val anim = ProgressBarAnimation(
                        binding.progressBar,
                        binding.progressBar.progress.toFloat(),
                        i.toFloat()
                    )

                    anim.duration = 400
                    binding.progressBar.startAnimation(anim)
                    i += 33

                    //recursion :: calling same method util condition false
                    runTimer()
                } else {

                    //fetching value from prefs while user is already login or not
                    var isLogin = Prefs.getKey(applicationContext, "isLogin")

                    if (isLogin == "true") {
                        //call when user already login
                        val intent = Intent(this@SplashActivity, HomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        // overridePendingTransition(R.anim.enter)

                    } else if (isLogin != "true" || isLogin.isEmpty()) {

                        var Authorization = Prefs.getKey(applicationContext, "Authorization")

                        if (!Authorization.isEmpty()) {

                            checkUploadDocuments()

                        } else if (Authorization.isEmpty()) {
                            //call when user not  login it will redirect to WelcomeActivity screen
                            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)

                        }

                    }

                }
            } catch (Ex: Exception) {

                Comman.makeToast(applicationContext, "Error ${Ex.message}")
            }
        }

        mHandler!!.postDelayed(mRunnable, 1000)


        /* Handler().postDelayed({



         }, 1000)*/


    }

    private fun checkUploadDocuments() {

        try {

            /*val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()*/

            apiInterface!!.getUploadDocs()
                .enqueue(object : Callback<Documents> {
                    override fun onFailure(call: Call<Documents>, t: Throwable) {
                        //   dialog.dismiss()
                       // Comman.makeToast(applicationContext, "Please try again later")
                        val intent = Intent(
                            applicationContext,
                            AddDocumentActivity::class.java
                        ) //not application context

                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                    override fun onResponse(call: Call<Documents>, response: Response<Documents>) {
                        try {

                            //  dialog.dismiss()

                            if (response.code() == 200) {


                                if (response.body()!!.driverDocuments.size != 0) {

                                    for (x in 0 until response.body()!!.driverDocuments.size) {

                                        if (response.body()!!.driverDocuments.get(x).document_id == "1") {
                                            isDocsUpload += 1
                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                ) ||

                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                )||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRETODAY",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                IsApproved += 1
                                            }
                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "8") {
                                            isDocsUpload += 1
                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                )||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRETODAY",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                IsApproved += 1
                                            }

                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "9") {
                                            isDocsUpload += 1

                                        }


                                    }

                                    if (isDocsUpload == 3) {


                                        if (IsApproved == 2) {

                                            Prefs.putKey(applicationContext, "isLogin", "true")

                                            val intent = Intent(
                                                applicationContext,
                                                HomeActivity::class.java
                                            ) //not application context
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)
                                        } else {
                                            val intent = Intent(
                                                applicationContext,
                                                AddDocumentActivity::class.java
                                            ) //not application context

                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)

                                            Comman.makeToast(
                                                applicationContext,
                                                "Please wait your documents is not approved yet")
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

                                    intent.flags =
                                        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)
                                }

                            } else {
                                val intent = Intent(
                                    applicationContext,
                                    AddDocumentActivity::class.java
                                ) //not application context

                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                startActivity(intent)
                               // Comman.makeToast(applicationContext, "Please try again later")

                            }

                        } catch (Ex: Exception) {

                        }
                    }

                })

        } catch (Ex: java.lang.Exception) {

        }

    }


}