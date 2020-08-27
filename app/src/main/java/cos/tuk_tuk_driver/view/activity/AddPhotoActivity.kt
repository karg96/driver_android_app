package cos.tuk_tuk_driver.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.theartofdev.edmodo.cropper.CropImage
import com.theartofdev.edmodo.cropper.CropImageView
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityAddPhotoBinding
import cos.tuk_tuk_driver.model.UploadDocsModal
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

class AddPhotoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddPhotoBinding
    private val apiInterface = Comman.getApiToken()
    private var isImage: Int = 0
    private lateinit var mCropImageUri: Uri
    private var SelectedImage: String = ""
    private var mCurrentPhotoPath: String = ""
    private var driverAddImage: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddPhotoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        try {

            driverAddImage = Prefs.getKey(applicationContext, "driverAddImage")

            init()

            if (!driverAddImage.isEmpty()) {
                /*Glide.with(applicationContext).load(URLHelper.BaseUrlImage + driverAddImage)
                    .into(binding.imgLicense)*/
//                binding.uploadDoc.visibility -= View.GONE
                Comman.setImageUriWithDefault(
                    applicationContext,
                    driverAddImage,
                    binding.imgLicense,
                    R.drawable.account
                )

            }
        } catch (Ex: Exception) {

        }

    }

    private fun init() {

        binding.ivBack.setOnClickListener {
            finish()
        }
        binding.imgLicense.setOnClickListener {
            showAlert()
        }

        binding.uploadDoc.setOnClickListener {

            if (SelectedImage != "" && isImage != 0) {
                uploadDocPhoto()
            } else {
                if(request_permission())
                {
                    showAlert()
                }

//                Dexter.withActivity(this)
//                    .withPermissions(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    )
//                    .withListener(object : MultiplePermissionsListener {
//                        override fun onPermissionsChecked(report: MultiplePermissionsReport) { // check if all permissions are granted
//                            if (report.areAllPermissionsGranted()) {
//                                showAlert()
//
//                            }
//                            // check for permanent denial of any permission
//                            if (report.isAnyPermissionPermanentlyDenied) { // show alert dialog navigating to Settings
//                                Toast.makeText(
//                                    applicationContext,
//                                    " permissions are not granted!",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//
//                        override fun onPermissionRationaleShouldBeShown(
//                            permissions: List<PermissionRequest>,
//                            token: PermissionToken
//                        ) {
//                            token.continuePermissionRequest()
//                        }
//                    }).withErrorListener {
//                        Toast.makeText(
//                            applicationContext,
//                            "Error occurred! ",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                    .onSameThread()
//                    .check()
            }

        }
    }


    fun showAlert() {

        val builder = AlertDialog.Builder(this@AddPhotoActivity, R.style.AlertDialogCustom)
//        builder.setTitle("Carbon")
        builder.setMessage("Choose Image from ?")
        builder.setPositiveButton("Gallery") { dialog, which ->
            dialog.dismiss()

            val insurance = Intent()
            insurance.type = "image/*"
            insurance.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(insurance, "Select Picture"), 12)
        }
        builder.setNegativeButton("Camera") { dialog, which ->
            dialog.dismiss()

            CropImage.startPickImageActivity(this)

//            val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
//            if (cameraIntent.resolveActivity(packageManager) != null) {
//                var photoFile: File? = null
//                try {
//                    photoFile = createImageFile()
//                } catch (ex: IOException) {
//                    Log.i("Main", "IOException")
//                }
//                if (photoFile != null) {
//                    val builder = StrictMode.VmPolicy.Builder()
//                    StrictMode.setVmPolicy(builder.build())
//                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile))
//                    startActivityForResult(cameraIntent, 121)
//
//
//                }
//            }
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

    private fun uploadDocPhoto() {
        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            val documentId: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "9")
            val expiry: RequestBody = RequestBody.create(MediaType.parse("text/plain"), "")



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
                                finish()

                            } else if (!response.body()!!.status) {
                                Comman.makeToast(applicationContext, response.body()!!.message)

                            }

                        }

                    })

            }
        } catch (Ex: Exception) {

        }

    }


    @RequiresApi(Build.VERSION_CODES.M)
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 12 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            isImage = 1
            SelectedImage = Comman.getImages(data, binding.imgLicense, applicationContext)
            startCropImageActivity(data.data!!)

        }

        if (requestCode == 121 && resultCode == Activity.RESULT_OK) {
            isImage = 1

            Glide.with(this).load(mCurrentPhotoPath).into(binding.imgLicense)
            SelectedImage = mCurrentPhotoPath
            // startCropImageActivity(data!!.data!!)

        }

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            var imageUri = CropImage.getPickImageResultUri(this, data)

            startCropImageActivity(imageUri)
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {
                isImage = 1

                Glide.with(this).load(result.uri).into(binding.imgLicense)
                SelectedImage = result.uri.toString()

            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG)
                    .show()
            }
        }
    }


    /**
     * Start crop image activity for the given image.
     */
    fun startCropImageActivity(imageUri: Uri) {
        CropImage.activity(imageUri)
            .setGuidelines(CropImageView.Guidelines.ON)
            .setMultiTouchEnabled(true)
            .start(this)
    }



    private fun request_permission(): Boolean {
        return if (ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                applicationContext,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(
                    arrayOf(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                    ), 5
                )
            }
            false
        } else {
            true
        }
    }


}
