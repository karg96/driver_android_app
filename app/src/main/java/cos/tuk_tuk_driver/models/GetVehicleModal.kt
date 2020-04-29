package cos.tuk_tuk_driver.models

import com.google.gson.annotations.SerializedName

data class GetVehicleModal(

    @SerializedName("status") val status: Boolean,
    @SerializedName("vehicles") val vehicles: List<Vehicles>,
    @SerializedName("message") val message: String

)

data class Vehicles(

    @SerializedName("id") val id: Int,
    @SerializedName("provider_id") val fleet: Int,
    @SerializedName("service_type_id") val service_type_id: Int,
    @SerializedName("prime") val prime: Int,
    @SerializedName("active_status") val active_status: Int,
    @SerializedName("chassis_number") val chassis_number: String,
    @SerializedName("status") val status: String,
    @SerializedName("service_number") val service_number: String,
    @SerializedName("service_model") val service_model: String,
    @SerializedName("service_year") val service_year: String,
    @SerializedName("service_color") val service_color: String

)

