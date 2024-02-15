package com.sparshchadha.workout_app.di

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.room.Room
import com.chuckerteam.chucker.api.ChuckerInterceptor
import com.google.gson.Gson
import com.sparshchadha.workout_app.alarm_manager.AndroidAlarmScheduler
import com.sparshchadha.workout_app.data.local.datastore.WorkoutAppDatastorePreference
import com.sparshchadha.workout_app.data.local.room_db.Converters
import com.sparshchadha.workout_app.data.local.room_db.WorkoutAppDatabase
import com.sparshchadha.workout_app.data.local.room_db.dao.FoodItemsDao
import com.sparshchadha.workout_app.data.local.room_db.dao.GymExercisesDao
import com.sparshchadha.workout_app.data.local.room_db.dao.RemindersDao
import com.sparshchadha.workout_app.data.local.room_db.dao.YogaDao
import com.sparshchadha.workout_app.data.remote.api.FoodApi
import com.sparshchadha.workout_app.data.remote.api.GymExercisesApi
import com.sparshchadha.workout_app.data.remote.api.PexelsApi
import com.sparshchadha.workout_app.data.remote.api.YogaApi
import com.sparshchadha.workout_app.data.repository.FoodRepositoryImpl
import com.sparshchadha.workout_app.data.repository.PexelsRepositoryImpl
import com.sparshchadha.workout_app.data.repository.RemindersRepositoryImpl
import com.sparshchadha.workout_app.data.repository.WorkoutRepositoryImpl
import com.sparshchadha.workout_app.domain.repository.FoodItemsRepository
import com.sparshchadha.workout_app.domain.repository.PexelsRepository
import com.sparshchadha.workout_app.domain.repository.RemindersRepository
import com.sparshchadha.workout_app.domain.repository.WorkoutRepository
import com.sparshchadha.workout_app.util.Constants.DATABASE_NAME
import com.sparshchadha.workout_app.util.GsonParser
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

    @Singleton
    @Provides
    fun provideDataStorePreference(@ApplicationContext context: Context): WorkoutAppDatastorePreference =
        WorkoutAppDatastorePreference(context)

    @Provides
    @Singleton
    fun provideHttpClient(
        @ApplicationContext context: Context,
    ): OkHttpClient {
        val appCache = Cache(File("cacheDir", "okhttpcache"), 10 * 1024 * 1024)

        val bootstrapClient = OkHttpClient.Builder()
            .cache(appCache)
            .proxy(Proxy.NO_PROXY)
            .build()

        val dns = DnsOverHttps.Builder()
            .client(bootstrapClient)
            .url("https://dns.google/dns-query".toHttpUrl())
            .bootstrapDnsHosts(InetAddress.getByName("8.8.4.4"), InetAddress.getByName("8.8.8.8"))
            .build()

        return OkHttpClient().newBuilder()
            .connectTimeout(60, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(60, TimeUnit.SECONDS)
            .dns(dns)
            .proxy(Proxy.NO_PROXY)
            .addInterceptor(ChuckerInterceptor(context))
            .build()
    }

    @Provides
    @Singleton
    fun provideFoodApi(
        okHttpClient: OkHttpClient,
    ): FoodApi {
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
        okHttpClient: OkHttpClient,
    ): PexelsApi {
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
        okHttpClient: OkHttpClient,
    ): YogaApi {
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
        okHttpClient: OkHttpClient,
    ): GymExercisesApi {
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
        api: FoodApi,
        foodItemsDao: FoodItemsDao,
        datastorePreference: WorkoutAppDatastorePreference
    ): FoodItemsRepository {
        return FoodRepositoryImpl(api = api, foodItemsDao = foodItemsDao, datastorePreference = datastorePreference)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Provides
    @Singleton
    fun providePexelsRepository(
        api: PexelsApi,
    ): PexelsRepository {
        return PexelsRepositoryImpl(api)
    }

    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    @Provides
    @Singleton
    fun provideWorkoutRepository(
        yogaApi: YogaApi,
        gymExercisesApi: GymExercisesApi,
        yogaDao: YogaDao,
        gymExercisesDao: GymExercisesDao
    ): WorkoutRepository {
        return WorkoutRepositoryImpl(yogaApi = yogaApi, gymExercisesApi = gymExercisesApi, yogaDao = yogaDao, gymExercisesDao = gymExercisesDao)
    }

    @Singleton
    @Provides
    fun provideDatabase(
        @ApplicationContext context: Context,
    ): WorkoutAppDatabase {
        return Room.databaseBuilder(
            context,
            WorkoutAppDatabase::class.java,
            DATABASE_NAME
        )
            .addTypeConverter(Converters(GsonParser(Gson())))
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideGymWorkoutsDao(database: WorkoutAppDatabase): GymExercisesDao {
        return database.gymWorkoutsDao()
    }

    @Singleton
    @Provides
    fun provideYogaDao(database: WorkoutAppDatabase): YogaDao {
        return database.yogaDao()
    }

    @Singleton
    @Provides
    fun provideFoodItemsDao(database: WorkoutAppDatabase): FoodItemsDao {
        return database.foodItemsDao()
    }

    @Singleton
    @Provides
    fun provideRemindersDao(database: WorkoutAppDatabase): RemindersDao {
        return database.remindersDao()
    }

    @Singleton
    @Provides
    fun provideRemindersRepository(remindersDao: RemindersDao): RemindersRepository {
        return RemindersRepositoryImpl(remindersDao = remindersDao)
    }

    @Singleton
    @Provides
    fun provideAlarmManager(
        @ApplicationContext context: Context,
    ) : AndroidAlarmScheduler {
        return AndroidAlarmScheduler(context)
    }
}