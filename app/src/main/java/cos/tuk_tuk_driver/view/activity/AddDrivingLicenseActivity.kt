package cos.tuk_tuk_driver.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.DatePickerDialog
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
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
import cos.tuk_tuk_driver.databinding.ActivityAddDrivingLicenseBinding
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

class AddDrivingLicenseActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var binding: ActivityAddDrivingLicenseBinding
    private val apiInterface = Comman.getApiToken()
    private var imageTypeSelected: Int = 0
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
           /* if (afterLogin.equals("beforeLogin")) {
                binding.imagetitle.visibility = View.GONE
            }*/
            if (!driverLicenceFrontImage.isEmpty() && !driverLicenceBackImage.isEmpty()) {
                binding.rlFirst.visibility = View.GONE
                binding.rlSecond.visibility = View.GONE
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
            dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
            dpd.show()
        }

        binding.drivingBack.setOnClickListener(this)
        binding.drivingFront.setOnClickListener(this)
        binding.uploadDoc.setOnClickListener(this)

        if (!driverLicenceFrontImage.isEmpty() && !driverLicenceBackImage.isEmpty()) {

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

            binding.rlFirst.visibility = View.GONE
            binding.rlSecond.visibility = View.GONE
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

            if (actionNo == 12) {
                imageTypeSelected = 12
                CropImage.startPickImageActivity(this)
            }

            if (actionNo == 13) {
                imageTypeSelected = 13
                CropImage.startPickImageActivity(this)
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

        //Save a file: path for use with ACTION_VIEW intents
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
            imageTypeSelected = 12

            SelectedFrontImage = Comman.getImages(data, binding.drivingFront, applicationContext)
            startCropImageActivity(data.data!!)

        }
        if (requestCode == 13 && resultCode == Activity.RESULT_OK && data != null && data.data != null) {
            imageTypeSelected = 13



            SelectedBackImage = Comman.getImages(data, binding.drivingBack, applicationContext)
            startCropImageActivity(data.data!!)

        }

        if (requestCode == 121 && resultCode == Activity.RESULT_OK) {

            Glide.with(this).load(mCurrentPhotoPath).into(binding.drivingFront)
            SelectedFrontImage = mCurrentPhotoPath

        }

        if (requestCode == 131 && resultCode == Activity.RESULT_OK) {

            Glide.with(this).load(mCurrentPhotoPath).into(binding.drivingBack)
            SelectedBackImage = mCurrentPhotoPath

        }

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            var imageUri = CropImage.getPickImageResultUri(this, data)

            startCropImageActivity(imageUri)
        }

        // handle result of CropImageActivity
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            var result = CropImage.getActivityResult(data)
            if (resultCode == RESULT_OK) {

                if (imageTypeSelected == 12) {
                    Glide.with(this).load(result.uri).into(binding.drivingFront)
                    SelectedFrontImage = result.uri.toString()
                    if(SelectedBackImage.isEmpty())
                    {
                        binding.rlFirst.visibility = View.GONE
                        binding.rlSecond.visibility = View.VISIBLE
                    }else{
                        binding.rlFirst.visibility = View.GONE
                        binding.rlSecond.visibility = View.GONE
                    }

                }

                if (imageTypeSelected == 13) {
                    Glide.with(this).load(result.uri).into(binding.drivingBack)
                    SelectedBackImage = result.uri.toString()

                    if(SelectedFrontImage.isEmpty())
                    {
                        binding.rlFirst.visibility = View.VISIBLE
                        binding.rlSecond.visibility = View.GONE
                    }else{
                        binding.rlFirst.visibility = View.GONE
                        binding.rlSecond.visibility = View.GONE
                    }
                }


            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Toast.makeText(this, "Cropping failed: " + result.error, Toast.LENGTH_LONG)
                    .show()
            }
        }

    }

    override fun onClick(p0: View?) {
        when (p0!!.id) {
            R.id.uploadDoc -> {

                validate()

            }
            R.id.drivingFront -> {
                if (request_permission()) {
                    showAlert(12)
                }
            }

            R.id.drivingBack -> {
                if (request_permission()) {
                    showAlert(13)
                }
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