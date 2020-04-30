package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.databinding.ActivityAddDocumentBinding
import cos.tuk_tuk_driver.models.Documents
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
                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "8") {
                                            binding.identyCheck.visibility = View.VISIBLE

                                        }

                                        if (response.body()!!.driverDocuments.get(x).document_id == "9") {
                                            binding.photoCheck.visibility = View.VISIBLE

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
                    "Please wait your documents is not approved yet"
                )
            }


        }


    }

}
