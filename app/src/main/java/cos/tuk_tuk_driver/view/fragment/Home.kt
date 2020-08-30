package fragment

import android.Manifest
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.Settings
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.volley.VolleyLog
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.mikhaellopez.circularprogressbar.CircularProgressBar
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.activity.HomeActivity
import cos.tuk_tuk_driver.model.GetpaymentModaal
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.ProgressBarAnimation
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
    lateinit var online: Switch
    lateinit var driverStatus: TextView
    lateinit var bottomSheetDialog: BottomSheetDialog
    lateinit var expandOnlineImage: ImageView
    lateinit var progressBar: ProgressBar
    private var mHandler: Handler? = null
    private var i = 0
    private var mRunnable: Runnable? = null
    lateinit var homeActivity: HomeActivity

    val apiInterface = Comman.getApiToken()

    companion object {
        const val Online = "You're online"
        const val Offline = "You're offline"
    }


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

        val view = inflater.inflate(R.layout.fragment_home, container, false)

        homeActivity = activity as HomeActivity

        online = view.findViewById(R.id.online)
        driverStatus = view.findViewById(R.id.Destination)
        expandOnlineImage = view.findViewById(R.id.online_expand_image)
        progressBar = view.findViewById(R.id.finding_trips_progressBar)

        online.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                setOnlineView()
            } else {
                setOfflineView()
            }
        }

        expandOnlineImage.setOnClickListener{

            Log.e("DRIVER STATUS", "CLicked")
            bottomSheetDialog = BottomSheetDialog(activity!!, R.style.BottomSheetDialogTheme)
            val bottomSheetView = LayoutInflater
                .from(context!!.applicationContext)
                .inflate(R.layout.bottom_sheet_my_day, null)

            bottomSheetView.findViewById<TextView>(R.id.go_offline_tv).setOnClickListener {
                bottomSheetDialog.dismiss()
                setOfflineView()
            }

            bottomSheetView.findViewById<ImageView>(R.id.close_expand_online).setOnClickListener {
                bottomSheetDialog.dismiss()
                setOnlineView()
            }

            bottomSheetView.findViewById<ConstraintLayout>(R.id.view).setOnClickListener {
                bottomSheetDialog.dismiss()
                view!!.findViewById<CardView>(R.id.cardview).visibility = View.GONE
                homeActivity.displaySuggestionView()
            }

            bottomSheetView.findViewById<ConstraintLayout>(R.id.add_destination_cell).setOnClickListener {
                setTripRequestView(4.0f,"4 mins","0.5 km")
            }

            bottomSheetView.findViewById<ConstraintLayout>(R.id.driving_preferences_cell).setOnClickListener {
                bottomSheetDialog.dismiss()
                setDrivingPreferencesView()
            }

            bottomSheetDialog.setContentView(bottomSheetView)
            bottomSheetDialog.show()

        }



        currentLocation()

        return view
    }

    private fun setTripRequestView(rating: Float,time: String,distance: String) {
        bottomSheetDialog?.dismiss()
        view!!.findViewById<CardView>(R.id.cardview).visibility = View.GONE
//        if(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT){
//            assert(view != null)
//            val parent = view?.parent as View
//            val layoutParams = parent.layoutParams as LinearLayout.LayoutParams
//            layoutParams.setMargins(
//                resources.getDimensionPixelSize(R.dimen.margin), // LEFT 14dp
//                0,
//                resources.getDimensionPixelSize(R.dimen.margin), // RIGHT 14dp
//                resources.getDimensionPixelSize(R.dimen.margin) // BOTTOM 14dp
//            )
//            parent.layoutParams = layoutParams
//        }

        bottomSheetDialog = BottomSheetDialog(activity!!, R.style.BottomSheetDialogTheme)
        val tripRequestView = LayoutInflater
            .from(context!!.applicationContext)
            .inflate(R.layout.view_trip_request, null)

        tripRequestView.findViewById<TextView>(R.id.trip_request_rating_tv).text = rating.toString()

        tripRequestView.findViewById<TextView>(R.id.trip_request_time_tv).text = time

        tripRequestView.findViewById<TextView>(R.id.trip_request_distance_tv).text = distance

        val progressBar = tripRequestView.findViewById<CircularProgressBar>(R.id.circularProgressBar)
        progressBar.progress = 0f

        bottomSheetDialog.setContentView(tripRequestView)
        bottomSheetDialog.show()

        try {
            i = 0
            mHandler = Handler(Looper.getMainLooper())
            startTripRequestTimer(progressBar!!)

        } catch (Ex: Exception){
            Log.e("Handler Error","Error lopping "+Ex.printStackTrace())
        }

    }

    private fun startTripRequestTimer(progressBar: CircularProgressBar) {
        mRunnable = Runnable {
            try {
                if (i < 100) {

                    val anim = ProgressBarAnimation(
                        progressBar,
                        progressBar.progress,
                        i.toFloat()
                    )

                    anim.duration = 3000
                    progressBar.startAnimation(anim)
                    i += 100/30
                    if(i>100)
                        i = 100
                    //recursion :: calling same method util condition false
                    startTripRequestTimer(progressBar)
                } else{
                    val anim = ProgressBarAnimation(
                        progressBar,
                        progressBar.progress,
                        i.toFloat()
                    )
                    anim.duration = 3000
                    progressBar.startAnimation(anim)

                    bottomSheetDialog.dismiss()
                    setOnlineView()
                }
            } catch (Ex: Exception){
                Log.e("Runnable Exception","Progress runnable error"+Ex.printStackTrace())
            }
        }
        mHandler!!.postDelayed(mRunnable!!, 0)
    }

    private fun setDrivingPreferencesView() {
        bottomSheetDialog = BottomSheetDialog(activity!!, R.style.BottomSheetDialogTheme)
        val drivingPreferencesView = LayoutInflater
            .from(context!!.applicationContext)
            .inflate(R.layout.view_driving_preferences, null)

        drivingPreferencesView.findViewById<ImageView>(R.id.cancel_driving_preferences).setOnClickListener {
            bottomSheetDialog.dismiss()
            setOnlineView()
        }

        drivingPreferencesView.findViewById<ConstraintLayout>(R.id.drop_off_cell).setOnClickListener {

        }

        drivingPreferencesView.findViewById<ConstraintLayout>(R.id.rider_rating_cell).setOnClickListener {

        }

        drivingPreferencesView.findViewById<Switch>(R.id.accept_cash_switch).setOnClickListener {

        }

        drivingPreferencesView.findViewById<Button>(R.id.save_button).setOnClickListener {

        }

        drivingPreferencesView.findViewById<Button>(R.id.reset_button).setOnClickListener {

        }

        bottomSheetDialog.setContentView(drivingPreferencesView)
        bottomSheetDialog.show()
    }

    private fun setFindingTripsView() {
        view!!.findViewById<CardView>(R.id.finding_trip).visibility = View.VISIBLE
        try {
            i=0
            mHandler = Handler(Looper.getMainLooper())
            runTimer(progressBar)
        } catch (Ex: Exception) {
            Log.e("Handler Error","Error lopping "+Ex.printStackTrace())
        }
    }

    private fun runTimer(progressBar: ProgressBar) {
        mRunnable = Runnable {
            try {
                if (i < 100) {

                    val anim = ProgressBarAnimation(
                        progressBar,
                        progressBar.progress.toFloat(),
                        i.toFloat()
                    )

                    anim.duration = 400
                    progressBar.startAnimation(anim)
                    i += 33

                    //recursion :: calling same method util condition false
                    runTimer(progressBar)
                } else {
                    setOnlineView()
                }
            } catch (Ex: Exception){
                Log.e("Runnable Exception","Progress runnable error"+Ex.printStackTrace())
            }
        }
        mHandler!!.postDelayed(mRunnable!!, 500)
    }

    private fun setOfflineView() {
        makeAvailable("offline")
        driverStatus.text = Offline
        expandOnlineImage.visibility = View.GONE
        online.visibility = View.VISIBLE
        online.isChecked = false
    }

    private fun setOnlineView() {
        view!!.findViewById<CardView>(R.id.finding_trip).visibility = View.GONE
        view!!.findViewById<CardView>(R.id.cardview).visibility = View.VISIBLE
        makeAvailable("online")
        driverStatus.text = Online
        expandOnlineImage.visibility = View.VISIBLE
        online.visibility = View.GONE
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

    override fun onResume() {
        super.onResume()
        currentLocation()
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

                       // showSettingsDialog()

                        // permission is denied permenantly, navigate user to app settings
                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                  //  token!!.continuePermissionRequest()
                }
            }).check()
    }

    override fun onMapReady(map: GoogleMap?) {

        //System.err.println("OnMapReady start")
        mMap = map as GoogleMap

        Dexter.withActivity(activity)
            .withPermissions(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
            .withListener(object : MultiplePermissionsListener {
                override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                    if (report.areAllPermissionsGranted()) { // Use fields to define the data types to return.


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

                        currentLocation()
                    }
                    // check for permanent denial of any permission

                    if (report.isAnyPermissionPermanentlyDenied) {
                        if (ContextCompat.checkSelfPermission(
                                context!!,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                                context!!,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) == PackageManager.PERMISSION_GRANTED
                        ) {
                            mMap = map

                            val sydney = LatLng(initial_latitude, initial_longitude)

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
                            currentLocation()

                            return
                        } else {
                            // show alert dialog navigating to Settings
                            showSettingsDialog()
                        }


                    }
                }

                override fun onPermissionRationaleShouldBeShown(
                    permissions: List<PermissionRequest?>?,
                    token: PermissionToken?
                ) {
                  //  token!!.continuePermissionRequest()

                }
            }).check()

    }

    /**
     * Showing Alert Dialog with Settings option
     * Navigates user to app settings
     * NOTE: Keep proper title and message depending on your app
     */
    private fun showSettingsDialog() {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(context!!, R.style.AlertDialogCustom)

        builder.setTitle("Need Permissions")
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("GOTO SETTINGS") { dialog, which ->
            // Do something when user press the positive button
            dialog.cancel()
            openSettings()

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("Cancel") { dialog, which ->
            dialog.cancel()
            activity!!.finish()
        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()

    }

    // navigating user to app settings
    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", context!!.packageName, null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    fun unhideBottomView() {
        view!!.findViewById<CardView>(R.id.cardview).visibility = View.VISIBLE
    }


}
