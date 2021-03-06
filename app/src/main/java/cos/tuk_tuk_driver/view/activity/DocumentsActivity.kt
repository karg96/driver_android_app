package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityDocumentsBinding
import cos.tuk_tuk_driver.model.Documents
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DocumentsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDocumentsBinding
    private val apiInterface = Comman.getApiToken()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDocumentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()


        } catch (Ex: Exception) {

        }

    }




    private fun init() {

        binding.close.setOnClickListener {
            finish()
        }

        binding.driver.setOnClickListener {

            val intent = Intent(this@DocumentsActivity, AddDrivingLicenseActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            // intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("from", "afterLogin")
            startActivity(intent)
        }


        binding.passport.setOnClickListener {

            val intent = Intent(this@DocumentsActivity, AddIdentityCardActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            //intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
            intent.putExtra("from", "afterLogin")
            startActivity(intent)
        }

         binding.addPhoto.setOnClickListener {

             val intent = Intent(this@DocumentsActivity, AddPhotoActivity::class.java)
             intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
//             intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK
             intent.putExtra("from", "afterLogin")
             startActivity(intent)

         }

    }

    override fun onResume() {
        super.onResume()
        binding.driver.visibility = View.GONE
        binding.passport.visibility = View.GONE

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
                                            //  Prefs.putKey(applicationContext, "driverLicenceImage",response.body()!!.driverDocuments.get(x).url)
//                                            if (response.body()!!.driverDocuments.get(x).status.equals(
//                                                    "ACTIVE",
//                                                    ignoreCase = true
//                                                )||response.body()!!.driverDocuments.get(x).status.equals(
//                                                    "ASSESSING",
//                                                    ignoreCase = true
//                                                )
//                                            ) {
//
//                                                binding.driver.visibility = View.VISIBLE
//                                                binding.vehicleText.text = "Document approved"
//                       2                         binding.driverImage.setImageResource(R.drawable.ic_check_white)
//                                                binding.driverImage.setBackgroundResource(R.drawable.edt_round_green)
//                                            }


                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE"))
                                                {
                                                    binding.driver.visibility = View.VISIBLE
                                                    binding.vehicleText.text = "Document approved"
                                                    binding.driverImage.setImageResource(R.drawable.ic_check_white)
                                                    binding.driverImage.setBackgroundResource(R.drawable.edt_round_green)
                                                }

                                            if(response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ASSESSING"))
                                            {
                                                binding.driver.visibility = View.VISIBLE
                                                binding.vehicleText.text = "Pending approval"
                                                binding.driverImage.setImageResource(R.drawable.timer)
                                                binding.driverImage.setBackgroundResource(R.drawable.edt_round_oranges)
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
                                                binding.driver.visibility = View.VISIBLE

                                                binding.vehicleText.text =
                                                    "Document will expire today"

                                                binding.driverImage.setImageResource(R.drawable.ic_remove_white)
                                                binding.driverImage.setBackgroundResource(R.drawable.edt_round_red)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "Processing",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.driver.visibility = View.VISIBLE

                                                binding.vehicleText.text =
                                                    "Document will expire soon"
                                                binding.driverImage.setImageResource(R.drawable.timer)
                                                binding.driverImage.setBackgroundResource(R.drawable.edt_round_oranges)

                                            }
                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "8") {
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


//                                            if (response.body()!!.driverDocuments.get(x).status.equals(
//                                                    "ACTIVE",
//                                                    ignoreCase = true
//                                                )||response.body()!!.driverDocuments.get(x).status.equals(
//                                                    "ASSESSING",
//                                                    ignoreCase = true
//                                                )
//                                            ) {
//                                                binding.passport.visibility = View.VISIBLE
//
//                                                binding.PassportText.text = "Document approved"
//
//                                                binding.PassportImage.setImageResource(R.drawable.ic_check_white)
//                                                binding.PassportImage.setBackgroundResource(R.drawable.edt_round_green)
//                                            }





                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE"))
                                            {
                                                binding.passport.visibility = View.VISIBLE
                                                binding.PassportText.text = "Document approved"
                                                binding.PassportImage.setImageResource(R.drawable.ic_check_white)
                                                binding.PassportImage.setBackgroundResource(R.drawable.edt_round_green)
                                            }

                                            if(response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ASSESSING"))
                                            {
                                                binding.passport.visibility = View.VISIBLE
                                                binding.PassportText.text = "Pending approval"
                                                binding.PassportImage.setImageResource(R.drawable.timer)
                                                binding.PassportImage.setBackgroundResource(R.drawable.edt_round_oranges)
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
                                                binding.passport.visibility = View.VISIBLE

                                                binding.PassportText.text =
                                                    "Document will expire today"

                                                binding.PassportImage.setImageResource(R.drawable.ic_remove_white)
                                                binding.PassportImage.setBackgroundResource(R.drawable.edt_round_red)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "Processing",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.passport.visibility = View.VISIBLE

                                                binding.PassportText.text =
                                                    "Document will expire soon"

                                                binding.PassportImage.setImageResource(R.drawable.timer)
                                                binding.PassportImage.setBackgroundResource(R.drawable.edt_round_oranges)
                                            }

                                        }


                                        if (response.body()!!.driverDocuments.get(x).document_id == "9") {

                                            Prefs.putKey(
                                                applicationContext,
                                                "driverAddImage",
                                                response.body()!!.driverDocuments.get(x).url
                                            )




                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ACTIVE"))
                                            {
                                                binding.addPhoto.visibility = View.VISIBLE
                                                binding.statusPhoto.text = "Document approved"
                                                binding.PhotoImage.setImageResource(R.drawable.ic_check_white)
                                                binding.PhotoImage.setBackgroundResource(R.drawable.edt_round_green)
                                            }

                                            if(response.body()!!.driverDocuments.get(x).status.equals(
                                                    "ASSESSING"))
                                            {
                                                binding.addPhoto.visibility = View.VISIBLE
                                                binding.statusPhoto.text = "Pending approval"
                                                binding.PhotoImage.setImageResource(R.drawable.timer)
                                                binding.PhotoImage.setBackgroundResource(R.drawable.edt_round_oranges)
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
                                                binding.addPhoto.visibility = View.VISIBLE

                                                binding.statusPhoto.text =
                                                    "Document will expire today"

                                                binding.PhotoImage.setImageResource(R.drawable.ic_remove_white)
                                                binding.PhotoImage.setBackgroundResource(R.drawable.edt_round_red)
                                            }

                                            if (response.body()!!.driverDocuments.get(x).status.equals(
                                                    "NEARTOEXPIRE",
                                                    ignoreCase = true
                                                ) ||
                                                response.body()!!.driverDocuments.get(x).status.equals(
                                                    "Processing",
                                                    ignoreCase = true
                                                )
                                            ) {
                                                binding.addPhoto.visibility = View.VISIBLE

                                                binding.statusPhoto.text =
                                                    "Document will expire soon"
                                                binding.PhotoImage.setImageResource(R.drawable.timer)
                                                binding.PhotoImage.setBackgroundResource(R.drawable.edt_round_oranges)

                                            }
                                        }


                                    }


                                }

                            }


                        } catch (Ex: Exception) {

                        }
                    }

                })

        } catch (Ex: java.lang.Exception) {

        }

    }

}
