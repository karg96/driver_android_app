package cos.tuk_tuk_driver.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityHomeBinding
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

        drawerLayout = findViewById(R.id.drawer)
        mToolbar = findViewById(R.id.toolbar)
//        title = findViewById(R.id.title);
        //        title = findViewById(R.id.title);
        setSupportActionBar(mToolbar)
        supportActionBar!!.title = null
        supportActionBar!!.setDisplayShowHomeEnabled(true)

        payment = findViewById<LinearLayout>(R.id.payment)
        home = findViewById<LinearLayout>(R.id.home)
        account = findViewById<LinearLayout>(R.id.account)
        notification = findViewById<LinearLayout>(R.id.notification)
        payment!!.setOnClickListener(this)
        home!!.setOnClickListener(this)
        account!!.setOnClickListener(this)
        notification!!.setOnClickListener(this)

    }

    override fun onDrawerItemSelected(view: View?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    override fun onClick(p0: View?) {

        drawerLayout!!.closeDrawers()

        when (p0!!.id) {
            R.id.payment -> {

                binding.Title.text = "Payment"

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
