package com.lex.postlist.application.util

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.ViewModelProviders
import android.support.annotation.LayoutRes
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup



inline fun ViewGroup.inflate(@LayoutRes id: Int): View = LayoutInflater.from(context).inflate(id, this, false)

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(): T = ViewModelProviders.of(this)[T::class.java]

inline fun <T> LiveData<T>.observeNonNull(owner: AppCompatActivity, crossinline observer: (T) -> Unit) {
    observe(owner, Observer { observer(it!!) })
}

fun View?.gone() {
    this?.visibility = View.GONE
}

fun View?.visible() {
    this?.visibility = View.VISIBLE
}