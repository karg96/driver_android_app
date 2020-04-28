package fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import cos.tuk_tuk_driver.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.online_bottom_sheet.*

/**
 * A simple [Fragment] subclass.
 */
class Home : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    var initial_latitude = -34.0
    var initial_longitude = 151.0
    var initial_marker = "Seed nay"


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

//        var bottomSheetBehavior = BottomSheetBehavior.from(bottom_sheet_layout)
//
//        bottomSheetBehavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                // React to state change
//                when (newState) {
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//                    }
//                    BottomSheetBehavior.STATE_EXPANDED -> {
//                    }
//                    BottomSheetBehavior.STATE_COLLAPSED -> {
//                    }
//                    BottomSheetBehavior.STATE_DRAGGING -> {
//                    }
//                    BottomSheetBehavior.STATE_SETTLING -> {
//                    }
//                }            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                // React to dragging events
//            }
//        })
//
//        cardview.setOnClickListener {
//            if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED)
//            } else {
//                bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED)
//            }
//        }
//
//        cardview.setOnClickListener {
//            val view = layoutInflater.inflate(R.layout.online_bottom_sheet, null)
//            val dialog =  BottomSheetDialog(context)
//            dialog.setContentView(view)
//            dialog.show()
//        }


        return view
    }

    override fun onMapReady(map: GoogleMap?) {

        //System.err.println("OnMapReady start")
        mMap = map as GoogleMap

        val sydney = LatLng(initial_latitude, initial_longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title(initial_marker))
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
