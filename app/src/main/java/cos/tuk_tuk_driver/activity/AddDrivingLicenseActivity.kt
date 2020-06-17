package cos.tuk_tuk_driver.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.os.StrictMode
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityAddDrivingLicenseBinding
import cos.tuk_tuk_driver.models.UploadDocsModal
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Prefs
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class AddDrivingLicenseActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddDrivingLicenseBinding
    private val apiInterface = Comman.getApiToken()
    private var isImage: Int = 0
    private var driverLicenceImage: String = ""
    private var afterLogin: String = ""

    private var mCurrentPhotoPath: String = ""
    private var SelectedFrontImage: String = ""
    private var SelectedBackImage: String = ""
    private var driverLicenceFrontImage: String = ""
    private var driverLicenceBackImage: String = ""
    private var driverLicenceExpiry: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddDrivingLicenseBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            driverLicenceFrontImage = Prefs.getKey(applicationContext, "driverLicenceFrontImage")
            driverLicenceBackImage = Prefs.getKey(applicationContext, "driverLicenceBackImage")
            driverLicenceExpiry = Prefs.getKey(applicationContext, "driverLicenceExpiry")
            afterLogin = intent.getStringExtra("from")
            if (afterLogin.equals("beforeLogin")) {
                binding.imagetitle.visibility = View.GONE
            }

            init()


        } catch (Ex: Exception) {

        }

    }


    private fun init() {

        binding.ivBack.setOnClickListener {
            finish()
        }

        binding.expiryDate.setOnClickListener {

            val c = Calendar.getInstance()
            val year = c.get(Calendar.YEAR)
            val month = c.get(Calendar.MONTH)
            val day = c.get(Calendar.DAY_OF_MONTH)

            val dpd = DatePickerDialog(
                this,
                R.style.AlertDialogCustom,
                DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->

                    // Display Selected date in textbox
                    binding.expiryDate.setText("" + year + "-" + (monthOfYear + 1) + "-" + dayOfMonth)

                }, year, month, day
            )

            dpd.show()
        }

        binding.drivingBack.setOnClickListener(this)
        binding.drivingFront.setOnClickListener(this)
        binding.uploadDoc.setOnClickListener(this)

        if (!driverLicenceFrontImage.isEmpty() || !driverLicenceBackImage.isEmpty()) {

            /* Glide.with(applicationContext).load(URLHelper.BaseUrlImage + driverLicenceFrontImage)
                 .into(binding.drivingFront)
             Glide.with(applicationContext).load(URLHelper.BaseUrlImage + driverLicenceBackImage)
                 .into(binding.drivingBack)*/

            Comman.setImageUriWithDefault(
                applicationContext,
                driverLicenceFrontImage,
                binding.drivingFront,
                R.drawable.pass_front
            )
            Comman.setImageUriWithDefault(
                applicationContext,
                driverLicenceBackImage,
                binding.drivingBack,
                R.drawable.pass_back
            )
            binding.expiryDate.setText(driverLicenceExpiry)
            binding.drivingFront.scaleType = null
            binding.drivingBack.scaleType = null

            binding.imagetitle.visibility = View.GONE
            binding.uploadDoc.visibility = View.GONE
            binding.drivingBack.setOnClickListener(null)
            binding.drivingFront.setOnClickListener(null)
        }

        if (afterLogin.equals("afterLogin", ignoreCase = true)) {
            binding.uploadDoc.visibility = View.VISIBLE
            binding.drivingBack.setOnClickListener(this)
            binding.drivingFront.setOnClickListener(this)
            binding.uploadDoc.setOnClickListener(this)
        }

        binding.expiryDate.setText(driverLicenceExpiry)
    }


    private fun validate() {

        if (SelectedFrontImage == "") {

            Comman.makeToast(applicationContext, "Please Select License Front Image")

        } else if (SelectedBackImage == "") {

            Comman.makeToast(applicationContext, "Please Select License Back Image")

        } else if (binding.expiryDate.text.isEmpty()) {
            Comman.makeToast(applicationContext, "Please enter expiry date")

        } else {
            uploadDocIdentity()
        }

    }

    fun showAlert(actionNo: Int) {

        val builder = AlertDialog.Builder(this@AddDrivingLicenseActivity, R.style.AlertDialogCustom)
//        builder.setTitle("Carbon")
        builder.setMessage("Choose Image from ?")
        builder.setPositiveButton("Gallery") { dialog, which ->
            dialog.dismiss()

            val insurance = Intent()
            insurance.type = "image/*"
            insurance.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(
                    insurance,
                    "Select Picture"
                ), actionNo
            )
        }
        builder.setNegativeButton("Camera") { dialog, which ->
            dialog.dismiss()
            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            if (cameraIntent.resolveActivity(packageManager) != null) {
                var photoFile: File? = null
                try {
                    photoFile = createImageFile()
                } catch (ex: IOException) {
                    Log.i("Main", "IOException")
                }
                if (photoFile != null) {
                    val builder = StrictMode.VmPolicy.Builder()
                    StrictMode.setVmPolicy(builder.build())
                    cameraIntent.putExtra("crop", true)
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
                    if (actionNo == 12) {
                        startActivityForResult(cameraIntent, 121)
                    }

                    if (actionNo == 13) {
                        startActivityForResult(cameraIntent, 131)
                    }
                }
            }
        }
        builder.setNeutralButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val dialog: AlertDialog = builder.create()
        dialog.show()

    }

    @SuppressLint("SimpleDateFormat")
    @Throws(IOException::class)
    private fun createImageFile(): File {
        // Create an image file name
        val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        val imageFileName = "JPEG_" + timeStamp + "_"
        val storageDir = Environment.getExternalStoragePublicDirectory(
            Environment.DIRECTORY_PICTURES
        )
        val image = File.createTempFile(
            imageFileName, // prefix
            ".jpg", // suffix
            storageDir      // directory
        )

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.absolutePath
        return image
    }


    private fun uploadDocIdentity() {
        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            val documentId: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "1")
            val expiry: RequestBody = RequestBody.create(
                MediaType.parse("text/plain"),
                binding.expiryDate.text.toString()
            )


            if (!SelectedFrontImage.equals(null, ignoreCase = true) && !SelectedFrontImage.equals(
                    "",
                    ignoreCase = true
                )
            ) {


                dialog.show()
                val uriFront = Uri.parse(SelectedFrontImage)
                val fileFront = File(uriFront.path)
                val reqFileFront = RequestBody.create(MediaType.parse("image/*"), fileFront)
                val document =
                    MultipartBody.Part.createFormData("document", fileFront.name, reqFileFront)

                val uriBack = Uri.parse(SelectedBackImage)
                val fileBack = File(uriBack.path)
                val reqFileBack = RequestBody.create(MediaType.parse("image/*"), fileBack)
                val additonal_doc =
                    MultipartBody.Part.createFormData(
                        "additonal_doc[]",
                        fileBack.name,
                        reqFileBack
                    )

                apiInterface!!.uploadDocsPassport(documentId, document, additonal_doc, expiry)
                    .enqueue(object : retrofit2.Callback<UploadDocsModal> {
                        override fun onFailure(call: Call<UploadDocsModal>, t: Throwable) {
                            dialog.dismiss()

                            Comman.makeToast(applicationContext, "Please try again later")

                        }

                        override fun onResponse(
                            call: Call<UploadDocsModal>,
                            response: Response<UploadDocsModal>
                        ) {

                            dialog.dismiss()

                            if (response.body()!!.status) {

                                SelectedFrontImage = ""
                                SelectedBackImage = ""
                                /* binding.imagetitle.visibility = View.VISIBLE
                                 binding.drivingFront.setImageResource(R.drawable.pass_front)
                                 binding.drivingBack.setImageResource(R.drawable.pass_front)*/

                                Comman.makeToast(applicationContext, response.body()!!.message)
                                finish()

                            } else if (!response.body()!!.status) {
                                Comman.makeToast(
                                    applicationContext,
                                    response.body()!!.error.get(0).error
                                )

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
            binding.imagetitle.visibility = View.GONE
            SelectedFrontImage = Comman.getImages(data, binding.drivingFront, applicationContext)
        }
        if (requestCode == 13 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            binding.imagetitle.visibility = View.GONE
            SelectedBackImage = Comman.getImages(data, binding.drivingBack, applicationContext)
        }
        if (requestCode == 121 && resultCode == Activity.RESULT_OK) {
            binding.imagetitle.visibility = View.GONE
            Glide.with(this).load(mCurrentPhotoPath).into(binding.drivingFront)
            SelectedFrontImage = mCurrentPhotoPath

        }
        if (requestCode == 131 && resultCode == Activity.RESULT_OK) {

            binding.imagetitle.visibility = View.GONE
            Glide.with(this).load(mCurrentPhotoPath).into(binding.drivingBack)
            SelectedBackImage = mCurrentPhotoPath

        }
    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.uploadDoc -> {

                validate()

            }
            R.id.drivingFront -> {

                Dexter.withActivity(this)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {

                                showAlert(12)
//                                val insurance = Intent()
//                                insurance.type = "image/*"
//                                insurance.action = Intent.ACTION_GET_CONTENT
//                                startActivityForResult(
//                                    Intent.createChooser(
//                                        insurance,
//                                        "Select Picture"
//                                    ), 12
//                                )
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

            R.id.drivingBack -> {

                Dexter.withActivity(this)
                    .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE
                    )
                    .withListener(object : MultiplePermissionsListener {
                        override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
                            if (report.areAllPermissionsGranted()) {
//                                val insurance = Intent()
//                                insurance.type = "image/*"
//                                insurance.action = Intent.ACTION_GET_CONTENT
//                                startActivityForResult(
//                                    Intent.createChooser(
//                                        insurance,
//                                        "Select Picture"
//                                    ), 13
//                                )

                                showAlert(13)
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
}