package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityAddDocumentBinding
import cos.tuk_tuk_driver.models.Documents
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDocumentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDocumentBinding
    private val apiInterface = Comman.getApiToken()
    private var UserName: String = ""
    private var IsApproved: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            UserName = intent.getStringExtra("name")
            init()

            binding.name.text = "Hi, " + UserName

        } catch (Ex: Exception) {

        }

    }

    override fun onResume() {
        super.onResume()
        GetUploadDocuments()

    }

    private fun GetUploadDocuments() {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.getUploadDocs()
                .enqueue(object : Callback<Documents> {
                    override fun onFailure(call: Call<Documents>, t: Throwable) {
                        dialog.dismiss()

                    }

                    override fun onResponse(call: Call<Documents>, response: Response<Documents>) {
                        try {

                            dialog.dismiss()

                            if (response.code() == 200) {

                                if (response.body()!!.driverDocuments.size != 0) {

                                    for (x in 0 until response.body()!!.driverDocuments.size) {

                                        if (response.body()!!.driverDocuments.get(x).document_id == "1") {
                                            binding.driverCheck.visibility = View.VISIBLE

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.driverCheck.setImageResource(R.drawable.ic_check_green)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "REJECT",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRED",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRETODAY",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.driverCheck.setImageResource(R.drawable.ic_check_red)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "Processing",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRETODAY",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.driverCheck.setImageResource(R.drawable.ic_check_yellow)
                                            }
                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "8") {
                                            binding.identyCheck.visibility = View.VISIBLE

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.identyCheck.setImageResource(R.drawable.ic_check_green)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "REJECT",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRED",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRETODAY",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.identyCheck.setImageResource(R.drawable.ic_check_red)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "Processing",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRETODAY",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.identyCheck.setImageResource(R.drawable.ic_check_yellow)
                                            }

                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "9") {
                                            binding.photoCheck.visibility = View.VISIBLE

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.photoCheck.setImageResource(R.drawable.ic_check_green)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "REJECT",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRED",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRETODAY",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.photoCheck.setImageResource(R.drawable.ic_check_red)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "Processing",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRETODAY",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.photoCheck.setImageResource(R.drawable.ic_check_yellow)
                                            }
                                        }

                                        if (response.body()!!.driverDocuments.get(x).status.equals(
                                                "approved",
                                                ignoreCase = true
                                            )
                                        ) {
                                            IsApproved += 1
                                        }

                                    }

                                }

                                /*if (response.body()!!.vehicleDocuments.size != 0) {
                                    for (x in 0 until response.body()!!.vehicleDocuments.size) {

                                        if (response.body()!!.vehicleDocuments.get(x).document_id == "5") {

                                            binding.registerationCheck.visibility = View.VISIBLE

                                        }

                                    }
                                }*/

                                if (binding.driverCheck.visibility == View.VISIBLE
                                    && binding.photoCheck.visibility == View.VISIBLE
                                    && binding.identyCheck.visibility == View.VISIBLE
                                ) {

                                    binding.btnNext.visibility = View.VISIBLE
                                }

                            } else {
                                Comman.makeToast(applicationContext, "Please try again later")

                            }

                        } catch (Ex: Exception) {

                        }
                    }

                })

        } catch (Ex: java.lang.Exception) {

        }

    }

    private fun init() {


        binding.cardLicense.setOnClickListener {

            val intent = Intent(this@AddDocumentActivity, AddDrivingLicenseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.cardRegDocument.setOnClickListener {

            val intent =
                Intent(this@AddDocumentActivity, AddVehicleRegistrationActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.cardIdentity.setOnClickListener {

            val intent = Intent(this@AddDocumentActivity, AddIdentityCardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

        binding.cardPhoto.setOnClickListener {

            val intent = Intent(this@AddDocumentActivity, AddPhotoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        binding.btnNext.setOnClickListener {

            if (IsApproved == 3) {
                val intent = Intent(
                    this@AddDocumentActivity,
                    HomeActivity::class.java
                )
                intent.flags =
                    Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent)
            } else {
                Comman.makeToast(
                    applicationContext,
                    "Your account need some attentions."
                )
            }


        }

        binding.signOut.setOnClickListener {

            LogoutAlert();
        }


    }

    private fun LogoutAlert() {

        // Initialize a new instance of
//        val builder = AlertDialog.Builder(binding.signOut, R.style.AlertDialogCustom)
//
//        // Set the alert dialog title
//        builder.setTitle("Logout")
//
//        // Display a message on alert dialog
//        builder.setMessage("Are you sure you want to logout?")
//
//        // Set a positive button and its click listener on alert dialog
//        builder.setPositiveButton("YES") { dialog, which ->
            // Do something when user press the positive button
            // doLogout()
            Prefs.clearSharedPreferences(DriverApp.context)
            val intent = Intent(
                this,
                GetOtpActivity::class.java
            ) //not application context
//            intent.putExtra("from", "register")

            intent.flags =
                Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

            startActivity(intent)
//        }
//
//
//        // Display a negative button on alert dialog
//        builder.setNegativeButton("No") { dialog, which ->
//
//        }
//
//
//        // Finally, make the alert dialog using builder
//        val dialog: AlertDialog = builder.create()
//
//        // Display the alert dialog on app interface
//        dialog.show()
    }



}
