package cos.tuk_tuk_driver.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.adapter.AllVehicleAdapter
import cos.tuk_tuk_driver.databinding.ActivityAllVehicleBinding
import cos.tuk_tuk_driver.models.GetVehicleModal
import cos.tuk_tuk_driver.models.Vehicles
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllVehicleBinding
    private val apiInterface = Comman.getApiToken()
    val dataList = ArrayList<Vehicles>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()
            getVehiclesList()

        } catch (Ex: Exception) {

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
                                            this@AllVehicleActivity,
                                            AddVehicleDetailsActiivity::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                                        startActivity(intent)
                                        return
                                    }
                                    dataList.addAll(response.body()!!.vehicles)
                                    val adapter = AllVehicleAdapter(applicationContext, dataList)
                                    binding.vehicleRecyclerview.adapter = adapter
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

    @SuppressLint("WrongConstant")
    private fun init() {


        binding.vehicleRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        binding.close.setOnClickListener {
          /*  val intent = Intent(this@AllVehicleActivity, HomeActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)*/
            finish()
        }

        binding.btnAddVehicle.setOnClickListener {
            val intent = Intent(
                this@AllVehicleActivity,
                AddVehicleDetailsActiivity::class.java
            )
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
            startActivity(intent)
        }

    }
}
