package com.lex.postlist.model.repository.posts

import com.lex.postlist.model.entity.PostEntity
import io.reactivex.Flowable


interface PostsRepository {
    fun posts(page: Int): Flowable<List<PostEntity>>
}
