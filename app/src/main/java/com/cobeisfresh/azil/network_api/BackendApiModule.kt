package com.cobeisfresh.azil.network_api

import com.cobeisfresh.azil.App
import com.cobeisfresh.azil.BuildConfig
import com.cobeisfresh.azil.R
import com.cobeisfresh.azil.common.utils.isEmpty
import com.cobeisfresh.azil.shared_prefs.SharedPrefs
import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by Zerina Salitrezic on 31/07/2017.
 */


@Module
class BackendApiModule {

    companion object {
        private const val BACKEND_BASE_URL = "BASE_URL"
        private const val LOGGING_INTERCEPTOR = "LOGGING_INTERCEPTOR"
        private const val AUTHENTICATION_INTERCEPTOR = "AUTHENTICATION_INTERCEPTOR"
        private const val AUTHENTICATION_STATIC_VALUE = ""
        private const val AUTHORIZATION = ""
        private const val AUTHORIZATION_STATIC = ""
    }

    @Provides
    @Named(BACKEND_BASE_URL)
    fun provideBaseUrl(): String = App.get().getString(R.string.backend_url)

    @Provides
    fun provideRxJava2CallAdapterFactory(): RxJava2CallAdapterFactory = RxJava2CallAdapterFactory.create()

    @Provides
    fun provideGsonConverterFactory(): GsonConverterFactory = GsonConverterFactory.create()

    @Provides
    @Named(LOGGING_INTERCEPTOR)
    fun provideDebugInterceptor(): Interceptor {
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        return interceptor
    }

    @Provides
    @Named(AUTHENTICATION_INTERCEPTOR)
    fun provideAuthenticationInterceptor(sharedPrefs: SharedPrefs): Interceptor =
            Interceptor { chain ->
                val original = chain.request()

                val requestBuilder = original.newBuilder()
                requestBuilder.addHeader(AUTHORIZATION_STATIC, AUTHENTICATION_STATIC_VALUE)
                if (!isEmpty(sharedPrefs.getToken())) {
                    requestBuilder.addHeader(AUTHORIZATION, sharedPrefs.getToken())
                }

                val request = requestBuilder.build()
                chain.proceed(request)
            }

    @Provides
    fun provideOkHttpClient(@Named(LOGGING_INTERCEPTOR) loggingInterceptor: Interceptor,
                            @Named(AUTHENTICATION_INTERCEPTOR) authenticationInterceptor: Interceptor): OkHttpClient = OkHttpClient.Builder().apply {
        connectTimeout(120, TimeUnit.SECONDS)
        readTimeout(120, TimeUnit.SECONDS)
        writeTimeout(120, TimeUnit.SECONDS)
        addInterceptor(authenticationInterceptor)

        if (BuildConfig.DEBUG) {
            addInterceptor(loggingInterceptor)
        }
    }.build()

    @Provides
    @Singleton
    fun provideRetrofit(rxJava2CallAdapterFactory: RxJava2CallAdapterFactory, gsonConverterFactory: GsonConverterFactory,
                        okHttpClient: OkHttpClient, @Named(BACKEND_BASE_URL) baseUrl: String): Retrofit =
            Retrofit.Builder()
                    .baseUrl(baseUrl)
                    .client(okHttpClient)
                    .addConverterFactory(gsonConverterFactory)
                    .addCallAdapterFactory(rxJava2CallAdapterFactory)
                    .build()

    @Provides
    @Singleton
    fun provideUserApiService(retrofit: Retrofit): UserApiService = retrofit.create(UserApiService::class.java)

    @Provides
    @Singleton
    fun provideDogsApiService(retrofit: Retrofit): DogsApiService = retrofit.create(DogsApiService::class.java)
}