package com.ashpreet.demo.githubuserrepo.di

import com.ashpreet.demo.githubuserrepo.api.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "https://api.github.com/"
    private const val REQUEST_INTERVAL_MS = 1000L // 1 second between requests

    private var lastRequestTime = 0L // Stores the last request timestamp

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                synchronized(this) {
                    val currentTime = System.currentTimeMillis()
                    val elapsedTime = currentTime - lastRequestTime

                    if (elapsedTime < REQUEST_INTERVAL_MS) {
                        Thread.sleep(REQUEST_INTERVAL_MS - elapsedTime) // Wait before next request
                    }

                    lastRequestTime = System.currentTimeMillis() // Update timestamp
                }

                chain.proceed(chain.request()) // Continue with request
            }
            .build()
    }

    @Singleton
    @Provides
    fun provideRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Singleton
    @Provides
    fun provideApiService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }
}