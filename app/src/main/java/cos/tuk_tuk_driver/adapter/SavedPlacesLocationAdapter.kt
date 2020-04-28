package cos.tuk_tuk_driver.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.DriverApp.Companion.context
import cos.tuk_tuk_driver.R
import kotlinx.android.synthetic.main.search_location_d.view.*

class SavedPlacesLocationAdapter(
    context: Context
) :
    RecyclerView.Adapter<SavedPlacesLocationAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return 5
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavedPlacesLocationAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(DriverApp.context).inflate(
                R.layout.search_location_d,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //   holder.tvAnimalType.text = items.get(position)
    /*    holder.location.setOnClickListener {
            val intent = Intent(
                context,
                SavedLocationActivity::class.java
            ) //not application context

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK *//*or Intent.FLAG_ACTIVITY_CLEAR_TASK*//*
            context?.startActivity(intent)
        }
*/
    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val location = view.location
    }

}