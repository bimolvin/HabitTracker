package com.example.habittracker.retrofit

import com.example.habittracker.json.DateTimeAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object RetrofitBuilder {
    private const val baseUrl = "https://mej1g.wiremockapi.cloud/"

    private val moshi: Moshi = Moshi.Builder()
        .add(DateTimeAdapter)
        .addLast(KotlinJsonAdapterFactory())
        .build()

    fun getInstance(): Retrofit {
        return Retrofit.Builder().baseUrl(baseUrl)
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
    }
}