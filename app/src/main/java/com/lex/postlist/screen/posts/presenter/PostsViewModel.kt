package com.lex.postlist.screen.posts.presenter

import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.SingleLiveEvent
import androidx.lifecycle.ViewModel
import android.os.Parcelable
import com.lex.postlist.application.di.AppComponentHolder
import com.lex.postlist.model.datasource.rest.constant.RestConsts
import com.lex.postlist.model.mapper.MapperPost
import com.lex.postlist.model.usecase.GetPostsUseCase
import com.lex.postlist.model.viewobject.PostVo
import com.lex.postlist.screen.posts.view.ViewStatePosts
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject


@Suppress("ProtectedInFinal")
class PostsViewModel : ViewModel() {
    @Inject
    protected lateinit var getPostsUseCase: GetPostsUseCase
    private var usersDisposable: Disposable? = null
    val liveData = SingleLiveEvent<ViewStatePosts>()
    var lastPage = RestConsts.PAGE_START
    var postsList = ArrayList<PostVo>()
    var layoutManagerState: Parcelable? = null


    init {
        AppComponentHolder.component()?.inject(this)
    }

    fun loadPage(isRestore: Boolean, page: Int) {
        if (isRestore) {
            liveData.value = ViewStatePosts.Loaded(postsList)
            return
        }
        usersDisposable = getPostsUseCase.execute(page)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe { liveData.value = ViewStatePosts.LoadingStarted }
                .map { data -> MapperPost().mapTo(data) }
                .subscribe(
                        {
                            lastPage = page
                            postsList.addAll(it)
                            liveData.value = ViewStatePosts.Loaded(it)
                        },
                        { liveData.value = ViewStatePosts.Error(it.message) }
                )
    }

    override fun onCleared() {
        super.onCleared()
        usersDisposable?.dispose()
    }
}
