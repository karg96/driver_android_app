package cos.tuk_tuk_driver.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.activity.NotificationActivity
import kotlinx.android.synthetic.main.notification_d.view.*

class NotificationAdapter(
    notificationActivity: NotificationActivity
) :
    RecyclerView.Adapter<NotificationAdapter.ViewHolder>() {

    // Gets the number of animals in the list
    override fun getItemCount(): Int {
        return 20
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): NotificationAdapter.ViewHolder {
        return ViewHolder(
            LayoutInflater.from(DriverApp.context).inflate(
                R.layout.notification_d,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //   holder.tvAnimalType.text = items.get(position)

    }


    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        // Holds the TextView that will add each animal to
        val tvAnimalType = view.notification
    }

}