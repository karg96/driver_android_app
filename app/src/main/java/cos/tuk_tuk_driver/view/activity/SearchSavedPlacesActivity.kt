package cos.tuk_tuk_driver.activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.adapter.SavedPlacesLocationAdapter
import cos.tuk_tuk_driver.databinding.ActivitySearchSavedPlacesBinding

class SearchSavedPlacesActivity : AppCompatActivity() {

    lateinit var binding: ActivitySearchSavedPlacesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_saved_places)
        binding = ActivitySearchSavedPlacesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()

        } catch (Ex: Exception) {

        }

    }

    @SuppressLint("WrongConstant")
    private fun init() {

        val adapter = SavedPlacesLocationAdapter(this)

        binding.placesRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        binding.placesRecyclerview.adapter = adapter
        adapter.notifyDataSetChanged()

        binding.ivBack.setOnClickListener {
            finish()

        }
    }


}
