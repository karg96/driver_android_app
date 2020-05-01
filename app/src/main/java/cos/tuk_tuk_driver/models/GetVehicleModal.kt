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
    @SerializedName("service_color") val service_color: String,
    @SerializedName("serviceDetail") val serviceDetail: ServiceDetail

)

data class ServiceDetail(


    @SerializedName("id") val id: Int,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("fixed") val fixed: Int,
    @SerializedName("price") val price: Int,
    @SerializedName("stop_price") val stop_price: Int,
    @SerializedName("night_charges") val night_charges: Int,
    @SerializedName("airport_charges") val airport_charges: Int,
    @SerializedName("cancellation_fee") val cancellation_fee: Int,
    @SerializedName("minute") val minute: Int,
    @SerializedName("distance") val distance: Int,
    @SerializedName("status") val status: Int,
    @SerializedName("name") val name: String,
    @SerializedName("provider_name") val provider_name: String,
    @SerializedName("image") val image: String,
    @SerializedName("hour") val hour: String,
    @SerializedName("surge") val surge: String,
    @SerializedName("calculator") val calculator: String,
    @SerializedName("description") val description: String



)

