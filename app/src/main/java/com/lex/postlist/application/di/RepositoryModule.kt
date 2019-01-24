package com.lex.postlist.application.di

import android.content.Context
import com.lex.postlist.model.datasource.rest.RestApi
import com.lex.postlist.model.repository.posts.PostsRepository
import com.lex.postlist.model.repository.posts.RestPostsRepository

import dagger.Module
import dagger.Provides
import javax.inject.Singleton


@Module
class RepositoryModule {
    @Singleton
    @Provides
    fun providePostsRepository(restApi: RestApi): PostsRepository {
        return RestPostsRepository(restApi)
    }
}