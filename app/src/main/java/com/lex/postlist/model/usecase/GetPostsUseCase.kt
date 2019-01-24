package com.lex.postlist.model.usecase

import com.lex.postlist.model.entity.PostEntity
import com.lex.postlist.model.repository.posts.PostsRepository
import io.reactivex.Flowable
import javax.inject.Inject


class GetPostsUseCase @Inject constructor(
        private val postsRepository: PostsRepository
) {

    fun execute(page: Int): Flowable<List<PostEntity>> {
        return postsRepository.posts(page)
    }
}