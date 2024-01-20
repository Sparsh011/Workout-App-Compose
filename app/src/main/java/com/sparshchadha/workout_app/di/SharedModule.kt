package com.sparshchadha.workout_app.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.sparshchadha.workout_app.data.remote.api.FoodApi
import com.sparshchadha.workout_app.data.remote.api.GymExercisesApi
import com.sparshchadha.workout_app.data.remote.api.PexelsApi
import com.sparshchadha.workout_app.data.remote.api.YogaApi
import com.sparshchadha.workout_app.data.repository.FoodRepositoryImpl
import com.sparshchadha.workout_app.data.repository.PexelsRepositoryImpl
import com.sparshchadha.workout_app.data.repository.WorkoutRepositoryImpl
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.domain.repository.PexelsRepository
import com.sparshchadha.workout_app.domain.repository.WorkoutRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
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
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
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

    @Provides
    @Singleton
    fun provideYogaApi(
        okHttpClient: OkHttpClient
    ) : YogaApi {
        return Retrofit.Builder()
            .baseUrl(YogaApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(YogaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideGymExercisesApi(
        okHttpClient: OkHttpClient
    ) : GymExercisesApi {
        return Retrofit.Builder()
            .baseUrl(GymExercisesApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
            .client(okHttpClient)
            .build()
            .create(GymExercisesApi::class.java)
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

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Provides
    @Singleton
    fun provideWorkoutRepository(
        yogaApi: YogaApi,
        gymExercisesApi: GymExercisesApi
    ) : WorkoutRepository {
        return WorkoutRepositoryImpl(yogaApi = yogaApi, gymExercisesApi = gymExercisesApi)
    }
}