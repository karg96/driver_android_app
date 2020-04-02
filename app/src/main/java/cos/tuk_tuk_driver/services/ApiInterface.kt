package com.example.punjabrocks.services

import com.tuktuk.models.OtpModal
import com.tuktuk.models.RegisterModal
import com.tuktuk.utils.URLHelper.Logout
import com.tuktuk.utils.URLHelper.MobileLogin
import com.tuktuk.utils.URLHelper.RegisterMobile
import com.tuktuk.utils.URLHelper.Resend
import com.tuktuk.utils.URLHelper.UpdateDriverData
import com.tuktuk.utils.URLHelper.VerifyOtp
import com.tuktuk.utils.URLHelper.doLogin
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST


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
    fun MobileLogin(
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

}
