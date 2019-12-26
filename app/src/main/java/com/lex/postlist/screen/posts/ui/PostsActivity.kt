package com.lex.postlist.screen.posts.ui

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.Toast
import com.kotterknife.KotterKnife
import com.kotterknife.bindView
import com.lex.postlist.R
import com.lex.postlist.application.util.getViewModel
import com.lex.postlist.application.util.gone
import com.lex.postlist.application.util.observeNonNull
import com.lex.postlist.application.util.visible
import com.lex.postlist.screen.posts.RxPagination
import com.lex.postlist.screen.posts.adapter.PostsAdapter
import com.lex.postlist.screen.posts.presenter.PostsViewModel
import com.lex.postlist.screen.posts.view.ViewStatePosts


class PostsActivity : AppCompatActivity() {

    private val recyclerView by bindView<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView)
    private val progressView by bindView<View>(R.id.progressView)

    private lateinit var viewModel: PostsViewModel
    private val postsAdapter = PostsAdapter()


    @SuppressLint("CheckResult")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_posts_list)
        KotterKnife.reset(this)
        viewModel = getViewModel()
        recyclerView.apply {
            adapter = postsAdapter
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)

        }
        with(viewModel) {
            liveData.observeNonNull(this@PostsActivity, ::update)
        }

        RxPagination.with(recyclerView, viewModel.lastPage).observePageChanges().subscribe {
            viewModel.loadPage(false, it + 1)
        }
        viewModel.loadPage(savedInstanceState != null, viewModel.lastPage)


    }

    override fun onSaveInstanceState(state: Bundle) {
        super.onSaveInstanceState(state)
        viewModel.layoutManagerState = recyclerView.layoutManager?.onSaveInstanceState()

    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle?) {
        if (viewModel.layoutManagerState != null) {
            recyclerView.layoutManager?.onRestoreInstanceState(viewModel.layoutManagerState)
        }
    }

    private fun update(event: ViewStatePosts) {
        when (event) {
            is ViewStatePosts.LoadingStarted -> progressView.visible()
            is ViewStatePosts.Loaded -> {
                progressView.gone()
                postsAdapter.addData(event.postEntities)
            }
            is ViewStatePosts.Error -> showError(event.message)
        }
    }

    private fun showError(message: String?) {
        progressView.gone()
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()

    }

}
