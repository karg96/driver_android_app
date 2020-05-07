package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityHomeBinding
import cos.tuk_tuk_driver.utils.Prefs
import fragment.Account
import fragment.FragmentDrawer
import fragment.Home

class HomeActivity : AppCompatActivity(), FragmentDrawer.FragmentDrawerListener,
    View.OnClickListener {


    var mToolbar: Toolbar? = null
    var drawerFragment: FragmentDrawer? = null
    var drawerLayout: DrawerLayout? = null
    var fragmentManager: FragmentManager? = null
    private var payment: LinearLayout? = null
    private var home: LinearLayout? = null
    private var account: LinearLayout? = null
    private var notification: LinearLayout? = null
    private var Image: ImageView? = null
    private var DriverName: TextView? = null
    private var driverRating: TextView? = null
    private var driveName: String? = ""
    private var driveImage: String? = ""
    private var driveRating: String? = ""

    lateinit var binding: ActivityHomeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_home)

        try {

            init()

            val home = Home()
            fragmentManager = supportFragmentManager
            fragmentManager!!.beginTransaction().replace(R.id.container_body, home, "home.getTag()")
                .commit()
            drawerFragment =
                supportFragmentManager.findFragmentById(R.id.fragment_navigation_drawer) as FragmentDrawer?
            drawerFragment!!.setUp(
                R.id.fragment_navigation_drawer,
                findViewById<View>(R.id.drawer) as DrawerLayout,
                mToolbar
            )
            drawerFragment!!.setDrawerListener(this)

        } catch (Ex: Exception) {

        }
    }

    private fun init() {

        driveName = Prefs.getKey(applicationContext, "driveName")
        driveImage = Prefs.getKey(applicationContext, "driveImage")
        driveRating = Prefs.getKey(applicationContext, "driveRating")

        drawerLayout = findViewById(R.id.drawer)
        mToolbar = findViewById(R.id.toolbar)
//        title = findViewById(R.id.title);
        //        title = findViewById(R.id.title);
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        Image = findViewById<ImageView>(R.id.Image)
        DriverName = findViewById<TextView>(R.id.DriverName)
        driverRating = findViewById<TextView>(R.id.driverRating)
        payment = findViewById<LinearLayout>(R.id.payment)
        home = findViewById<LinearLayout>(R.id.home)
        account = findViewById<LinearLayout>(R.id.account)
        notification = findViewById<LinearLayout>(R.id.notification)
        payment!!.setOnClickListener(this)
        home!!.setOnClickListener(this)
        account!!.setOnClickListener(this)
        notification!!.setOnClickListener(this)
        DriverName!!.text = Comman.singleCapsName(driveName)
        driverRating!!.text = Comman.singleCapsName(driveRating)

        Comman.setCircleImage(applicationContext, driveImage, Image)

    }

    override fun onDrawerItemSelected(view: View?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onClick(p0: View?) {

        drawerLayout!!.closeDrawers()

        when (p0!!.id) {
            R.id.payment -> {

                // binding.Title.text = "Payment"

                //changeView(Payment())
            }

            R.id.home -> {
                binding.Title.text = "Home"

                changeView(Home())

            }
            R.id.account -> {
                binding.Title.text = "Account"

                changeView(Account())

            }

            R.id.notification -> {
                val intent = Intent(this@HomeActivity, NotificationActivity::class.java)
                Intent.FLAG_ACTIVITY_NEW_TASK /*or Intent.FLAG_ACTIVITY_CLEAR_TASK*/
                startActivity(intent)

            }

        }
    }

    fun changeView(fragment: Fragment) {
        fragmentManager = supportFragmentManager
        fragmentManager!!.beginTransaction().replace(R.id.container_body, fragment, "home.getTag()")
            .commit()
    }


}
