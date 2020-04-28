package com.tuktuk.utils

import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.DocumentsContract
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import cos.tuk_tuk_driver.services.ApiClients.client
import cos.tuk_tuk_driver.services.ApiClients.client_token
import cos.tuk_tuk_driver.services.ApiInterface

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


}