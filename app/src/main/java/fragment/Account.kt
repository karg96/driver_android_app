package fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.DriverApp

import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.activity.*
import cos.tuk_tuk_driver.models.GetVehicleModal
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class Account : Fragment() {

    private val apiInterface = Comman.getApiToken()
    private var vehilce: RelativeLayout? = null
    private var securityPrivacy: LinearLayout? = null
    private var about: LinearLayout? = null
    private var reportIssue: LinearLayout? = null
    private var payment: LinearLayout? = null
    private var deleteAccount: TextView? = null
    private var vehicleText: TextView? = null

//    private var fragmentAccountBinding: FragmentAccountBinding? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var view = inflater.inflate(R.layout.fragment_account, container, false)

        /*  val binding = fragmentAccountBinding.bind(view)

          fragmentAccountBinding = binding*/

        try {

            payment = view.findViewById<LinearLayout>(R.id.payment)
            vehilce = view.findViewById<RelativeLayout>(R.id.vehilce)
            vehicleText = view.findViewById<TextView>(R.id.vehicleText)
            deleteAccount = view.findViewById<TextView>(R.id.deleteAccount)
            securityPrivacy = view.findViewById<LinearLayout>(R.id.securityPrivacy)
            about = view.findViewById<LinearLayout>(R.id.about)
            reportIssue = view.findViewById<LinearLayout>(R.id.reportIssue)


            securityPrivacy!!.setOnClickListener {
                val intent = Intent(context, SecurityAndPrivacy::class.java)
                Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                startActivity(intent)
            }
            about!!.setOnClickListener {
                val intent = Intent(context, AboutActivity::class.java)
                Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                startActivity(intent)
            }
            reportIssue!!.setOnClickListener {

                val intent = Intent(context, ReportIssueActivity::class.java)
                Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                startActivity(intent)
            }
            payment!!.setOnClickListener {

                val intent = Intent(context, PaymentTypeActivity::class.java)
                Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                startActivity(intent)
            }
            vehilce!!.setOnClickListener {
                getVehiclesList(1)

            }
            deleteAccount!!.setOnClickListener {
                DeleteAlerts(view)

            }
            getVehiclesList(0)


        } catch (Ex: Exception) {

        }


        return view

    }

    fun DeleteAlerts(view: View) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(view.context, R.style.AlertDialogCustom)

        // Set the alert dialog title
        builder.setTitle("Delete")

        // Display a message on alert dialog
        builder.setMessage("Are you sure want to delete?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES") { dialog, which ->
            // Do something when user press the positive button
            // doLogout()
            Prefs.clearSharedPreferences(DriverApp.context)
            val intent = Intent(
                context,
                WelcomeActivity::class.java
            ) //not application context
            intent.putExtra("from", "register")

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No") { dialog, which ->

        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }


    private fun getVehiclesList(status: Int) {

        try {

            val dialog = ProgressDialog(context)
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
                                            context,
                                            VehicleActivity::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                                        startActivity(intent)
                                        return
                                    } else {

                                        if (status == 0) {
                                            for (x in 0..response.body()!!.vehicles.size) {
                                                if (response.body()!!.vehicles.get(x).prime == 1) {
                                                    vehicleText!!.visibility = View.GONE
                                                }
                                            }
                                        }

                                        if (status == 1) {
                                            val intent = Intent(
                                                context,
                                                AllVehicleActivity::class.java
                                            )
                                            intent.flags =
                                                Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                                            startActivity(intent)
                                        }

                                    }

                                }
                            } else {

                                Comman.makeToast(context, "Please try again later")

                            }

                        } catch (Ex: Exception) {

                        }

                    }

                })
        } catch (Ex: Exception) {
            Comman.makeToast(context, "Please try again later")

        }

    }

}
