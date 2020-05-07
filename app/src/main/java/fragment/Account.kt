package fragment

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.tuktuk.utils.Comman

import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.activity.*
import cos.tuk_tuk_driver.databinding.FragmentAccountBinding
import cos.tuk_tuk_driver.models.GetVehicleModal
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class Account : Fragment() {

    private val apiInterface = Comman.getApiToken()
    private var vehilce: LinearLayout? = null
    private var securityPrivacy: LinearLayout? = null
    private var about: LinearLayout? = null
    private var reportIssue: LinearLayout? = null

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

            vehilce = view.findViewById<LinearLayout>(R.id.vehilce)
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
            vehilce!!.setOnClickListener {
                getVehiclesList()

            }


        } catch (Ex: Exception) {

        }


        return view

    }


    private fun getVehiclesList() {

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
                                        val intent = Intent(
                                            context,
                                            AllVehicleActivity::class.java
                                        )
                                        intent.flags =
                                            Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                                        startActivity(intent)
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