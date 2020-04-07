package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.os.Handler
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.tuktuk.utils.BaseActivity
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.databinding.ActivitySplashBinding
import cos.tuk_tuk_driver.utils.Prefs
import cos.tuk_tuk_driver.utils.ProgressBarAnimation
import java.util.*


class SplashActivity : BaseActivity() {


    private var i = 0
    private lateinit var binding: ActivitySplashBinding

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


    override fun onResume() {
        super.onResume()
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
                        //call when user not  login it will redirect to WelcomeActivity screen
                        val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                        intent.flags =
                            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                    }

                }
            } catch (Ex: Exception) {

                Comman.makeToast(applicationContext, "Error ${Ex.message}")
            }

        }, 1000)

        /* timer.schedule(object : TimerTask() {
             @RequiresApi(Build.VERSION_CODES.N)
             override fun run() {
                 if (i < 100) {
                     val anim = ProgressBarAnimation(
                         binding.progressBar,
                         binding.progressBar.progress.toFloat(),
                         i.toFloat()
                     )
                     anim.duration = 400
                     binding.progressBar.startAnimation(anim)
                     i += 33

                 } else {

                     timer.cancel()

                     val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                     intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                     startActivity(intent)
                 }

             }

         }, 0, 1000)*/
    }


}