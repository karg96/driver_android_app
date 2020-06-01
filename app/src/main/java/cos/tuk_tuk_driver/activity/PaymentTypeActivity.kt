package cos.tuk_tuk_driver.activity

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog

import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.R
import cos.tuk_tuk_driver.databinding.ActivityPaymentTypeBinding
import cos.tuk_tuk_driver.models.GetpaymentModaal
import cos.tuk_tuk_driver.models.RegisterModal
import cos.tuk_tuk_driver.models.UploadDocsModal
import cos.tuk_tuk_driver.utils.Comman
import cos.tuk_tuk_driver.utils.Prefs
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PaymentTypeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentTypeBinding
    private val apiInterface = Comman.getApiToken()
    private var paymentMode: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentTypeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        try {

            paymentMode = Prefs.getKey(applicationContext, "payment_mode")

            init()
            getPaymentMethod()

        } catch (Ex: Exception) {

        }
    }

    private fun init() {

        if (paymentMode.equals(
                "CASH",
                ignoreCase = true
            )
        ) {
            binding.radioCash.visibility = View.VISIBLE
            binding.radioCard.visibility = View.GONE
            binding.radiopaypal.visibility = View.GONE
            binding.radioPostPaid.visibility = View.GONE

        }
        if (paymentMode.equals(
                "CARD",
                ignoreCase = true
            )
        ) {
            binding.radioCard.visibility = View.VISIBLE
            binding.radioCash.visibility = View.GONE
            binding.radiopaypal.visibility = View.GONE
            binding.radioPostPaid.visibility = View.GONE
        }
        if (paymentMode.equals(
                "PAYPAL",
                ignoreCase = true
            )
        ) {
            binding.radioPostPaid.visibility = View.VISIBLE
            binding.radioCash.visibility = View.GONE
            binding.radiopaypal.visibility = View.GONE
            binding.radioCard.visibility = View.GONE
        }
        if (paymentMode.equals(
                "TUKTUKPOSTPAID",
                ignoreCase = true
            )
        ) {
            binding.radioPostPaid.visibility = View.VISIBLE
            binding.radioCash.visibility = View.GONE
            binding.radiopaypal.visibility = View.GONE
            binding.radioCard.visibility = View.GONE
        }

        binding.cashPay.setOnClickListener {
            DeleteAlerts("CASH")
        }
        binding.cardPay.setOnClickListener {
            DeleteAlerts("CARD")
        }
        binding.paypalPay.setOnClickListener {
            DeleteAlerts("PAYPAL")
        }
        binding.postpaidPay.setOnClickListener {
            DeleteAlerts("TUKTUKPOSTPAID")
        }
        binding.close.setOnClickListener {
            finish()
        }
    }


    fun DeleteAlerts(method: String) {

        // Initialize a new instance of
        val builder = AlertDialog.Builder(this, R.style.AlertDialogCustom)

        // Set the alert dialog title
        builder.setTitle("Delete")

        // Display a message on alert dialog
        builder.setMessage("Are you sure! Do you want to change your payment method to ${method.toUpperCase()} ?")

        // Set a positive button and its click listener on alert dialog
        builder.setPositiveButton("YES") { dialog, which ->
            // Do something when user press the positive button
            ChangePayments(method.toUpperCase())

        }


        // Display a negative button on alert dialog
        builder.setNegativeButton("No") { dialog, which ->

        }


        // Finally, make the alert dialog using builder
        val dialog: AlertDialog = builder.create()

        // Display the alert dialog on app interface
        dialog.show()
    }

    fun ChangePayments(method: String) {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.changePayments(method.toUpperCase())
                .enqueue(object : Callback<GetpaymentModaal> {
                    override fun onFailure(call: Call<GetpaymentModaal>, t: Throwable) {
                        dialog.dismiss()
                        Comman.makeToast(applicationContext, "Please try again later")

                    }

                    override fun onResponse(
                        call: Call<GetpaymentModaal>,
                        response: Response<GetpaymentModaal>
                    ) {
                        try {

                            dialog.dismiss()

                            if (response.body()!!.status) {

                                Comman.makeToast(applicationContext, response.body()!!.message)

                                if (response.body()!!.data != null) {

                                    Prefs.putKey(
                                        applicationContext,
                                        "payment_mode",
                                        response.body()!!.data.payment_mode
                                    )

                                    if (response.body()!!.data.payment_mode.equals(
                                            "CASH",
                                            ignoreCase = true
                                        )
                                    ) {
                                        binding.radioCash.visibility = View.VISIBLE
                                        binding.radioCard.visibility = View.GONE
                                        binding.radiopaypal.visibility = View.GONE
                                        binding.radioPostPaid.visibility = View.GONE

                                    }
                                    if (response.body()!!.data.payment_mode.equals(
                                            "CARD",
                                            ignoreCase = true
                                        )
                                    ) {
                                        binding.radioCard.visibility = View.VISIBLE
                                        binding.radioCash.visibility = View.GONE
                                        binding.radiopaypal.visibility = View.GONE
                                        binding.radioPostPaid.visibility = View.GONE
                                    }
                                    if (response.body()!!.data.payment_mode.equals(
                                            "PAYPAL",
                                            ignoreCase = true
                                        )
                                    ) {
                                        binding.radiopaypal.visibility = View.VISIBLE
                                        binding.radioCash.visibility = View.GONE
                                        binding.radioPostPaid.visibility = View.GONE
                                        binding.radioCard.visibility = View.GONE
                                    }
                                    if (response.body()!!.data.payment_mode.equals(
                                            "TUKTUKPOSTPAID",
                                            ignoreCase = true
                                        )
                                    ) {
                                        binding.radioPostPaid.visibility = View.VISIBLE
                                        binding.radioCash.visibility = View.GONE
                                        binding.radiopaypal.visibility = View.GONE
                                        binding.radioCard.visibility = View.GONE
                                    }
                                }

                            } else {

                                Comman.makeToast(applicationContext, response.body()!!.message)

                            }

                        } catch (Ex: Exception) {

                        }

                    }

                })
        } catch (Ex: Exception) {
            Comman.makeToast(applicationContext, "Please try again later")

        }

    }

    fun getPaymentMethod() {

        try {

            val dialog = ProgressDialog(this)
            dialog.setMessage("Please wait....")
            dialog.show()

            apiInterface!!.paymentsList()
                .enqueue(object : Callback<UploadDocsModal> {
                    override fun onFailure(call: Call<UploadDocsModal>, t: Throwable) {
                        dialog.dismiss()

                    }

                    override fun onResponse(
                        call: Call<UploadDocsModal>,
                        response: Response<UploadDocsModal>
                    ) {
                        try {

                            dialog.dismiss()

                            if (response.body()!!.status) {

                                if (response.body()!!.methods != null && response.body()!!.methods.size != 0) {

                                    for (x in 0 until response.body()!!.methods.size) {
                                        if (response.body()!!.methods.get(x).equals(
                                                "CASH",
                                                ignoreCase = true
                                            )
                                        ) {
                                            binding.cash.visibility = View.VISIBLE
                                        }
                                        if (response.body()!!.methods.get(x).equals(
                                                "CARD",
                                                ignoreCase = true
                                            )
                                        ) {
                                            binding.card.visibility = View.VISIBLE
                                        }
                                        if (response.body()!!.methods.get(x).equals(
                                                "PAYPAL",
                                                ignoreCase = true
                                            )
                                        ) {
                                            binding.paypal.visibility = View.VISIBLE
                                        }
                                        if (response.body()!!.methods.get(x).equals(
                                                "TUKTUKPOSTPAID",
                                                ignoreCase = true
                                            )
                                        ) {
                                            binding.postpaid.visibility = View.VISIBLE
                                        }
                                    }
                                }

                            } else {

                                Comman.makeToast(applicationContext, "Please try again later")

                            }

                        } catch (Ex: Exception) {

                        }

                    }

                })
        } catch (Ex: Exception) {
            Comman.makeToast(applicationContext, "Please try again later")

        }

    }

}
