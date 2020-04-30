package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.os.Handler
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.tuktuk.utils.BaseActivity
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.databinding.ActivitySplashBinding
import cos.tuk_tuk_driver.models.Documents
import cos.tuk_tuk_driver.utils.Prefs
import cos.tuk_tuk_driver.utils.ProgressBarAnimation
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class SplashActivity : BaseActivity() {


    private var i = 0
    private lateinit var binding: ActivitySplashBinding
    private var isDocsUpload: Int = 0
    private var IsApproved: Int = 0

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

            runTimer()

        } catch (Ex: Exception) {

        }
    }


    private fun runTimer() {

        Handler().postDelayed({

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

                    } else if (isLogin != "true") {

                        var Authorization = Prefs.getKey(applicationContext, "Authorization")

                        if (!Authorization.isEmpty()) {

                            checkUploadDocuments()

                        } else {
                            //call when user not  login it will redirect to WelcomeActivity screen
                            val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }

                    }

                }
            } catch (Ex: Exception) {

                Comman.makeToast(applicationContext, "Error ${Ex.message}")
            }

        }, 1000)


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

                    }

                    override fun onResponse(call: Call<Documents>, response: Response<Documents>) {
                        try {

                            //  dialog.dismiss()

                            if (response.code() == 200) {


                                if (response.body()!!.driverDocuments.size != 0) {

                                    for (x in 0 until response.body()!!.driverDocuments.size) {

                                        if (response.body()!!.driverDocuments.get(x).document_id == "1") {
                                            isDocsUpload += 1
                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "8") {
                                            isDocsUpload += 1

                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "9") {
                                            isDocsUpload += 1

                                        }

                                        if (response.body()!!.driverDocuments.get(x).status.equals(
                                                "approved",
                                                ignoreCase = true
                                            )
                                        ) {
                                            IsApproved += 1
                                        }

                                    }

                                    if (isDocsUpload == 3) {


                                        if (IsApproved == 3) {

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
                                            intent.putExtra(
                                                "name",
                                                Prefs.getKey(applicationContext, "driveName")
                                            )
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                            startActivity(intent)

                                            Comman.makeToast(
                                                applicationContext,
                                                "Please wait your documents is not approved yet"
                                            )
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
                                            "name", Prefs.getKey(applicationContext, "driveName")
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                        startActivity(intent)
                                    }

                                }


                            } else {
                                Comman.makeToast(applicationContext, "Please try again later")

                            }

                        } catch (Ex: Exception) {

                        }
                    }

                })

        } catch (Ex: java.lang.Exception) {

        }

    }


}