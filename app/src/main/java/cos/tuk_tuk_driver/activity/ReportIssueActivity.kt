package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.databinding.ActivityAboutBinding
import cos.tuk_tuk_driver.databinding.ActivityReportIssueBinding

class ReportIssueActivity : AppCompatActivity() {

    private lateinit var binding: ActivityReportIssueBinding

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
    }
}
