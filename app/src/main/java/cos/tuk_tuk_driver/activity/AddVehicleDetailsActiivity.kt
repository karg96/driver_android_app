package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.databinding.ActivityAddVehicleDetailsActiivityBinding
import cos.tuk_tuk_driver.models.AddVehicleModal
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Field

class AddVehicleDetailsActiivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVehicleDetailsActiivityBinding
    private val apiInterface = Comman.getApiToken()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleDetailsActiivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()

        } catch (Ex: Exception) {

        }

    }

    private fun init() {

        binding.btnAddVehicle.setOnClickListener {

            validate()

        }
        binding.close.setOnClickListener {

            finish()

        }

    }

    private fun validate() {

        if (binding.model.text.isEmpty() || binding.licensePlate.text.isEmpty() || binding.year.text.isEmpty()
        ) {

            Comman.makeToast(applicationContext, "Please fill all mandatory fields")

        } else {

            addVehicles()

        }

    }

    private fun addVehicles() {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()


            apiInterface!!.addVehicle(
                "3",
                binding.licensePlate.text.toString(),
                binding.model.text.toString(),
                binding.year.text.toString(),
                binding.vehilceColor.text.toString()
            )
                .enqueue(object : retrofit2.Callback<AddVehicleModal> {
                    override fun onFailure(call: Call<AddVehicleModal>, t: Throwable) {
                        dialog.dismiss()
                        Comman.makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<AddVehicleModal>,
                        response: Response<AddVehicleModal>
                    ) {

                        dialog.dismiss()

                        if (response.body()!!.status) {

                            /*  binding.licensePlate.text = "" as EditText
                              binding.model.text = ""
                              binding.year.text = ""
                              binding.vehilceColor.text = ""*/

                            Comman.makeToast(applicationContext, response.body()!!.message)
                            finish()
                            /*  val intent = Intent(
                                  this@AddVehicleDetailsActiivity,
                                  AllVehicleActivity::class.java
                              )
                              intent.flags =
                                  Intent.FLAG_ACTIVITY_NEW_TASK *//*or Intent.FLAG_ACTIVITY_CLEAR_TASK*//*
                            startActivity(intent)*/

                        } else if (!response.body()!!.status) {
                            Comman.makeToast(applicationContext, response.body()!!.message)

                        }

                    }

                })


        } catch (Ex: Exception) {

        }

    }
}
