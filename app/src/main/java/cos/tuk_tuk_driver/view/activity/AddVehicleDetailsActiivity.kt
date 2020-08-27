package cos.tuk_tuk_driver.activity

import android.R
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import cos.tuk_tuk_driver.databinding.ActivityAddVehicleDetailsActiivityBinding
import cos.tuk_tuk_driver.model.AddVehicleModal
import cos.tuk_tuk_driver.model.GetVehicleServiceModal
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Comman.makeToast
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
    private var makeYear: String = ""
    private var count: Int = 1
    private var mCatAdapter: ArrayAdapter<String>? = null
    private var mYearAdapter: ArrayAdapter<String>? = null
    private val Items: ArrayList<String> = ArrayList<String>()
    private val ItemsId: ArrayList<String> = ArrayList<String>()
    private val ItemsYear: ArrayList<String> = ArrayList<String>()

    init {
        ItemsYear.add(0, "Please select Year")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleDetailsActiivityBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            from = intent.getStringExtra("from")//update and add

            init()

            val thisYear = Calendar.getInstance()[Calendar.YEAR]

            for (i in 1995..thisYear) {
                ItemsYear.add(count, "" + i)
                count++
            }

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

                binding.vehicleTitle.text = "Edit Vehicle"
                binding.btnAddVehicle.text = "Update Vehicle"

                binding.model.setText(service_model)
                binding.licensePlate.setText(service_number)

                binding.vehilceColor.setText(service_color)
                binding.licensePlate.isEnabled = false
                binding.licensePlate.setBackgroundColor(Color.parseColor("#f4f4f4"))
                //  binding.licensePlate.setBackgroundColor(R.color.holo_blue_dark);

            }

            mYearAdapter = ArrayAdapter<String>(
                applicationContext,
                R.layout.simple_spinner_dropdown_item,
                ItemsYear
            )

            binding.year.setAdapter(mYearAdapter)
            binding.year.setOnItemSelectedListener(object : OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View,
                    position: Int,
                    id: Long
                ) {
                    try {
                        makeYear = ItemsYear.get(position)

                        if (makeYear.equals("Please select Year", ignoreCase = true)) {
                            makeYear = ""
                            makeToast(applicationContext, "Please select Year")

                        } else {
                            makeYear = ItemsYear.get(position)
                            // mCategory = (String) Items.get(position);
                        }
                    } catch (e: java.lang.Exception) {
                    }
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {}
            })


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

        if (binding.model.text.isEmpty() || binding.licensePlate.text.isEmpty() || binding.vehilceColor.text.isEmpty()) {

            makeToast(applicationContext, "Please fill all mandatory fields")

        }
        else if (makeId.isEmpty()) {
            makeToast(applicationContext, "Please select make field")
        }

        else if (makeYear.isEmpty()) {

            makeToast(applicationContext, "Please select Year field")

        } else {

            if (from.equals("add", ignoreCase = true) || from.equals("addNew", ignoreCase = true)) {
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
                makeYear,
                binding.vehilceColor.text.toString()
            )
                .enqueue(object : retrofit2.Callback<AddVehicleModal> {
                    override fun onFailure(call: Call<AddVehicleModal>, t: Throwable) {
                        dialog.dismiss()
                        makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<AddVehicleModal>,
                        response: Response<AddVehicleModal>
                    ) {

                        dialog.dismiss()

                        if (response.code() == 200) {
                            if (response.body()!!.status) {

                                makeToast(applicationContext, response.body()!!.message)
                                finish()
                                /*val intent = Intent(
                                    this@AddVehicleDetailsActiivity,
                                    AllVehicleActivity::class.java
                                )
                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK *//*or Intent.FLAG_ACTIVITY_CLEAR_TASK*//*
                            startActivity(intent)*/

                            } else if (!response.body()!!.status) {
                                makeToast(applicationContext, response.body()!!.error)

                            }
                        } else {
                            makeToast(
                                applicationContext,
                                response.message().toString()
                            )
                        }


                    }

                })


        } catch (Ex: Exception) {
            makeToast(applicationContext, Ex.message)

        }

    }

    private fun addVehicles() {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.addVehicle(
                //"sedan",
                makeId,
                binding.licensePlate.text.toString(),
                binding.model.text.toString(),
                makeYear,
                binding.vehilceColor.text.toString()
            )
                .enqueue(object : retrofit2.Callback<AddVehicleModal> {
                    override fun onFailure(call: Call<AddVehicleModal>, t: Throwable) {
                        dialog.dismiss()
                        makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<AddVehicleModal>,
                        response: Response<AddVehicleModal>
                    ) {

                        dialog.dismiss()
                        if (response.code() == 200) {
                            if (response.body()!!.status) {

                                binding.licensePlate.setText("")
                                binding.model.setText("")
                                binding.year.text = ""
                                binding.vehilceColor.setText("")

                             //   makeToast(applicationContext, response.body()!!.message)
                                if (from.equals("addNew")) {
//                                    val intent = Intent(
//                                        this@AddVehicleDetailsActiivity,
//                                        AllVehicleActivity::class.java
//                                    )
//                                    intent.flags =
//                                        Intent.FLAG_ACTIVITY_NEW_TASK //or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                    startActivity(intent)

                                }

                                finish()


                            }
                            //else if (!response.body()!!.status) {
//                                makeToast(
//                                    applicationContext,
//                                    "Registration Number has already been taken."
//                                )

                          //  }
                        } else {
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


                            for (x in 0 until ItemsYear.size) {

                                if (service_year.equals(
                                        "" + ItemsYear.get(x),
                                        ignoreCase = true
                                    )
                                ) {

                                    binding.year.text = service_year
                                    makeYear = service_year
                                    binding.year.selectedIndex = x
                                }
                            }



                            mYearAdapter!!.notifyDataSetChanged()


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
