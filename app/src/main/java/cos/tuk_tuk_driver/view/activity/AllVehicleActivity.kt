package cos.tuk_tuk_driver.activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.adapter.AllVehicleAdapter
import cos.tuk_tuk_driver.databinding.ActivityAllVehicleBinding
import cos.tuk_tuk_driver.listener.MyButtonClickListener
import cos.tuk_tuk_driver.model.GetVehicleModal
import cos.tuk_tuk_driver.model.Vehicles
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.MyButton
import cos.tuk_tuk_driver.utils.MySwipeHelper
import cos.tuk_tuk_driver.utils.recylerviewswipe.RecyclerTouchListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVehicleActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAllVehicleBinding
    private val apiInterface = Comman.getApiToken()
    val dataList = ArrayList<Vehicles>()
    private var touchListener: RecyclerTouchListener? = null
    var adapter =
        AllVehicleAdapter(
            this@AllVehicleActivity,
            this@AllVehicleActivity
        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAllVehicleBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initAdviSwipeView()

    }

    override fun onResume() {
        super.onResume()
        try {
            initRecycler()
            touchListener?.let { binding.vehicleRecyclerview.addOnItemTouchListener(it) }
            onClickListners()
            getVehiclesList()
        } catch (Ex: Exception) {

        }
    }


    fun initAdviSwipeView() {
        touchListener = RecyclerTouchListener(this, binding.vehicleRecyclerview)
        touchListener!!.closeVisibleBG(object : RecyclerTouchListener.OnSwipeListener {
            override fun onSwipeOptionsClosed() {}
            override fun onSwipeOptionsOpened() {}
        })
        touchListener?.setClickable(object : RecyclerTouchListener.OnRowClickListener {
            override fun onRowClicked(position: Int) {}
            override fun onIndependentViewClicked(independentViewID: Int, position: Int) {}
        })
            ?.setSwipeOptionViews(
                R.id.delete,
                R.id.edit
            )
            ?.setSwipeable(
                R.id.outerLayout,
                R.id.rowBG,
                object : RecyclerTouchListener.OnSwipeOptionsClickListener {
                    override fun onSwipeOptionClicked(viewID: Int, position: Int) {
                        // Toast.makeText(getActivity(),"onSwipeOptionClicked",Toast.LENGTH_LONG).show();
                        when (viewID) {
                            R.id.delete -> {
                                if (dataList.get(position).prime != 1) {
                                    deleteVehicleData("" + dataList.get(position).id)
                                } else {
                                    Comman.makeToast(
                                        applicationContext,
                                        "You cannot delete prime vehicle "
                                    )
                                }
                            }
                            R.id.edit -> {
                                editFunction(position)
                            }
                        }
                    }
                })
    }


    fun editFunction(position: Int) {
        val intent = Intent(
            this@AllVehicleActivity,
            AddVehicleDetailsActiivity::class.java
        )

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK //or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("from", "update")
        intent.putExtra("vehicleId", "" + dataList.get(position).id)
        intent.putExtra(
            "service_type_id",
            "" + dataList.get(position).service_type_id
        )
        intent.putExtra(
            "service_color",
            "" + dataList.get(position).service_color
        )
        intent.putExtra(
            "service_model",
            "" + dataList.get(position).service_model
        )
        intent.putExtra("service_year", "" + dataList.get(position).service_year)
        intent.putExtra(
            "service_number",
            "" + dataList.get(position).service_number
        )
        startActivity(intent)


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

//                                if (response.body()!!.vehicles.isEmpty()) {
//                                    val intent = Intent(
//                                        this@AllVehicleActivity,
//                                        VehicleActivity::class.java
//                                    )
//                                    intent.putExtra("from", "listEmpty")
//                                    intent.flags =
//                                        Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
//                                    // intent.putExtra("from", "add")
//                                    startActivity(intent)
//
//                                    return
//                                }


                                if (response.body()!!.vehicles.size != 0) {
                                    binding.vehicleRecyclerview.visibility = View.VISIBLE
                                    binding.emptyView.visibility = View.GONE
                                    dataList.addAll(response.body()!!.vehicles)
                                    adapter.updateList(dataList)
                                } else {
                                    binding.vehicleRecyclerview.visibility = View.GONE
                                    binding.emptyView.visibility = View.VISIBLE
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
    private fun initRecycler() {
        binding.vehicleRecyclerview.layoutManager =
            LinearLayoutManager(this, LinearLayout.VERTICAL, false)
        binding.vehicleRecyclerview.adapter = adapter
    }


    private fun onClickListners() {
        binding.close.setOnClickListener {
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

//        var swipeHelper = object : MySwipeHelper(this, binding.vehicleRecyclerview, 200) {
//            override fun instantiateMyButton(
//                viewHolder: RecyclerView.ViewHolder,
//                buffer: MutableList<MyButton>
//            ) {
//
//                buffer.add(
//                    MyButton(this@AllVehicleActivity,
//                        "Edit",
//                        100,
//                        R.drawable.ic_edit,
//                        Color.parseColor("#FF8800"),
//                        object : MyButtonClickListener {
//                            override fun onclick(pos: Int) {
//                                // Comman.makeToast(applicationContext, "Delete $pos")
//                                val intent = Intent(
//                                    this@AllVehicleActivity,
//                                    AddVehicleDetailsActiivity::class.java
//                                )
//
//                                intent.flags =
//                                    Intent.FLAG_ACTIVITY_NEW_TASK //or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                                intent.putExtra("from", "update")
//                                intent.putExtra("vehicleId", "" + dataList.get(pos).id)
//                                intent.putExtra(
//                                    "service_type_id",
//                                    "" + dataList.get(pos).service_type_id
//                                )
//                                intent.putExtra(
//                                    "service_color",
//                                    "" + dataList.get(pos).service_color
//                                )
//                                intent.putExtra(
//                                    "service_model",
//                                    "" + dataList.get(pos).service_model
//                                )
//                                intent.putExtra("service_year", "" + dataList.get(pos).service_year)
//                                intent.putExtra(
//                                    "service_number",
//                                    "" + dataList.get(pos).service_number
//                                )
//                                startActivity(intent)
//
//                            }
//
//                        })
//                )
//
//                buffer.add(
//                    MyButton(this@AllVehicleActivity,
//                        "Delete",
//                        100,
//                        R.drawable.ic_delete,
//                        Color.parseColor("#E31A2B"),
//                        object : MyButtonClickListener {
//                            override fun onclick(pos: Int) {
//
//                                if (dataList.get(pos).prime != 1) {
//                                    deleteVehicleData("" + dataList.get(pos).id)
//                                } else {
//
//                                    Comman.makeToast(
//                                        applicationContext,
//                                        "You cannot delete prime vehicle "
//                                    )
//
//                                }
//
//                            }
//
//                        })
//                )
//
//            }
//
//        }

    }


}
