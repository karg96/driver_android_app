package cos.tuk_tuk_driver.model

import com.google.gson.annotations.SerializedName

data class GetpaymentModaal(

    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: UserData

)


data class UserData(

    @SerializedName("id") val id: Int,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("status") val status: String,
    @SerializedName("latitude") val latitude: String,
    @SerializedName("longitude") val longitude: String,
    @SerializedName("fleet") val fleet: Int,
    @SerializedName("otp") val otp: Int,
    @SerializedName("otp_count") val otp_count: Int,
    @SerializedName("new_register") val new_register: Int,
    @SerializedName("experince") val experince: String,
    @SerializedName("last_otp") val last_otp: String,
    @SerializedName("login_by") val login_by: String,
    @SerializedName("social_unique_id") val social_unique_id: String,
    @SerializedName("access_token") val access_token: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("fun_fact") val fun_fact: String,
    @SerializedName("payment_mode") val payment_mode: String,
    @SerializedName("updated_at") val updated_at: String,
    @SerializedName("free_triel_date") val free_triel_date: String


)
