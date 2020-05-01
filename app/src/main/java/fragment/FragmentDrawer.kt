package fragment

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.activity.AccountActivity
import cos.tuk_tuk_driver.activity.GetOtpActivity
import cos.tuk_tuk_driver.activity.HomeActivity
import cos.tuk_tuk_driver.models.RegisterModal
import cos.tuk_tuk_driver.utils.Prefs
import kotlinx.android.synthetic.main.fragment_drawer.view.*
import retrofit2.Call
import retrofit2.Response
import java.lang.Exception

/**
 * A simple [Fragment] subclass.
 */
class FragmentDrawer : Fragment() {

    private var mDrawerToggle: ActionBarDrawerToggle? = null
    private var mDrawerLayout: DrawerLayout? = null
    private var containerView: View? = null
    private var drawerListener: FragmentDrawerListener? = null

    private val apiInterface = Comman.getApi()


    fun FragmentDrawer() {}


    fun setDrawerListener(listener: FragmentDrawerListener?) {
        drawerListener = listener
    }

    fun setUp(fragmentId: Int, drawerLayout: DrawerLayout?, toolbar: Toolbar?) {
        containerView = activity!!.findViewById(fragmentId)
        mDrawerLayout = drawerLayout
        mDrawerToggle = object : ActionBarDrawerToggle(
            activity,
            drawerLayout,
            toolbar,
            R.drawable.ic_launcher_background,
            R.drawable.ic_launcher_background
        ) {
            override fun onDrawerOpened(drawerView: View) {
                super.onDrawerOpened(drawerView)
                activity!!.invalidateOptionsMenu()
            }

            override fun onDrawerClosed(drawerView: View) {
                super.onDrawerClosed(drawerView)
                activity!!.invalidateOptionsMenu()
            }

            override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                super.onDrawerSlide(drawerView, slideOffset)
                toolbar!!.alpha = 1 - slideOffset / 2
            }
        }
        mDrawerLayout!!.addDrawerListener(mDrawerToggle as ActionBarDrawerToggle)
        mDrawerLayout!!.post { (mDrawerToggle as ActionBarDrawerToggle).syncState() }


        (mDrawerToggle as ActionBarDrawerToggle).isDrawerIndicatorEnabled = false

        (mDrawerToggle as ActionBarDrawerToggle).setToolbarNavigationClickListener {
            mDrawerLayout!!.openDrawer(GravityCompat.START)

        }

        (mDrawerToggle as ActionBarDrawerToggle).setHomeAsUpIndicator(R.drawable.ic_hamburger)


    }


    interface FragmentDrawerListener {
        fun onDrawerItemSelected(view: View?, position: Int)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view: View = inflater.inflate(R.layout.fragment_drawer, container, false)

        view.logout.setOnClickListener {

            LogoutAlert(view)

        }
        view.account.setOnClickListener {

            val intent = Intent(context, AccountActivity::class.java)
            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        return view
    }

    private fun LogoutAlert(view: View) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(view.context, R.style.AlertDialogCustom)

        // Set the alert dialog title
        builder.setTitle("Logout")

        // Display a message on alert dialog
        builder.setMessage("Are you sure you want to logout?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES") { dialog, which ->
            // Do something when user press the positive button
            // doLogout()
            Prefs.clearSharedPreferences(DriverApp.context)
            val intent = Intent(
                context,
                GetOtpActivity::class.java
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

    private fun doLogout() {

        try {

            apiInterface!!.logout("23")
                .enqueue(object : retrofit2.Callback<RegisterModal> {
                    override fun onFailure(call: Call<RegisterModal>, t: Throwable) {

                        Comman.makeToast(context, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<RegisterModal>,
                        response: Response<RegisterModal>
                    ) {

                        try {

                            if (response.body()?.status!!) {

                                Comman.makeToast(context, response.body()!!.message)
                                val intent = Intent(
                                    context,
                                    GetOtpActivity::class.java
                                ) //not application context

                                intent.flags =
                                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

                                startActivity(intent)

                            } else {

                                Comman.makeToast(context, "Please try again later")
                            }


                        } catch (Ex: java.lang.Exception) {

                        }

                    }

                })

        } catch (Ex: Exception) {

        }

    }


}
