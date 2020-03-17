package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Build
import android.view.Window
import android.view.WindowManager
import androidx.annotation.RequiresApi
import com.tuktuk.utils.BaseActivity
import cos.tuk_tuk_driver.databinding.ActivitySplashBinding
import cos.tuk_tuk_driver.utils.ProgressBarAnimation
import java.util.*


class SplashActivity : BaseActivity() {

    private val displayLength: Long = 3000
    lateinit var timer: Timer
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

            init()

        } catch (Ex: Exception) {

        }
    }

    private fun init() {
        timer = Timer()
        runTimer()
    }


    private fun runTimer() {
        timer.schedule(object : TimerTask() {
            @RequiresApi(Build.VERSION_CODES.N)
            override fun run() {
                if (i < 100) {
                    val anim = ProgressBarAnimation(
                        binding.progressBar,
                        binding.progressBar.progress.toFloat(),
                        i.toFloat()
                    )
                    anim.duration = 400
//                    binding.progressBar.startAnimation(anim)
                    i += 33

                } else {

                    timer.cancel()

                    val intent = Intent(this@SplashActivity, WelcomeActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

            }

        }, 0, 1000)
    }


}