package com.tuktuk.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cos.tuk_tuk_driver.R

@SuppressLint("Registered")
open class BaseActivity() : AppCompatActivity() {

    /**
     * this method is used when we move from one activity to
     * another
     */
    fun moveToNextScreen(
        srcActivity: Activity, destination: Class<Any>, bundle: Bundle?,
        isFinish: Boolean
    ) {

        val intent = Intent(srcActivity, destination)

        if (bundle != null)
            intent.putExtras(bundle)
        srcActivity.startActivity(intent)
        if (isFinish)
            srcActivity.finish()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
    }

}