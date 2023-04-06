package com.prashant.openapi.di

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.prashant.openapi.BuildConfig
import com.prashant.openapi.networkservice.RetrofitApi
import com.prashant.openapi.networkservice.Repository
import dagger.Provides
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton



/**
*A module class providing dependencies for the app using Dagger Hilt.
*@constructor Creates an instance of [Module].
 */
@InstallIn(SingletonComponent::class)
@Module
class Module {

    /**
     * Provides a [CoroutineExceptionHandler] to handle exceptions in coroutines.
     *
     * @return [CoroutineExceptionHandler] instance.
     */
    @Provides
    @Singleton
    fun exceptionHandler() = CoroutineExceptionHandler { _, t ->
        t.printStackTrace()

        CoroutineScope(Dispatchers.Main).launch {
            t.printStackTrace()
        }
    }

    /**
     * Provides the base url for the API.
     *
     * @return Base url as a string.
     */

    @Provides
    fun provideBaseUrl() = BuildConfig.BASE_URL

    /**
     * Provides an instance of [OkHttpClient] with required settings.
     *
     * @return [OkHttpClient] instance.
     */
    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient.Builder()
            .readTimeout(1, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .addInterceptor(loggingInterceptor)
            .build()
    }

    /**
     * Provides an instance of [GsonConverterFactory] for JSON parsing.
     *
     * @return [GsonConverterFactory] instance.
     */
    @Singleton
    @Provides
    fun gsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    /**
     * Provides an instance of [Retrofit] to make API requests.
     *
     * @param okHttpClient [OkHttpClient] instance.
     * @param gsonConverterFactory [GsonConverterFactory] instance.
     * @param BASE_URL Base url for the API.
     *
     * @return [Retrofit] instance.
     */
    @Singleton
    @Provides
    fun provideRetrofit(
        okHttpClient: OkHttpClient,
        gsonConverterFactory: GsonConverterFactory,
        BASE_URL: String
    ): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .addCallAdapterFactory(CoroutineCallAdapterFactory()).build()

    /**
     * Provides an instance of [RetrofitApi] to interact with the API.
     *
     * @param retrofit [Retrofit] instance.
     *
     * @return [RetrofitApi] instance.
     */
    @Provides
    @Singleton
    fun provideApiService(retrofit: Retrofit): RetrofitApi =
        retrofit.create(RetrofitApi::class.java)

    /**
     * Provides an instance of [Repository] to fetch data from the API.
     *
     * @param retrofitApi [RetrofitApi] instance.
     *
     * @return [Repository] instance.
     */
    @Provides
    @Singleton
    fun provideRepository(retrofitApi: RetrofitApi): Repository = Repository(retrofitApi)
}