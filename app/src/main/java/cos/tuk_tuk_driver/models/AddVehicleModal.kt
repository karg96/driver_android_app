package cos.tuk_tuk_driver.models

import com.google.gson.annotations.SerializedName

data class AddVehicleModal(

    @SerializedName("status") val status: Boolean,
    @SerializedName("data") val data: VehicleData,
    @SerializedName("message") val message: String


)

data class VehicleData(

    @SerializedName("id") val id: Int,
    @SerializedName("fleet") val fleet: Int,
    @SerializedName("first_name") val first_name: String,
    @SerializedName("last_name") val last_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("gender") val gender: String,
    @SerializedName("mobile") val mobile: String,
    @SerializedName("avatar") val avatar: String,
    @SerializedName("rating") val rating: String,
    @SerializedName("last_otp") val last_otp: String,
    @SerializedName("status") val status: String,
    @SerializedName("bio") val bio: String,
    @SerializedName("fun_fact") val fun_fact: String,
    @SerializedName("service") val service: Service

)

data class Service(

    @SerializedName("id") val id: Int,
    @SerializedName("provider_id") val provider_id: Int,
    @SerializedName("status") val status: String,
    @SerializedName("service_number") val service_number: String,
    @SerializedName("service_model") val service_model: String,
    @SerializedName("service_year") val service_year: String,
    @SerializedName("service_color") val service_color: String,
    @SerializedName("service_type_id") val service_type_id: String,
    @SerializedName("created_at") val created_at: String


)
