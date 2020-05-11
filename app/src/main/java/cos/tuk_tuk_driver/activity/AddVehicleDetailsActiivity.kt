package cos.tuk_tuk_driver.activity

import android.R
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.tuktuk.utils.Comman
import com.tuktuk.utils.Comman.makeToast
import cos.tuk_tuk_driver.databinding.ActivityAddVehicleDetailsActiivityBinding
import cos.tuk_tuk_driver.models.AddVehicleModal
import cos.tuk_tuk_driver.models.GetVehicleServiceModal
import retrofit2.Call
import retrofit2.Response
import java.util.*

class AddVehicleDetailsActiivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVehicleDetailsActiivityBinding
    private val apiInterface = Comman.getApiToken()
    private var from: String = ""
    private var service_color: String = ""
    private var service_model: String = ""
    private var service_year: String = ""
    private var service_number: String = ""
    private var service_type_id: String = ""
    private var vehicleId: String = ""
    private var makeId: String = ""
    private var mCatAdapter: ArrayAdapter<String>? = null
    private val Items: ArrayList<String> = ArrayList<String>()
    private val ItemsId: ArrayList<String> = ArrayList<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleDetailsActiivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            from = intent.getStringExtra("from")

            init()

            Items.add(0, "Please select Make")
            ItemsId.add(0, "Please select Make")

            binding.licensePlate.isEnabled = true

            if (from.equals("update", ignoreCase = true)) {

                service_type_id = intent.getStringExtra("service_type_id")
                service_color = intent.getStringExtra("service_color")
                service_model = intent.getStringExtra("service_model")
                service_year = intent.getStringExtra("service_year")
                service_number = intent.getStringExtra("service_number")
                vehicleId = intent.getStringExtra("vehicleId")

                binding.model.setText(service_model)
                binding.licensePlate.setText(service_number)
                binding.year.setText(service_year)
                binding.vehilceColor.setText(service_color)
                binding.licensePlate.isEnabled = false
            }

            mCatAdapter = ArrayAdapter<String>(
                applicationContext,
                R.layout.simple_spinner_dropdown_item,
                Items
            )
            binding.make.setAdapter(mCatAdapter)
            binding.make.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    try {
                        if (position == 0) {
                            makeId = ""
                            makeToast(applicationContext, "Please select Make")

                        } else {
                            makeId = ItemsId.get(position)
                            // mCategory = (String) Items.get(position);
                        }
                    } catch (e: java.lang.Exception) {
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })

            getServices()
        } catch (Ex: Exception) {
            makeToast(applicationContext, "${Ex.message}")

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

            makeToast(applicationContext, "Please fill all mandatory fields")

        } else if (makeId.isEmpty()) {

            makeToast(applicationContext, "Please select make field")

        } else {

            if (from.equals("add", ignoreCase = true)) {
                addVehicles()

            }

            if (from.equals("update", ignoreCase = true)) {
                updateVehicles()

            }


        }

    }

    private fun updateVehicles() {
        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.updateVehicle(
                vehicleId,
                makeId,
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

                            Comman.makeToast(applicationContext, response.body()!!.message)
                            // finish()
                            val intent = Intent(
                                this@AddVehicleDetailsActiivity,
                                AllVehicleActivity::class.java
                            )
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else if (!response.body()!!.status) {
                            Comman.makeToast(applicationContext, response.body()!!.error)

                        }

                    }

                })


        } catch (Ex: Exception) {
            Comman.makeToast(applicationContext, Ex.message)

        }

    }

    private fun addVehicles() {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.addVehicle(
                makeId,
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

                            binding.licensePlate.setText("")
                            binding.model.setText("")
                            binding.year.setText("")
                            binding.vehilceColor.setText("")

                            makeToast(applicationContext, response.body()!!.message)
                            // finish()
                            val intent = Intent(
                                this@AddVehicleDetailsActiivity,
                                AllVehicleActivity::class.java
                            )
                            intent.flags =
                                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)

                        } else if (!response.body()!!.status) {
                            makeToast(
                                applicationContext,
                                "Registration Number has already been taken."
                            )

                        }

                    }

                })


        } catch (Ex: Exception) {

        }

    }

    private fun getServices() {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()
            Items.clear()
            ItemsId.clear()
            Items.add(0, "Please select Make")
            ItemsId.add(0, "Please select Make")

            apiInterface!!.services("0")
                .enqueue(object : retrofit2.Callback<GetVehicleServiceModal> {
                    override fun onFailure(call: Call<GetVehicleServiceModal>, t: Throwable) {
                        dialog.dismiss()
                        makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<GetVehicleServiceModal>,
                        response: Response<GetVehicleServiceModal>
                    ) {

                        dialog.dismiss()

                        if (response.body()!!.status) {


                            for (x in 0 until response.body()!!.data.size) {

                                Items.add(x + 1, response.body()!!.data.get(x).name)
                                ItemsId.add(x + 1, "" + response.body()!!.data.get(x).id)

                                if (service_type_id.equals(
                                        "" + response.body()!!.data.get(x).id,
                                        ignoreCase = true
                                    )
                                ) {
                                    binding.make.text = response.body()!!.data.get(x).name
                                    makeId = "" + response.body()!!.data.get(x).id
                                }
                            }

                            mCatAdapter!!.notifyDataSetChanged()

                        } else if (!response.body()!!.status) {
                            makeToast(applicationContext, response.body()!!.message)

                        }

                    }

                })


        } catch (Ex: Exception) {
            makeToast(applicationContext, Ex.message)

        }

    }
}
