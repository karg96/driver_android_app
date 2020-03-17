package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityCreatePasswordBinding
import cos.tuk_tuk_driver.databinding.ActivityTermsAndPolicyBinding

class TermsAndPolicyActivity : AppCompatActivity() {

    private lateinit var binding: ActivityTermsAndPolicyBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityTermsAndPolicyBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
