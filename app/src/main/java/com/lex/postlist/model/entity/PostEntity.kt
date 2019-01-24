package com.lex.postlist.model.entity

import com.google.gson.annotations.SerializedName


@Suppress("MemberVisibilityCanBePrivate")
data class PostEntity(
        @SerializedName("title")
        val title: String = "",
        @SerializedName("body")
        val body: String = ""
)
