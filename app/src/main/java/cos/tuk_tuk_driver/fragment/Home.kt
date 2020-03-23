package cos.tuk_tuk_driver.fragment

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
        // Inflate the layout for this cos.tuk_tuk_driver.fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onMapReady(map: GoogleMap?) {

        //System.err.println("OnMapReady start")
        mMap = map as GoogleMap

        val sydney = LatLng(initial_latitude, initial_longitude)
        mMap.addMarker(MarkerOptions().position(sydney).title(initial_marker))
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(initial_latitude, initial_longitude), 12.0f))

        // Toast.makeText(this.context, "OnMapReady end", Toast.LENGTH_LONG).show()

        /* map?.let {
             mMap = it
         }*/
    }
}
