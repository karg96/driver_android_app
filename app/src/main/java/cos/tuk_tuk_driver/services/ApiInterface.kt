package cos.tuk_tuk_driver.services

import cos.tuk_tuk_driver.models.*
import cos.tuk_tuk_driver.utils.URLHelper
import cos.tuk_tuk_driver.utils.URLHelper.GetUploadDocs
import cos.tuk_tuk_driver.utils.URLHelper.Logout
import cos.tuk_tuk_driver.utils.URLHelper.MobileLogin
import cos.tuk_tuk_driver.utils.URLHelper.PaymentsList
import cos.tuk_tuk_driver.utils.URLHelper.RegisterMobile
import cos.tuk_tuk_driver.utils.URLHelper.Resend
import cos.tuk_tuk_driver.utils.URLHelper.UpdateDriverData
import cos.tuk_tuk_driver.utils.URLHelper.UploadDocs
import cos.tuk_tuk_driver.utils.URLHelper.VerifyOtp
import cos.tuk_tuk_driver.utils.URLHelper.doLogin
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {


    @POST(doLogin)
    @FormUrlEncoded
    fun Login(
        @Field("mobile") mobile: String,
        @Field("password") password: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String
    ): Call<RegisterModal>

    @POST(RegisterMobile)
    @FormUrlEncoded
    fun RegisterMobiles(
        @Field("mobile") mobile: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String
    ): Call<RegisterModal>

    @POST(VerifyOtp)
    @FormUrlEncoded
    fun VealidateOtp(
        @Field("mobile") mobile: String,
        @Field("device_token") device_token: String,
        @Field("otp") otp: String
    ): Call<OtpModal>


    @POST(Resend)
    @FormUrlEncoded
    fun ResendOTP(
        @Field("mobile") mobile: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String
    ): Call<RegisterModal>

    @POST(MobileLogin)
    @FormUrlEncoded
    fun mobileLogin(
        @Field("mobile") mobile: String,
        @Field("device_token") device_token: String,
        @Field("device_type") device_type: String
    ): Call<RegisterModal>

    @POST(UpdateDriverData)
    @FormUrlEncoded
    fun UpdateDriver(
        @Field("first_name") first_name: String,
        @Field("last_name") last_name: String,
        @Field("password") password: String,
        @Field("password_confirmation") password_confirmation: String,
        @Field("email") email: String
    ): Call<RegisterModal>


    @POST(Logout)
    @FormUrlEncoded
    fun logout(
        @Field("id") id: String
    ): Call<RegisterModal>

    @POST(UploadDocs)
    @Multipart
    fun uploadDocs(
        @Part("document_id") document_id: RequestBody?,
        @Part document: MultipartBody.Part,
        @Part("expiry") expiry: RequestBody?
    ): Call<UploadDocsModal>

    @POST(UploadDocs)
    @Multipart
    fun uploadDocsPassport(
        @Part("document_id") document_id: RequestBody?,
        @Part front: MultipartBody.Part,
        @Part back: MultipartBody.Part,
        @Part("expiry") expiry: RequestBody?
    ): Call<UploadDocsModal>

    @GET(GetUploadDocs)
    fun getUploadDocs(): Call<Documents>

    @POST(URLHelper.AddVehicle)
    @FormUrlEncoded
    fun addVehicle(
        @Field("service_type") service_type: String,
        @Field("service_number") service_number: String,
        @Field("service_model") service_model: String,
        @Field("service_year") service_year: String,
        @Field("service_color") service_color: String
    ): Call<AddVehicleModal>

    @POST(URLHelper.GetVehicle)
    @FormUrlEncoded
    fun getVehicle(@Field("empty") empty: String): Call<GetVehicleModal>

    @GET(URLHelper.MakePrime + "{id}/set-prime")
    fun makePrime(@Path("id") id: String): Call<GetVehicleModal>

    @GET(URLHelper.MakePrime + "{id}/unset-prime")
    fun unsetPrime(@Path("id") id: String): Call<GetVehicleModal>

    @POST(URLHelper.DeleteVehicle + "{id}")
    @FormUrlEncoded
    fun deleteVehicle(
        @Path("id") id: String,
        @Field("empty") empty: String
    ): Call<GetVehicleModal>

    @POST(URLHelper.UpdateVehicle)
    @FormUrlEncoded
    fun updateVehicle(
        @Path("id") id: String,
        @Field("service_type") service_type: String,
        @Field("service_model") service_model: String,
        @Field("service_year") service_year: String,
        @Field("service_color") service_color: String
    ): Call<AddVehicleModal>

    @POST(URLHelper.Services)
    @FormUrlEncoded
    fun services(
        @Field("empty") empty: String
    ): Call<GetVehicleServiceModal>

    @POST(URLHelper.ChangePayments)
    @FormUrlEncoded
    fun changePayments(
        @Field("method") method: String
    ): Call<GetpaymentModaal>

    @POST(URLHelper.ReportIssue)
    @FormUrlEncoded
    fun reportIssue(
        @Field("title") title: String,
        @Field("description") description: String,
        @Field("category") category: String
    ): Call<GetpaymentModaal>

    @POST(URLHelper.Available)
    @FormUrlEncoded
    fun available(
        @Field("service_status") service_status: String
    ): Call<GetpaymentModaal>

    @GET(PaymentsList)
    fun paymentsList(): Call<UploadDocsModal>


}
