package com.lex.postlist.application.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.lex.postlist.model.datasource.rest.RestApi
import com.lex.postlist.model.datasource.rest.config.ServerEndpoint
import com.lex.postlist.model.datasource.rest.config.SimpleServerEndpoint
import com.lex.postlist.model.datasource.rest.constant.RestOptions
import dagger.Module
import dagger.Provides
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton


@Module
class RetrofitModule {

    @Singleton
    @Provides
    fun provideUsersRestClient(retrofit: Retrofit): RestApi {
        return retrofit.create(RestApi::class.java)
    }

    @Singleton
    @Provides
    fun provideRetrofit(endpoint: ServerEndpoint, gson: Gson, client: OkHttpClient): Retrofit {
        return Retrofit.Builder()
                .baseUrl(endpoint.url())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(client)
                .build()
    }

    @Singleton
    @Provides
    fun provideServerEndpoint(): ServerEndpoint {
        return SimpleServerEndpoint()
    }

    @Singleton
    @Provides
    fun provideGson(): Gson {
        return GsonBuilder()
                .create()
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(RestOptions.TIMEOUT_CONNECTION_SECONDS, TimeUnit.SECONDS)
                .readTimeout(RestOptions.TIMEOUT_READ_SECONDS, TimeUnit.SECONDS)
                .writeTimeout(RestOptions.TIMEOUT_WRITE_SECONDS, TimeUnit.SECONDS)
                .build()
    }

    @Singleton
    @Provides
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }
}
