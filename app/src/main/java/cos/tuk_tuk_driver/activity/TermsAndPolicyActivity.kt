package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.tuktuk.models.RegisterModal
import com.tuktuk.utils.Comman
import com.tuktuk.utils.Comman.makeToast
import cos.tuk_tuk_driver.databinding.ActivityTermsAndPolicyBinding
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TermsAndPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTermsAndPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            binding.btnNext.setOnClickListener {

                updateDriverData()

            }

            binding.ivBack.setOnClickListener {

                finish()
            }

            val styledText =
                "To learn more, see our  <font  color=\"#00B9FF\">Terms of Use</font> and <font  color=\"#00B9FF\">Privacy Policy</font>"
            binding.textBottom.setText(Html.fromHtml(styledText));




        } catch (Ex: Exception) {

        }

    }

    private fun updateDriverData() {

        try {

            val pd = ProgressDialog(this)
            pd.setMessage("Please wait....")
            pd.setCancelable(false)
            pd.setProgressStyle(ProgressDialog.STYLE_SPINNER)
            pd.show()


            var DriverFirstName = Prefs.getKey(applicationContext, "DriverFirstName")
            var DriverLastName = Prefs.getKey(applicationContext, "DriverLastName")
            var DriverEmail = Prefs.getKey(applicationContext, "DriverEmail")
            var Password = Prefs.getKey(applicationContext, "DriverPassword")
            var ConfirmPassword = Prefs.getKey(applicationContext, "DriverConfirmPassword")


            Comman.getApiToken()!!.UpdateDriver(
                DriverFirstName,
                DriverLastName,
                Password,
                ConfirmPassword,
                DriverEmail
            ).enqueue(object : Callback<RegisterModal> {
                override fun onFailure(call: Call<RegisterModal>, t: Throwable) {
                    pd.dismiss()
                    makeToast(applicationContext, "Please try again later")
                }

                override fun onResponse(
                    call: Call<RegisterModal>,
                    response: Response<RegisterModal>
                ) {
                    pd.dismiss()
                    Prefs.putKey(applicationContext, "isLogin", "true")
                    makeToast(applicationContext, "User Created successfully")
                    val intent = Intent(
                        applicationContext,
                        HomeActivity::class.java
                    ) //not application context
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                }

            })

        } catch (Ex: java.lang.Exception) {

        }

    }

}
