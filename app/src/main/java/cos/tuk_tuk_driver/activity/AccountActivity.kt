package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.adapter.AllVehicleAdapter
import cos.tuk_tuk_driver.databinding.ActivityAccountBinding
import cos.tuk_tuk_driver.models.GetVehicleModal
import cos.tuk_tuk_driver.models.Vehicles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class AccountActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAccountBinding
    private val apiInterface = Comman.getApiToken()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccountBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()

        } catch (Ex: Exception) {

        }

    }

    private fun init() {

        binding.vehilce.setOnClickListener {
            getVehiclesList()

        }

        binding.close.setOnClickListener {
            finish()
        }
    }


    private fun getVehiclesList() {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.getVehicle("")
                .enqueue(object : Callback<GetVehicleModal> {
                    override fun onFailure(call: Call<GetVehicleModal>, t: Throwable) {
                        dialog.dismiss()

                    }

                    override fun onResponse(
                        call: Call<GetVehicleModal>,
                        response: Response<GetVehicleModal>
                    ) {
                        try {

                            dialog.dismiss()

                            if (response.body()!!.status) {

                                if (response.body()!!.vehicles != null) {

                                    if (response.body()!!.vehicles.isEmpty()) {
                                        val intent = Intent(
                                            this@AccountActivity,
                                            VehicleActivity::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                                        startActivity(intent)
                                        return
                                    } else {
                                        val intent = Intent(
                                            this@AccountActivity,
                                            AllVehicleActivity::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                                        startActivity(intent)
                                    }

                                }
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
