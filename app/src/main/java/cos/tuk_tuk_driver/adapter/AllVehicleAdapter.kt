package cos.tuk_tuk_driver.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.models.Vehicles
import kotlinx.android.synthetic.main.notification_d.view.*
import kotlinx.android.synthetic.main.vehicle_d.view.*

class AllVehicleAdapter(
    val context: Context,
    val dataList: ArrayList<Vehicles>
) :
    RecyclerView.Adapter<AllVehicleAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return dataList.size
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

        holder.vehicleName.text = data.service_model
        holder.vehicleDes.text = data.service_number

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val vehicleName = view.vehicleName
        val vehicleDes = view.vehicleDec
    }

}