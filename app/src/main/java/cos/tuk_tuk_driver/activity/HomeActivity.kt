package cos.tuk_tuk_driver.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.FragmentManager
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityHomeBinding
import fragment.FragmentDrawer
import fragment.Home

class HomeActivity : AppCompatActivity(), FragmentDrawer.FragmentDrawerListener {

    var mToolbar: Toolbar? = null
    var drawerFragment: FragmentDrawer? = null
    var drawerLayout: DrawerLayout? = null
    var fragmentManager: FragmentManager? = null

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
            fragmentManager!!.beginTransaction().replace(R.id.container_body, home, "home.getTag()").commit()
            drawerFragment = supportFragmentManager.findFragmentById(R.id.fragment_navigation_drawer) as FragmentDrawer?
            drawerFragment!!.setUp(R.id.fragment_navigation_drawer, findViewById<View>(R.id.drawer) as DrawerLayout, mToolbar)
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
}

    override fun onDrawerItemSelected(view: View?, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


}
