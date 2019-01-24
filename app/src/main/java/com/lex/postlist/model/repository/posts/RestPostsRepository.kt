package com.lex.postlist.model.repository.posts

import com.lex.postlist.model.datasource.rest.RestApi
import com.lex.postlist.model.entity.PostEntity
import io.reactivex.Flowable
import javax.inject.Inject

class RestPostsRepository @Inject constructor(
        private val restApi: RestApi
) : PostsRepository {
    override fun posts(page: Int): Flowable<List<PostEntity>> {
        return restApi.posts(page)
    }
}