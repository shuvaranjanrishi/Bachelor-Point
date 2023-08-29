package com.therishideveloper.bachelorpoint.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Created by Shuva Ranjan Rishi on 28,August,2023
 * BABL, Bangladesh,
 */

@InstallIn(SingletonComponent::class)
@Module
class ApiClient {

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    private var client: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor { chain ->
            val url = chain
                .request()
                .url()
                .newBuilder()
                .addQueryParameter("auth", Constant.authKey)
                .build()
            chain.proceed(chain.request().newBuilder().url(url).build())
        }.build()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(Constant.baseUrl)
            .client(client)
            .build()
    }

    @Singleton
    @Provides
    fun provideMemberApi(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}