package com.lex.postlist.application.di

import android.content.Context
import com.lex.postlist.application.App
import dagger.Module
import dagger.Provides

import javax.inject.Singleton


@Module
class AppModule(private val app: App) {

    @Singleton
    @Provides
    fun provideContext(): Context {
        return app
    }

}