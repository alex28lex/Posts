package com.lex.postlist.application.di.holder


interface ComponentHolder<T> {

  fun component(): T?

  fun bindComponent(component: T)

  fun unbindComponent()
}