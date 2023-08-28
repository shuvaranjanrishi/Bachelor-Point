package com.therishideveloper.bachelorpoint.api

import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


/**
 * Created by Shuva Ranjan Rishi on 28,August,2023
 * BABL, Bangladesh,
 */

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    val database: DatabaseReference = Firebase.database.reference

    var gson = GsonBuilder()
        .setLenient()
        .create()

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl("https://bachelor-point-f2f71-default-rtdb.firebaseio.com/")
            .build()
    }

    @Singleton
    @Provides
    fun provideMemberApi(retrofit: Retrofit): MemberApi {
        return retrofit.create(MemberApi::class.java)
    }
}