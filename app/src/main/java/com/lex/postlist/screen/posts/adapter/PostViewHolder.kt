package com.lex.postlist.screen.posts.adapter

import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.lex.postlist.R
import com.lex.postlist.application.util.inflate
import com.lex.postlist.model.viewobject.PostVo


open class PostViewHolder constructor(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
    private val bodyText: TextView = itemView.findViewById(R.id.textBody)
    private val titleText: TextView = itemView.findViewById(R.id.textTitle)

    fun bindView(post: PostVo) {
        post.apply {
            bodyText.text = body
            titleText.text = title
        }
    }

    companion object {
        fun createHolder(parent: ViewGroup) =
                PostViewHolder(parent.inflate(R.layout.item_post))
    }
}
