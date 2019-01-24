package com.lex.postlist.model.datasource.rest

import com.lex.postlist.model.entity.PostEntity
import io.reactivex.Flowable
import retrofit2.http.GET
import retrofit2.http.Query

interface RestApi {
    @GET("posts")
    fun posts(@Query("_page") page: Int?): Flowable<List<PostEntity>>
}
