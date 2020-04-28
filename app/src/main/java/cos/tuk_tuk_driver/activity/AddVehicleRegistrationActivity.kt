package cos.tuk_tuk_driver.activity

import android.Manifest
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.tuktuk.utils.Comman
import cos.tuk_tuk_driver.databinding.ActivityAddVehicleRegistrationBinding
import cos.tuk_tuk_driver.models.UploadDocsModal
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class AddVehicleRegistrationActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddVehicleRegistrationBinding
    private val apiInterface = Comman.getApiToken()
    private var isImage: Int = 0
    private var SelectedImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddVehicleRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            init()

        } catch (Ex: Exception) {

        }

    }

    private fun init() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.uploadDoc.setOnClickListener {

            if (SelectedImage != "" && isImage != 0) {
                uploadDocRC()
            } else {
                Dexter.withActivity(this)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
                                val insurance = Intent()
                                insurance.type = "image/*"
                                insurance.action = Intent.ACTION_GET_CONTENT
                                startActivityForResult(
                                    Intent.createChooser(
                                        insurance,
                                        "Select Picture"
                                    ), 12
                                )
                            }
                            // check for permanent denial of any permission
                            if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings
                                Toast.makeText(
                                    applicationContext,
                                    " permissions are not granted!",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }

                        override fun onPermissionRationaleShouldBeShown(
                            permissions: List<PermissionRequest>,
                            token: PermissionToken
                        ) {
                            token.continuePermissionRequest()
                        }
                    }).withErrorListener {
                        Toast.makeText(
                            applicationContext,
                            "Error occurred! ",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                    .onSameThread()
                    .check()
            }

        }
    }


    private fun uploadDocRC() {
        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            val documentId: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "5")
            val expiry: RequestBody =
                RequestBody.create(MediaType.parse("text/plain"), "")

            if (!SelectedImage.equals(null, ignoreCase = true) && !SelectedImage.equals(
                    "",
                    ignoreCase = true
                )
            ) {


                dialog.show()
                val uri1 = Uri.parse(SelectedImage)
                val Li_file = File(uri1.path)
                val Li_file_reqFile = RequestBody.create(MediaType.parse("image/*"), Li_file)
                val document =
                    MultipartBody.Part.createFormData("document", Li_file.name, Li_file_reqFile)

                apiInterface!!.uploadDocs(documentId, document, expiry)
                    .enqueue(object : retrofit2.Callback<UploadDocsModal> {
                        override fun onFailure(call: Call<UploadDocsModal>, t: Throwable) {

                        }

                        override fun onResponse(
                            call: Call<UploadDocsModal>,
                            response: Response<UploadDocsModal>
                        ) {
                            dialog.dismiss()

                            if (response.body()!!.status) {
                                isImage = 0
                                SelectedImage = ""
                                Comman.makeToast(applicationContext, response.body()!!.message)

                            } else if (!response.body()!!.status) {
                                Comman.makeToast(applicationContext, response.body()!!.message)

                            }

                        }

                    })

            }
        } catch (Ex: Exception) {

        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 12 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            isImage = 1
            SelectedImage = Comman.getImages(data, binding.imgLicense, applicationContext)
        }
    }
}
