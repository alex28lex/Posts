package com.lex.postlist.application.di

import com.lex.postlist.screen.posts.presenter.PostsViewModel
import dagger.Component
import javax.inject.Singleton


@Singleton
@Component(modules = [AppModule::class, RetrofitModule::class, RepositoryModule::class])
interface AppComponent {
    fun inject(injectable: PostsViewModel)
}