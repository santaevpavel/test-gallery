package ru.santaev.techtask.di.modules

import com.github.ajalt.timberkt.Timber
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.santaev.techtask.BuildConfig
import ru.santaev.techtask.network.api.UserApiService
import ru.santaev.techtask.network.auth.AuthInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
class NetworkModule {

    @Singleton
    @Provides
    fun provideApiService(
        retrofitBuilder: Retrofit.Builder,
    ): UserApiService {
        return retrofitBuilder.baseUrl(BASE_URL)
            .build()
            .create(UserApiService::class.java)
    }

    @Provides
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(HTTP_CLIENT_CONNECTION_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(HTTP_CLIENT_READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(HTTP_CLIENT_WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .addInterceptor(AuthInterceptor("Bearer", AUTH_TOKEN))
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    fun provideRetrofitBuilder(
        okHttpClient: OkHttpClient,
        gson: Gson
    ): Retrofit.Builder {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
    }


    @Singleton
    @Provides
    fun provideGsonConverter(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun provideLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor { message ->
            Timber.d { "Retrofit logging: $message" }
        }.apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    companion object {
        private const val BASE_URL = "https://gorest.co.in/"
        // TODO extract to build variable
        private const val AUTH_TOKEN = "a220f931c2472038f4c50e0777b058a8a9faa2579686728c0cf0d5ab5613b085"
        private const val HTTP_CLIENT_CONNECTION_TIMEOUT_SECONDS = 10L
        private const val HTTP_CLIENT_READ_TIMEOUT_SECONDS = 30L
        private const val HTTP_CLIENT_WRITE_TIMEOUT_SECONDS = 30L
    }
}
