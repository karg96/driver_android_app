package com.tuktuk.utils

import android.content.Context
import android.widget.Toast
import com.example.punjabrocks.services.ApiClients.client
import com.example.punjabrocks.services.ApiClients.client_token
import com.example.punjabrocks.services.ApiInterface

object  Comman {


    fun getApi(): ApiInterface? {
        return client!!.create(ApiInterface::class.java)
    }

    fun getApiToken(): ApiInterface? {
        return client_token!!.create(ApiInterface::class.java)
    }


    fun makeToast(context: Context?, message: String?) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }

}