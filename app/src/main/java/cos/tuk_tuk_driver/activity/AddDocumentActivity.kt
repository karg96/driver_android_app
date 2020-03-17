package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityAddDocumentBinding
import cos.tuk_tuk_driver.databinding.ActivityCreateNameBinding

class AddDocumentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDocumentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding=ActivityAddDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
