package com.lex.postlist.screen.posts.view

import com.lex.postlist.model.viewobject.PostVo

/**
 * Developed by Magora Team (magora-systems.com). 2019.
 *
 * @author mihaylov
 */
sealed class ViewStatePosts {
    object LoadingStarted : ViewStatePosts()
    class Loaded(val postEntities: List<PostVo>) : ViewStatePosts()
    class Error(val message: String?) : ViewStatePosts()
}