package cos.tuk_tuk_driver.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.LinearLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.adapter.AllVehicleAdapter
import cos.tuk_tuk_driver.databinding.ActivityAllVehicleBinding
import cos.tuk_tuk_driver.listener.MyButtonClickListener
import cos.tuk_tuk_driver.models.GetVehicleModal
import cos.tuk_tuk_driver.models.Vehicles
import cos.tuk_tuk_driver.utils.MyButton
import cos.tuk_tuk_driver.utils.MySwipeHelper
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


    fun getVehiclesList() {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()
            dataList.clear()

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
                                        intent.putExtra("from", "add")
                                        startActivity(intent)

                                        return
                                    }
                                    dataList.addAll(response.body()!!.vehicles)
                                    val adapter =
                                        AllVehicleAdapter(this@AllVehicleActivity, dataList)
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

    fun deleteVehicleData(id: String) {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.deleteVehicle(id, "")
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
                                Comman.makeToast(applicationContext, response.body()!!.message)
                                getVehiclesList()

                            } else {

                                Comman.makeToast(applicationContext, response.body()!!.message)

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
            intent.putExtra("from", "add")

            startActivity(intent)
        }

        var swipeHelper = object : MySwipeHelper(this, binding.vehicleRecyclerview, 200) {
            override fun instantiateMyButton(
                viewHolder: RecyclerView.ViewHolder,
                buffer: MutableList<MyButton>
            ) {

                buffer.add(
                    MyButton(this@AllVehicleActivity,
                        "Edit",
                        80,
                        R.drawable.ic_edit,
                        Color.parseColor("#FF8800"),
                        object : MyButtonClickListener {
                            override fun onclick(pos: Int) {
                                // Comman.makeToast(applicationContext, "Delete $pos")
                                val intent = Intent(
                                    this@AllVehicleActivity,
                                    AddVehicleDetailsActiivity::class.java
                                )

                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                                intent.putExtra("from", "update")
                                intent.putExtra("vehicleId", "" + dataList.get(pos).id)
                                intent.putExtra(
                                    "service_type_id",
                                    "" + dataList.get(pos).service_type_id
                                )
                                intent.putExtra(
                                    "service_color",
                                    "" + dataList.get(pos).service_color
                                )
                                intent.putExtra(
                                    "service_model",
                                    "" + dataList.get(pos).service_model
                                )
                                intent.putExtra("service_year", "" + dataList.get(pos).service_year)
                                intent.putExtra(
                                    "service_number",
                                    "" + dataList.get(pos).service_number
                                )
                                startActivity(intent)

                            }

                        })
                )

                buffer.add(
                    MyButton(this@AllVehicleActivity,
                        "Delete",
                        80,
                        R.drawable.ic_delete,
                        Color.parseColor("#E31A2B"),
                        object : MyButtonClickListener {
                            override fun onclick(pos: Int) {

                                if (dataList.get(pos).prime != 1) {
                                    deleteVehicleData("" + dataList.get(pos).id)
                                } else {

                                    Comman.makeToast(
                                        applicationContext,
                                        "You cannot delete prime vehicle "
                                    )

                                }

                            }

                        })
                )

            }

        }


    }
}
