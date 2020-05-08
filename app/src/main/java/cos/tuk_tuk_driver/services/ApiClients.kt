package cos.tuk_tuk_driver.services


import android.widget.Toast

import com.google.gson.GsonBuilder
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import cos.tuk_tuk_driver.utils.URLHelper.BaseUrl
import cos.tuk_tuk_driver.DriverApp
import cos.tuk_tuk_driver.utils.Prefs


object ApiClients {


    var retrofit: Retrofit? = null
    private var okHttpClient: OkHttpClient? = null
    private var retrofit1: Retrofit? = null
    private var okHttpClient1: OkHttpClient? = null

    val client: Retrofit?
        get() {

            try {

                val gson = GsonBuilder()
                    .setLenient()
                    .create()

                initOkHttp()

                if (retrofit == null) {

                    retrofit = Retrofit.Builder()
                        .baseUrl(BaseUrl)
                        .client(okHttpClient!!)
                        // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                        .addConverterFactory(GsonConverterFactory.create(gson))
                        .build()
                }

            } catch (e: Exception) {
                Toast.makeText(DriverApp.context, "" + e, Toast.LENGTH_SHORT).show()
            }

            return retrofit

        }

    val client_token: Retrofit?
        get() {
            val gson = GsonBuilder()
                .setLenient()
                .create()

            initOkHttp_token()

            if (retrofit1 == null) {
                retrofit1 = Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .client(okHttpClient1!!)
                    // .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()
            }
            return retrofit1
        }


    private fun initOkHttp() {

        val httpClient = OkHttpClient().newBuilder()
        httpClient.connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor { chain ->
            val original = chain.request()

            val requestBuilder = original.newBuilder()
                .addHeader("Accept", "application/json")
                //                        .addHeader("Cache-Control", "no-cache")
                .addHeader("Content-Type", "application/json")
            // .addHeader("Authorization", "Bearer " + Prefs.getKey(GarageOwnerApp.context, "token"));
            //.header("Authorization", "Bearer " + Prefs.getKey(MechanicApp.context, "token"));

            val request = requestBuilder.build()
            //request.header(token);
            chain.proceed(request)
        }

        okHttpClient = httpClient.build()
    }

    private fun initOkHttp_token() {

        val httpClient = OkHttpClient().newBuilder()
        httpClient.connectTimeout(100, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)

        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        httpClient.addInterceptor(interceptor)
        httpClient.addInterceptor { chain ->
            val original = chain.request()
            val requestBuilder = original.newBuilder()
                //.addHeader("Accept", "application/json")
                .header(
                    "Authorization",
                    "Bearer " + Prefs.getKey(DriverApp.context, "Authorization")
                )
//                                    .header("Authorization", "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJodHRwOlwvXC9zZXJ2ZXIudmlzaW9udml2YW50ZS5jb206ODA4MFwvdHVrdHVrXC9wdWJsaWNcL2FwaVwvcHJvdmlkZXJcL29hdXRoXC90b2tlbiIsImlhdCI6MTU4ODA1NDgzNCwiZXhwIjo2MDE1ODgwNTQ3NzQsIm5iZiI6MTU4ODA1NDgzNCwianRpIjoiVWpGZGV3bEJQWk94d25DUyIsInN1YiI6MTU1LCJwcnYiOiJiMzY4ZmMwYjVhMGJlOTgxYjFkOTBiMTA0OTU4ZTg2NmJkZjYwZDViIn0.5epLSv9rFPQkbGfDcL8a8QhYxoIcyxvs2q5ARhhYbnI");


            val request = requestBuilder.build()
            //request.header(token);
            chain.proceed(request)
        }

        okHttpClient1 = httpClient.build()

    }


}
