package com.lex.postlist.application.util

import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.appcompat.app.AppCompatActivity
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