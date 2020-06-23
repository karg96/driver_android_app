package fragment

import android.Manifest
import android.app.ProgressDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.android.volley.VolleyLog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.models.GetpaymentModaal
import cos.tuk_tuk_driver.utils.Comman
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var initial_latitude = 0.0
    var initial_longitude = 0.0
    var initial_marker = "Seed nay"
    lateinit var online: Switch

    val apiInterface = Comman.getApiToken()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        google_map.onCreate(savedInstanceState)
        google_map.onResume()
        google_map.getMapAsync(this)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        var view = inflater.inflate(R.layout.fragment_home, container, false)

        online = view.findViewById(R.id.online)
        val driverStatus = view.findViewById<TextView>(R.id.Destination)

        online.setOnCheckedChangeListener(object : CompoundButton.OnCheckedChangeListener {
            override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
                if (online.isChecked) {
                    makeAvailable("active")
                    driverStatus.text = "You're online"
                } else {
                    makeAvailable("offline")
                    driverStatus.text = "You're offline"

                }
            }
        })

        currentLocation()

        return view
    }


    private fun makeAvailable(s: String) {

        try {

            val dialog = ProgressDialog(context)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.available(s)
                .enqueue(object : Callback<GetpaymentModaal> {
                    override fun onFailure(call: Call<GetpaymentModaal>, t: Throwable) {
                        dialog.dismiss()
                        Comman.makeToast(context, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<GetpaymentModaal>,
                        response: Response<GetpaymentModaal>
                    ) {
                        try {

                            dialog.dismiss()

                            if (response.body()!!.status) {
                                Comman.makeToast(context, response.body()!!.message)

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

    private fun currentLocation() {

        Dexter.withActivity(activity)
            .withPermissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) { // Use fields to define the data types to return.
                        val placeFields =
                            Arrays.asList(
                                Place.Field.ID,
                                Place.Field.NAME,
                                Place.Field.LAT_LNG,
                                Place.Field.ADDRESS
                            )
                        // Use the builder to create a FindCurrentPlaceRequest.
                        val request =
                            FindCurrentPlaceRequest.newInstance(placeFields)
                        //   Places.initialize(getContext(), getString(R.string.google_maps_key), Locale.US);
                        if (!Places.isInitialized()) {
                            context?.let {
                                Places.initialize(
                                    it,
                                    getString(R.string.google_maps_key),
                                    Locale.US
                                )
                            }
                        }
                        if (context?.let {
                                ActivityCompat.checkSelfPermission(
                                    it,
                                    Manifest.permission.ACCESS_FINE_LOCATION
                                )
                            } != PackageManager.PERMISSION_GRANTED
                        ) { // TODO: Consider calling
//    ActivityCompat#requestPermissions
                            return
                        }
                        val placesClient = Places.createClient(context!!)
                        val placeResponse =
                            placesClient.findCurrentPlace(request)
                        placeResponse.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                val response = task.result!!
                                val latLngVal =
                                    response.placeLikelihoods[0].place.latLng!!
                                initial_latitude = latLngVal.latitude
                                initial_longitude = latLngVal.longitude

                                val sydney = LatLng(initial_latitude, initial_longitude)
                                /*mMap.addMarker(
                                    MarkerOptions().position(sydney).title(initial_marker).icon(
                                        BitmapDescriptorFactory.fromResource(R.drawable.current_location)
                                    )
                                )*/
                                mMap.isMyLocationEnabled = true

                                mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
                                mMap.animateCamera(
                                    CameraUpdateFactory.newLatLngZoom(
                                        LatLng(
                                            initial_latitude,
                                            initial_longitude
                                        ), 12.0f
                                    )
                                )


                            } else {
                                val exception = task.exception
                                if (exception is ApiException) {
                                    Log.e(
                                        VolleyLog.TAG,
                                        "Place not found: " + exception.statusCode
                                    )
                                }
                            }
                        }
                    }
                    // check for permanent denial of any permission
                    if (report.isAnyPermissionPermanentlyDenied) {
                        Toast.makeText(context, "Please allow permission", Toast.LENGTH_SHORT)
                            .show()
                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                }
            }).check()
    }

    override fun onMapReady(map: GoogleMap?) {

        //System.err.println("OnMapReady start")
        mMap = map as GoogleMap

        val sydney = LatLng(initial_latitude, initial_longitude)
        /*mMap.addMarker(
            MarkerOptions().position(sydney).title(initial_marker).icon(
                BitmapDescriptorFactory.fromResource(R.drawable.current_location)
            )
        )*/
        mMap.isMyLocationEnabled = true

        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(
            CameraUpdateFactory.newLatLngZoom(
                LatLng(
                    initial_latitude,
                    initial_longitude
                ), 12.0f
            )
        )

        // Toast.makeText(this.context, "OnMapReady end", Toast.LENGTH_LONG).show()

        /* map?.let {
             mMap = it
         }*/
    }
}
