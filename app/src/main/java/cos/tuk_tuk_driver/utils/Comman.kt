package cos.tuk_tuk_driver.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.swiperefreshlayout.widget.CircularProgressDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.services.ApiClients.client
import cos.tuk_tuk_driver.services.ApiClients.client_token
import cos.tuk_tuk_driver.services.ApiInterface
import cos.tuk_tuk_driver.utils.URLHelper.BaseUrl
import cos.tuk_tuk_driver.utils.URLHelper.BaseUrlImage

object Comman {


    fun getApi(): ApiInterface? {
        return client!!.create(ApiInterface::class.java)
    }

    fun getApiToken(): ApiInterface? {
        return client_token!!.create(ApiInterface::class.java)
    }


    fun makeToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    fun getImages(
        data: Intent,
        first: ImageView?,
        context: Context
    ): String {
        val uri = data.data
        var profile_img = ""
        val wholeID = DocumentsContract.getDocumentId(uri)
        // Split at colon, use second item in the array
        val id = wholeID.split(":").toTypedArray()[1]
        val column = arrayOf(
            MediaStore.Images.Media.DATA
        )
        // where id is equal to
        val sel = MediaStore.Images.Media._ID + "=?"
        val cursor = context.contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            column, sel, arrayOf(id), null
        )

        val columnIndex = cursor!!.getColumnIndex(column[0])
        if (cursor.moveToFirst()) {
            profile_img = cursor.getString(columnIndex)
        }

        cursor.close()
        Glide.with(context).load("file://$profile_img")
            /* .apply(RequestOptions.circleCropTransform())*/.into(first!!)
        return "file://$profile_img"
        /*Prefs.putKey(context,"profile_img","file://"+profile_img);*/
    }


    fun singleCapsName(first: String?): String? {
        return if (first == null || first == "") {
            " "
        } else first.substring(0, 1).toUpperCase() + first.substring(1)
    }

    fun setImageUri(
        mContext: Context?,
        url: String?,
        imageView: ImageView?
    ) {
        val circularProgressDrawable = CircularProgressDrawable(mContext!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()
        val request = RequestOptions()
        request.placeholder(circularProgressDrawable)
        request.error(R.drawable.muslim_girl)
        if (url != null) {
            if (imageView != null) {
                Glide.with(mContext).load(BaseUrlImage + url)
                    // .apply(request)
                    .into(imageView)
            }
        }
    }

    fun setCircleImage(
        mContext: Context?,
        url: String?,
        imageView: ImageView?
    ) {

        val circularProgressDrawable = CircularProgressDrawable(mContext!!)
        circularProgressDrawable.strokeWidth = 5f
        circularProgressDrawable.centerRadius = 30f
        circularProgressDrawable.start()

        val request = RequestOptions()
        request.apply(RequestOptions.circleCropTransform())
        request.placeholder(circularProgressDrawable)
        request.error(R.drawable.muslim_girl)

        Glide.with(mContext)
            .load(BaseUrlImage + url) //                load(UrlHelper.BASE_URL+"storage/"+url)
            .apply(request)
            .into(imageView!!)
    }


}