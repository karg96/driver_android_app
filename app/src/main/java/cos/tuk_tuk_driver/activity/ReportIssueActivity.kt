package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityReportIssueBinding
import cos.tuk_tuk_driver.models.GetpaymentModaal
import cos.tuk_tuk_driver.utils.Comman
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ReportIssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportIssueBinding
    private val apiInterface = Comman.getApiToken()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReportIssueBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()

        } catch (Ex: Exception) {

        }

    }

    private fun init() {

        binding.close.setOnClickListener {
            finish()
        }

        binding.btnGetOtp.setOnClickListener {
            validate()
        }

    }

    private fun validate() {
        var string: String = ""
        string = binding.edtMessage.text.toString()
        if (string.isEmpty()) {
            Comman.makeToast(this,
                "Please enter message")
        } else {
            reportIssueData()
        }

    }

    private fun reportIssueData() {
        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.reportIssue("title", binding.edtMessage.text.toString(), "cate")
                .enqueue(object : Callback<GetpaymentModaal> {
                    override fun onFailure(call: Call<GetpaymentModaal>, t: Throwable) {
                        dialog.dismiss()
                        Comman.makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<GetpaymentModaal>,
                        response: Response<GetpaymentModaal>
                    ) {
                        try {

                            dialog.dismiss()

                            if (response.body()!!.status) {
                                binding.edtMessage.setText("")
                                Comman.makeToast(applicationContext, response.body()!!.message)

                            } else {

                                Comman.makeToast(applicationContext, "Please try again later")

                            }

                        } catch (Ex: Exception) {

                        }

                    }

                })
        } catch (Ex: Exception) {
            Comman.makeToast(applicationContext, "Please try again later")

        }

    }
}
