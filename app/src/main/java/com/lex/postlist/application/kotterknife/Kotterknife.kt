@file:Suppress("unused")

package com.kotterknife

import android.app.Activity
import android.app.Dialog
import android.view.View
import java.util.*
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

object KotterKnife {
    fun reset(target: Any) = LazyRegistry.reset(target)
}

fun <V : View> View.bindView(id: Int): ReadOnlyProperty<View, V> = required(id, viewFinder)

fun <V : View> Activity.bindView(id: Int): ReadOnlyProperty<Activity, V> = required(id, viewFinder)

fun <V : View> Dialog.bindView(id: Int): ReadOnlyProperty<Dialog, V> = required(id, viewFinder)

fun <V : View> androidx.fragment.app.DialogFragment.bindView(id: Int): ReadOnlyProperty<androidx.fragment.app.DialogFragment, V> = required(id, viewFinder)

fun <V : View> androidx.fragment.app.Fragment.bindView(id: Int): ReadOnlyProperty<androidx.fragment.app.Fragment, V> = required(id, viewFinder)

fun <V : View> androidx.recyclerview.widget.RecyclerView.ViewHolder.bindView(id: Int): ReadOnlyProperty<androidx.recyclerview.widget.RecyclerView.ViewHolder, V> = required(id, viewFinder)

fun <V : View> View.bindOptionalView(id: Int): ReadOnlyProperty<View, V?> = optional(id, viewFinder)

fun <V : View> Activity.bindOptionalView(id: Int): ReadOnlyProperty<Activity, V?> = optional(id, viewFinder)

fun <V : View> Dialog.bindOptionalView(id: Int): ReadOnlyProperty<Dialog, V?> = optional(id, viewFinder)

fun <V : View> androidx.fragment.app.DialogFragment.bindOptionalView(id: Int): ReadOnlyProperty<androidx.fragment.app.DialogFragment, V?> = optional(id, viewFinder)

fun <V : View> androidx.fragment.app.Fragment.bindOptionalView(id: Int): ReadOnlyProperty<androidx.fragment.app.Fragment, V?> = optional(id, viewFinder)

fun <V : View> androidx.recyclerview.widget.RecyclerView.ViewHolder.bindOptionalView(id: Int): ReadOnlyProperty<androidx.recyclerview.widget.RecyclerView.ViewHolder, V?> = optional(id, viewFinder)

fun <V : View> View.bindViews(vararg ids: Int): ReadOnlyProperty<View, List<V>> = required(ids, viewFinder)

fun <V : View> Activity.bindViews(vararg ids: Int): ReadOnlyProperty<Activity, List<V>> = required(ids, viewFinder)

fun <V : View> Dialog.bindViews(vararg ids: Int): ReadOnlyProperty<Dialog, List<V>> = required(ids, viewFinder)

fun <V : View> androidx.fragment.app.DialogFragment.bindViews(vararg ids: Int): ReadOnlyProperty<androidx.fragment.app.DialogFragment, List<V>> = required(ids, viewFinder)

fun <V : View> androidx.fragment.app.Fragment.bindViews(vararg ids: Int): ReadOnlyProperty<androidx.fragment.app.Fragment, List<V>> = required(ids, viewFinder)

fun <V : View> androidx.recyclerview.widget.RecyclerView.ViewHolder.bindViews(vararg ids: Int): ReadOnlyProperty<androidx.recyclerview.widget.RecyclerView.ViewHolder, List<V>> = required(ids, viewFinder)

fun <V : View> View.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<View, List<V>> = optional(ids, viewFinder)

fun <V : View> Activity.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<Activity, List<V>> = optional(ids, viewFinder)

fun <V : View> Dialog.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<Dialog, List<V>> = optional(ids, viewFinder)

fun <V : View> androidx.fragment.app.DialogFragment.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<androidx.fragment.app.DialogFragment, List<V>> = optional(ids, viewFinder)

fun <V : View> androidx.fragment.app.Fragment.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<androidx.fragment.app.Fragment, List<V>> = optional(ids, viewFinder)

fun <V : View> androidx.recyclerview.widget.RecyclerView.ViewHolder.bindOptionalViews(vararg ids: Int): ReadOnlyProperty<androidx.recyclerview.widget.RecyclerView.ViewHolder, List<V>> = optional(ids, viewFinder)

private val View.viewFinder: View.(Int) -> View? get() = { findViewById(it) }

private val Activity.viewFinder: Activity.(Int) -> View? get() = { findViewById(it) }

private val Dialog.viewFinder: Dialog.(Int) -> View? get() = { findViewById(it) }

private val androidx.fragment.app.DialogFragment.viewFinder: androidx.fragment.app.DialogFragment.(Int) -> View?
    get() = { dialog?.findViewById(it) ?: view?.findViewById(it) }

private val androidx.fragment.app.Fragment.viewFinder: androidx.fragment.app.Fragment.(Int) -> View? get() = { view?.findViewById(it) }

@Suppress("UNNECESSARY_SAFE_CALL")
private val androidx.recyclerview.widget.RecyclerView.ViewHolder.viewFinder: androidx.recyclerview.widget.RecyclerView.ViewHolder.(Int) -> View?
    get() = { itemView?.findViewById(it) }

private fun viewNotFound(id: Int, desc: KProperty<*>): Nothing = throw IllegalStateException("View ID $id for '${desc.name}' not found.")

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(id: Int, finder: T.(Int) -> View?) = Lazy { t: T, desc ->
    t.finder(id) as V? ?: viewNotFound(id, desc)
}

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(id: Int, finder: T.(Int) -> View?) = Lazy { t: T, _ -> t.finder(id) as V? }

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> required(ids: IntArray, finder: T.(Int) -> View?) = Lazy { t: T, desc ->
    ids.map { t.finder(it) as V? ?: viewNotFound(it, desc) }
}

@Suppress("UNCHECKED_CAST")
private fun <T, V : View> optional(ids: IntArray, finder: T.(Int) -> View?) = Lazy { t: T, _ -> ids.map { t.finder(it) as V? }.filterNotNull() }

// Like Kotlin's lazy delegate but the initializer gets the target and metadata passed to it
private class Lazy<in T, out V>(private val initializer: (T, KProperty<*>) -> V) : ReadOnlyProperty<T, V> {
    private object EMPTY

    private var value: Any? = EMPTY

    override fun getValue(thisRef: T, property: KProperty<*>): V {
        LazyRegistry.register(thisRef!!, this)

        if (value == EMPTY) {
            value = initializer(thisRef, property)
        }

        @Suppress("UNCHECKED_CAST")
        return value as V
    }

    fun reset() {
        value = EMPTY
    }
}

private object LazyRegistry {
    private val lazyMap = WeakHashMap<Any, MutableCollection<Lazy<*, *>>>()

    fun register(target: Any, lazy: Lazy<*, *>) {
        lazyMap.getOrPut(target) { Collections.newSetFromMap(WeakHashMap()) }.add(lazy)
    }

    fun reset(target: Any) {
        lazyMap[target]?.forEach { it.reset() }
    }
}