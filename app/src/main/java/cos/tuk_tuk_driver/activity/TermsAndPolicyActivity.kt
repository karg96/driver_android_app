package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tuktuk.models.RegisterModal
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityCreatePasswordBinding
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


            Comman.getApi()!!.UpdateDriver(
                DriverFirstName,
                DriverLastName,
                Password,
                ConfirmPassword,
                DriverEmail
            )
                .enqueue(object : Callback<RegisterModal> {
                    override fun onFailure(call: Call<RegisterModal>, t: Throwable) {
                        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
                        pd.dismiss()

                    }

                    override fun onResponse(
                        call: Call<RegisterModal>,
                        response: Response<RegisterModal>
                    ) {
                        pd.dismiss()

                    }

                })

        } catch (Ex: java.lang.Exception) {

        }

    }
}
