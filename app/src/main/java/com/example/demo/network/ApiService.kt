package com.example.demo.network

import com.example.demo.network.data.Attractions
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import io.reactivex.Observable
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

private const val BASE_URL = "https://www.travel.taipei/open-api/"

private val httpClient = OkHttpClient.Builder()
    .addInterceptor { chain: Interceptor.Chain ->
        val original = chain.request()
        val request = original.newBuilder()
            .header("Accept", "application/json")
            .method(original.method(), original.body()).build()
        chain.proceed(request)
    }

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .client(httpClient.build())
    .baseUrl(BASE_URL)
    .build()

private val retrofitRx = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
    .client(httpClient.build())
    .baseUrl(BASE_URL)
    .build()

interface ApiService {
    @GET("zh-tw/attractions/all")
    fun getAll(): Deferred<Attractions>

    @GET("zh-tw/attractions/all")
    fun getAllRx(): Observable<Attractions>
}

object Api {
    val retrofitService: ApiService by lazy{
        retrofit.create(ApiService::class.java)
    }
    val retrofitRxService: ApiService by lazy{
        retrofitRx.create(ApiService::class.java)
    }
}
