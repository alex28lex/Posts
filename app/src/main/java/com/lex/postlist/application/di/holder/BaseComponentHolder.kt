package com.lex.postlist.application.di.holder


abstract class BaseComponentHolder<T> : ComponentHolder<T> {
  private var component: T? = null

  override fun component(): T? {
    return component
  }

  override fun bindComponent(component: T) {
    this.component = component
  }

  override fun unbindComponent() {
    this.component = null
  }
}