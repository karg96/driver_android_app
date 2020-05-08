package cos.tuk_tuk_driver.models

import com.google.gson.annotations.SerializedName

data class GetVehicleServiceModal(

    @SerializedName("status") val status: Boolean,
    @SerializedName("data") val data: List<Services>,
    @SerializedName("message") val message: String

)


data class Services(

    @SerializedName("id") val id: Int,
    @SerializedName("capacity") val capacity: Int,
    @SerializedName("fixed") val fixed: Int,
    @SerializedName("price") val price: Int,
    @SerializedName("minute") val minute: Int,
    @SerializedName("stop_price") val stop_price: Int,
    @SerializedName("night_charges") val night_charges: Int,
    @SerializedName("airport_charges") val airport_charges: Int,
    @SerializedName("cancellation_fee") val cancellation_fee: Int,
    @SerializedName("platform_fee") val platform_fee: Int,
    @SerializedName("distance") val distance: Int,
    @SerializedName("surge") val surge: String,
    @SerializedName("calculator") val calculator: String,
    @SerializedName("description") val description: String,
    @SerializedName("name") val name: String,
    @SerializedName("status") val status: Int

)


