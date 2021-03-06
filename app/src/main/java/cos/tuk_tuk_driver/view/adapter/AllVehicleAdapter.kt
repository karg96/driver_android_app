package cos.tuk_tuk_driver.adapter

import android.app.ProgressDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.activity.AllVehicleActivity
import cos.tuk_tuk_driver.model.GetVehicleModal
import cos.tuk_tuk_driver.model.Vehicles
import cos.tuk_tuk_driver.services.ApiInterface
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Constants
import cos.tuk_tuk_driver.utils.Prefs
import cos.tuk_tuk_driver.utils.URLHelper
import kotlinx.android.synthetic.main.vehicle_d.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AllVehicleAdapter(
    val context: Context,
    val allVehicleActivity: AllVehicleActivity
) :
    RecyclerView.Adapter<AllVehicleAdapter.ViewHolder>() {

    val apiInterface: ApiInterface?
    var selectedPosition  = -1
    var dataList = ArrayList<Vehicles>()
    var checkedPosition = 0

    init {

        apiInterface = Comman.getApiToken()

    }

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun getItemId(position: Int): Long {
        return super.getItemId(position)
    }


    override fun getItemViewType(position: Int): Int {
        return super.getItemViewType(position)
    }
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): AllVehicleAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(DriverApp.context).inflate(
                R.layout.vehicle_d,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var data = dataList.get(position)

        holder.vehicleName.text = data.serviceDetail!!.name
        holder.vehicleDes.text = "${data.service_model} / ${data.service_number}"

        if (data.serviceDetail.image != null) {

            val circularProgressDrawable = CircularProgressDrawable(context)
            circularProgressDrawable.strokeWidth = 5f
            circularProgressDrawable.centerRadius = 30f
            circularProgressDrawable.start()
            val request = RequestOptions()
            request.placeholder(circularProgressDrawable)
            request.error(R.drawable.taxi)

            Glide.with(context).load(URLHelper.BaseUrl + "uploads" + data.serviceDetail.image)
                .apply(request)
                .into(holder.image)

        }


            if (data.prime == 1) {
                holder.check.visibility = View.VISIBLE
            }else{
                holder.check.visibility = View.GONE
            }

        holder.outerView.setOnClickListener {
            // if (!holder.makePrime.isRightSwipeEnabled) {
            selectedPosition =selectedPosition;
            val builder = AlertDialog.Builder(allVehicleActivity, R.style.AlertDialogCustom)
            //set message for alert dialog
            if (data.prime == 1) {
                builder.setMessage("Are you sure! Do you want to unset "+data.serviceDetail!!.name+" vehicle from prime?")
            } else if (data.prime == 0) {
                builder.setMessage("Are you sure! Do you want to make "+data.serviceDetail!!.name+" vehicle as a prime?")
            }
//            builder.setIcon(android.R.drawable.ic_dialog_alert)

            //performing positive action
            builder.setPositiveButton("Yes") { dialogInterface, which ->
                if (data.prime == 1) {
                    UnsetPrime(data.id, holder.check)
                } else if (data.prime == 0) {
                    MakePrime(data.id, holder.check)
                }
            }

            //performing negative action
            builder.setNegativeButton("No") { dialogInterface, which ->
            }
            // Create the AlertDialog
            val alertDialog: AlertDialog = builder.create()
            // Set other dialog properties
            alertDialog.setCancelable(false)
            alertDialog.show()

            //  }
        }
    }



    public fun updateList(dataList: ArrayList<Vehicles>)
    {
        this.dataList=dataList
        notifyDataSetChanged()
    }


    private fun MakePrime(id: Int, check: ImageView) {

        try {

            val dialog = ProgressDialog(context)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.makePrime("$id")
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
                              //  check.visibility = View.VISIBLE
                                Comman.makeToast(context, response.body()!!.message)
                                Prefs.putKey(context, Constants.PRIME, Constants.SET_PRIME)
                                if (context is AllVehicleActivity) {
                                    context.getVehiclesList()
                                }

                            } else {

                                Comman.makeToast(context, response.body()!!.message)

                            }

                        } catch (Ex: Exception) {

                        }

                    }

                })
        } catch (Ex: Exception) {
            Comman.makeToast(context, "Please try again later")
        }

    }

    private fun UnsetPrime(id: Int, check: ImageView) {

        try {

            val dialog = ProgressDialog(context)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.unsetPrime("$id")
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
                               // check.visibility = View.GONE
                                Comman.makeToast(context, response.body()!!.message)
                                Prefs.putKey(context, Constants.PRIME, Constants.UNSET_PRIME)
                                if (context is AllVehicleActivity) {
                                    context.getVehiclesList()
                                }
                            } else {

                                Comman.makeToast(context, response.body()!!.message)

                            }

                        } catch (Ex: Exception) {

                        }
                    }
                })
        } catch (Ex: Exception) {
            Comman.makeToast(context, "Please try again later")
        }

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val vehicleName = view.vehicleName
        val vehicleDes = view.vehicleDec
        val check = view.check
        val makePrime = view.makePrime
        val outerView= view.outerLayout
        val image = view.image
//        val delete = view.delete
//        val edit = view.edit




    }

}