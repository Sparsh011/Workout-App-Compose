package com.sparshchadha.workout_app.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.sparshchadha.workout_app.data.remote.FoodApi
import com.sparshchadha.workout_app.data.remote.PexelsApi
import com.sparshchadha.workout_app.data.remote.dto.pexels.PexelResponseDto
import com.sparshchadha.workout_app.data.repository.FoodRepositoryImpl
import com.sparshchadha.workout_app.data.repository.PexelsRepositoryImpl
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.domain.repository.PexelsRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.dnsoverhttps.DnsOverHttps
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.net.InetAddress
import java.net.Proxy
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object SharedModule {

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context
        ) : OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor(ChuckerInterceptor(context))
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodApi(
        okHttpClient: OkHttpClient
    ) : FoodApi {
        return Retrofit.Builder()
            .baseUrl(FoodApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(FoodApi::class.java)
    }

    @Provides
    @Singleton
    fun providePexelApi(
        okHttpClient: OkHttpClient
    ) : PexelsApi {
        return Retrofit.Builder()
            .baseUrl(PexelsApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(PexelsApi::class.java)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Provides
    @Singleton
    fun provideFoodItemsRepository(
        api: FoodApi
    ) : FoodItemsRepository {
        return FoodRepositoryImpl(api)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Provides
    @Singleton
    fun providePexelsRepository(
        api: PexelsApi
    ) : PexelsRepository {
        return PexelsRepositoryImpl(api)
    }
}