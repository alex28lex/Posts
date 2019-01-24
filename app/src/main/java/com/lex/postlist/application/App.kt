package com.lex.postlist.application

import android.app.Application
import com.lex.postlist.BuildConfig
import com.lex.postlist.application.di.AppComponentHolder
import com.lex.postlist.application.di.AppModule
import com.lex.postlist.application.di.DaggerAppComponent


open class App : Application() {

  override fun onCreate() {
    super.onCreate()
    initDi()
  }



  private fun initDi() {
    AppComponentHolder.bindComponent(DaggerAppComponent.builder().appModule(AppModule(this)).build())
  }
}
