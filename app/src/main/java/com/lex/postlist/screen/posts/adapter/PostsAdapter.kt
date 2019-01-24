package com.lex.postlist.screen.posts.adapter

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.lex.postlist.model.viewobject.PostVo


class PostsAdapter : RecyclerView.Adapter<PostViewHolder>() {
    var posts: ArrayList<PostVo> = ArrayList()

    fun addData(posts: List<PostVo>) {
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    fun setData(posts: List<PostVo>) {
        this.posts.clear()
        this.posts.addAll(posts)
        notifyDataSetChanged()
    }

    fun getItem(position: Int): PostVo {
        return posts[position]
    }

    override fun onCreateViewHolder(parent: ViewGroup, p: Int): PostViewHolder {
        val holder = PostViewHolder.createHolder(parent)
        holder.itemView.setOnClickListener { v ->
        }
        return holder
    }

    override fun getItemCount(): Int {
        return posts.size
    }

    override fun onBindViewHolder(holder: PostViewHolder, position: Int) {
        holder.bindView(getItem(position))
    }

}
