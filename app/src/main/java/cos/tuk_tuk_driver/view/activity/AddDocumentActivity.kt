package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityAddDocumentBinding
import cos.tuk_tuk_driver.model.Documents
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Constants
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddDocumentActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddDocumentBinding
    private val apiInterface = Comman.getApiToken()
    private var firstName: String = ""
    private var lastName: String = ""
    private var Message: String = ""
    private var IsApproved: Int = 0
    private var IsReject: Int = 0
    private var clickStatus:Boolean=false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDocumentBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            firstName = Prefs.getKey(applicationContext, "driveName")
            //  lastName = Prefs.getKey(applicationContext, "DriverLastName")
            val intent = intent
            val name = intent?.getStringExtra(Constants.NAME)
            binding.name.text = "Hi, " + firstName
            init()
        } catch (Ex: Exception) {

        }

    }


    override fun onBackPressed() {
        super.onBackPressed()
//        finish()
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
                            Message = ""
                            IsApproved = 0
                            IsReject = 0
                            dialog.dismiss()

                            if (response.code() == 200) {

                                if (response.body()!!.driverDocuments.size != 0) {

                                    for (x in 0 until response.body()!!.driverDocuments.size) {

                                        if (response.body()!!.driverDocuments.get(x).document_id == "1") {

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ASSESSING",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.driverCheck.visibility = View.VISIBLE
                                                if (response.body()!!.driverDocuments.get(x).status.equals(
                                                        "ASSESSING",
                                                        ignoreCase = true
                                                    )
                                                ) {
                                                    Message =
                                                        "Your Driving License document is ${response.body()!!.driverDocuments.get(
                                                            x
                                                        ).status}"
                                                }
                                                if (response.body()!!.driverDocuments.get(x).status.equals(
                                                        "ACTIVE",
                                                        ignoreCase = true
                                                    ) ||
                                                    response.body()!!.driverDocuments.get(x).status.equals(
                                                        "NEARTOEXPIRE",
                                                        ignoreCase = true
                                                    ) ||
                                                    response.body()!!.driverDocuments.get(x).status.equals(
                                                        "EXPIRETODAY",
                                                        ignoreCase = true
                                                    )
                                                ) {
                                                    IsApproved += 1
                                                }

                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverLicenceExpiry",
                                                    response.body()!!.driverDocuments.get(x).expiry
                                                )
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverLicenceFrontImage",
                                                    response.body()!!.driverDocuments.get(x).url
                                                )
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverLicenceBackImage",
                                                    response.body()!!.driverDocuments.get(x).additonal_doc
                                                )


                                                if (response.body()!!.driverDocuments.get(x).status.equals(
                                                        "ASSESSING",
                                                        ignoreCase = true
                                                    )
                                                ) {

                                                    binding.driverCheck.setImageResource(R.drawable.timer)
//                                                    binding.driverCheck.setBackgroundResource(R.drawable.edt_round_oranges)
                                                }

                                                if (response.body()!!.driverDocuments.get(x).status.equals(
                                                        "ACTIVE",
                                                        ignoreCase = true
                                                    )
                                                ) {
                                                    binding.driverCheck.setImageResource(R.drawable.done)
                                                }


                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "REJECT",
                                                    ignoreCase = true
                                                ) || response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRED",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                Message =
                                                    Message + "\n\nYour Driving License document is ${response.body()!!.driverDocuments.get(
                                                        x
                                                    ).status}"
                                                IsReject += 1
                                                binding.driverCheck.visibility = View.VISIBLE

                                                binding.driverCheck.setImageResource(R.drawable.cancel_red)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "processing", ignoreCase = true
                                                )
                                            ) {
                                                Message =
                                                    Message + "\n\nYour Driving License document is in ${response.body()!!.driverDocuments.get(
                                                        x
                                                    ).status}"
                                            }

                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "8") {

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ASSESSING",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.identyCheck.visibility = View.VISIBLE
                                                if (response.body()!!.driverDocuments.get(x).status.equals(
                                                        "ASSESSING",
                                                        ignoreCase = true
                                                    )
                                                ) {
//                                                    Message =
//                                                        Message + "\n\nYour Passport document is ${response.body()!!.driverDocuments.get(
//                                                            x
//                                                        ).status}"

                                                    Message =
                                                        Message + "\n\nPassport and Driving License both ${response.body()!!.driverDocuments.get(
                                                            x
                                                        ).status}"

                                                }

                                                if (response.body()!!.driverDocuments.get(x).status.equals(
                                                        "ACTIVE",
                                                        ignoreCase = true
                                                    ) ||
                                                    response.body()!!.driverDocuments.get(x).status.equals(
                                                        "NEARTOEXPIRE",
                                                        ignoreCase = true
                                                    ) ||
                                                    response.body()!!.driverDocuments.get(x).status.equals(
                                                        "EXPIRETODAY",
                                                        ignoreCase = true
                                                    )
                                                ) {
                                                    IsApproved += 1
                                                }
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverPassportExpiry",
                                                    response.body()!!.driverDocuments.get(x).expiry
                                                )
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverPassportFrontImage",
                                                    response.body()!!.driverDocuments.get(x).url
                                                )
                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverPassportBackImage",
                                                    response.body()!!.driverDocuments.get(x).additonal_doc
                                                )



                                                if (response.body()!!.driverDocuments.get(x).status.equals(
                                                        "ASSESSING",
                                                        ignoreCase = true
                                                    )
                                                ) {

                                                    binding.identyCheck.setImageResource(R.drawable.timer)
//                                                    binding.identyCheck.setBackgroundResource(R.drawable.edt_round_oranges)
                                                }

                                                if (response.body()!!.driverDocuments.get(x).status.equals(
                                                        "ACTIVE",
                                                        ignoreCase = true
                                                    )
                                                ) {
                                                    binding.identyCheck.setImageResource(R.drawable.done)
                                                }


                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "REJECT",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRED",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                Message =
                                                    Message + "\n\nYour Passport document is ${response.body()!!.driverDocuments.get(
                                                        x
                                                    ).status}"

                                                IsReject += 1

                                                binding.identyCheck.visibility = View.VISIBLE
                                                binding.identyCheck.setImageResource(R.drawable.cancel_red)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "processing", ignoreCase = true
                                                )
                                            ) {

                                                Message =
                                                    Message + "\n\nYour Passport document is in ${response.body()!!.driverDocuments.get(
                                                        x
                                                    ).status}"
                                            }

                                            /*

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
                                                binding.identyCheck.setImageResource(R.drawable.ic_check_red)
                                            }*/

                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "9") {

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ASSESSING",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.photoCheck.visibility = View.VISIBLE

                                                Prefs.putKey(
                                                    applicationContext,
                                                    "driverAddImage",
                                                    response.body()!!.driverDocuments.get(x).url
                                                )

                                               // binding.photoCheck.setImageResource(R.drawable.ic_check_green)
                                            }




                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ASSESSING",
                                                    ignoreCase = true
                                                )
                                            ) {

                                                binding.photoCheck.setImageResource(R.drawable.timer)
//                                                binding.photoCheck.setBackgroundResource(R.drawable.edt_round_oranges)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.photoCheck.setImageResource(R.drawable.done)
                                            }



                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "REJECT",
                                                    ignoreCase = true
                                                ) || response.body()!!.driverDocuments.get(x).status.equals(
                                                    "EXPIRED",
                                                    ignoreCase = true
                                                )
                                            ) {
//                                                Message =
//                                                    Message + "\n\nYour Driving License document is ${response.body()!!.driverDocuments.get(
//                                                        x
//                                                    ).status}"
                                                IsReject += 1
                                                binding.photoCheck.visibility = View.VISIBLE

                                                binding.photoCheck.setImageResource(R.drawable.cancel_red)
                                            }


                                        }

                                    }

                                }


                                if (binding.photoCheck.visibility == View.VISIBLE &&
                                    binding.identyCheck.visibility == View.VISIBLE &&
                                    binding.driverCheck.visibility == View.VISIBLE

                                ) {
                                    binding.btnNext.visibility = View.VISIBLE

                                }


                                if (IsApproved == 2) {
                                    val intent = Intent(this@AddDocumentActivity, HomeActivity::class.java)
                                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                    startActivity(intent)

                                } else {

                                    if (IsReject > 0) {
                                        Comman.makeToast(applicationContext, "Your Document is Expire or REJECT.")

                                    } else {

//                    Comman.makeToast(applicationContext, "Your account need some attentions.")
                                        if(clickStatus)
                                        {
                                            clickStatus=false
                                            if (!Message.isEmpty()) {
                                                MessageAlert(Message)
                                                /*Comman.makeToast(
                                                    applicationContext,
                                                    Message
                                                )*/
                                            } else {
                                                Comman.makeToast(
                                                    applicationContext,
                                                    "Your account is not approved yet"
                                                )
                                            }
                                        }



                                    }

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
            intent.putExtra("from", "beforeLogin")
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
            intent.putExtra("from", "beforeLogin")
            startActivity(intent)

        }

        binding.cardPhoto.setOnClickListener {

            val intent = Intent(this@AddDocumentActivity, AddPhotoActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

        }

        binding.btnNext.setOnClickListener {

            /* IsApproved=0
             IsReject=0
             */
            clickStatus=true
            GetUploadDocuments()


        }

        binding.signOut.setOnClickListener {

            LogoutAlert()
        }


    }

    private fun MessageAlert(message: String) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)

        // Set the alert dialog title
        builder.setTitle("Alert")

        // Display a message on alert dialog
        builder.setMessage(message)

        // Display a negative button on alert dialog
        builder.setNegativeButton("OK") { dialog, which ->

        }

        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }


    private fun LogoutAlert() {

        Prefs.clearSharedPreferences(DriverApp.context)
        val intent = Intent(
            this,
            GetOtpActivity::class.java
        ) //not application context
        intent.putExtra("from", "login")

        intent.flags =
            Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

    }


}
