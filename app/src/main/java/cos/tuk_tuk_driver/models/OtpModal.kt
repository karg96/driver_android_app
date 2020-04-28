package cos.tuk_tuk_driver.models

import com.google.gson.annotations.SerializedName

data class OtpModal(

        @SerializedName("status") val status: Boolean,
        @SerializedName("password") val password: Boolean,
        @SerializedName("error") val list: List<Error>,
        @SerializedName("data") val data: Data


)
