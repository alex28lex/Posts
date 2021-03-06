package com.lex.postlist.screen.posts

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lex.postlist.model.datasource.rest.constant.RestConsts
import io.reactivex.Observable
import io.reactivex.disposables.Disposable

open class RxPagination private constructor(
        private val recyclerView: androidx.recyclerview.widget.RecyclerView,
        private var page: Int,
        private val pageSize: Int,
        private val gap: Int
) {
    private var emitted: Boolean = false

    companion object {
        fun with(recyclerView: androidx.recyclerview.widget.RecyclerView): RxPagination {
            return RxPagination(
                    recyclerView,
                    RestConsts.PAGE_START,
                    RestConsts.PAGE_SIZE,
                    RestConsts.PAGE_GAP
            )
        }

        fun with(recyclerView: androidx.recyclerview.widget.RecyclerView, page: Int): RxPagination {
            return RxPagination(
                    recyclerView,
                    page,
                    RestConsts.PAGE_SIZE,
                    RestConsts.PAGE_GAP
            )
        }

        fun with(recyclerView: androidx.recyclerview.widget.RecyclerView, page: Int, additionalElements: Int): RxPagination {
            return RxPagination(
                    recyclerView,
                    page,
                    RestConsts.PAGE_SIZE + additionalElements,
                    RestConsts.PAGE_GAP
            )
        }

    }

    fun observePageChanges(): Observable</*Page*/Int> {
        return Observable.create { emitter ->
            if (recyclerView.layoutManager is androidx.recyclerview.widget.LinearLayoutManager || recyclerView.layoutManager is androidx.recyclerview.widget.GridLayoutManager) {

                var onScrollListener: androidx.recyclerview.widget.RecyclerView.OnScrollListener? = null
                val adapter = recyclerView.adapter
                var dataObserver: RxPaginationAdapterDataObserver? = null
                if (adapter != null) {
                    dataObserver = object : RxPaginationAdapterDataObserver() {
                        override fun onDataChanged() {
                            emitted = false
                        }
                    }

                    adapter.registerAdapterDataObserver(dataObserver)
                }
                val layoutManager = if (recyclerView.layoutManager is androidx.recyclerview.widget.LinearLayoutManager) {
                    recyclerView.layoutManager as androidx.recyclerview.widget.LinearLayoutManager
                } else {
                    recyclerView.layoutManager as androidx.recyclerview.widget.GridLayoutManager
                }
                onScrollListener = object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
                    override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                        super.onScrolled(recyclerView, dx, dy)

                        if (emitted) {
                            /*Do nothing*/
                        } else {
                            val maxItems = layoutManager.itemCount % pageSize != 0
//
//                            val calculatedMax = pageSize + pageSize * page
//                            val realMax = layoutManager.itemCount
//                            val maxItems = realMax < calculatedMax

                            if (maxItems) {
                                /*Max items received from server. Do nothing. Only set emitted = true, because we already emit all items*/
                                emitted = true
                            } else {
                                val lastItemIndex = layoutManager.itemCount - 1
                                val lastVisiblePosition = layoutManager.findLastVisibleItemPosition()
                                val theEnd = (lastVisiblePosition >= lastItemIndex - gap)

                                if (theEnd) {
                                    page++
                                    emitter.onNext(page)
                                    emitted = true
                                }
                            }
                        }
                    }
                }
                recyclerView.addOnScrollListener(onScrollListener)
                emitter.setDisposable(object : Disposable {
                    private var isDisposed = false
                    override fun isDisposed(): Boolean {
                        return isDisposed
                    }

                    override fun dispose() {
                        recyclerView.removeOnScrollListener(onScrollListener)
                        adapter?.unregisterAdapterDataObserver(dataObserver!!)
                    }
                })
            } else {
                emitter.onError(IllegalLayoutManagerException())
            }
        }
    }

    class IllegalLayoutManagerException :
            RuntimeException("LayoutManager must be LinearLayoutManager")

    abstract class RxPaginationAdapterDataObserver : androidx.recyclerview.widget.RecyclerView.AdapterDataObserver() {
        override fun onChanged() {
            onDataChanged()
        }

        override fun onItemRangeRemoved(positionStart: Int, itemCount: Int) {
            onDataChanged()
        }

        override fun onItemRangeMoved(fromPosition: Int, toPosition: Int, itemCount: Int) {
            onDataChanged()
        }

        override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
            onDataChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int) {
            onDataChanged()
        }

        override fun onItemRangeChanged(positionStart: Int, itemCount: Int, payload: Any?) {
            onDataChanged()
        }

        abstract fun onDataChanged()
    }
}