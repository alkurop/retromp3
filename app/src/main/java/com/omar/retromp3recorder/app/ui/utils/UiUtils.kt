package com.omar.retromp3recorder.app.ui.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.recyclerview.widget.RecyclerView

fun <T : View> Fragment.findViewById(@IdRes id: Int): T = this.requireView().findViewById(id)

fun <T : View> RecyclerView.ViewHolder.findViewById(@IdRes id: Int): T =
    this.itemView.findViewById(id)

/**
 * It releases the view when called Fragment.onDestroyView
 */
fun <View : android.view.View> Fragment.lazyView(@IdRes id: Int): Lazy<View> {
    return object : Lazy<View> {
        override val value: View
            get() = findOrCached()

        //this is only used for toString() - IDC, fuck it
        override fun isInitialized(): Boolean = false

        private var cachedValue: View? = null

        private fun findOrCached(): View {
            return if (cachedValue != null) cachedValue!!
            else {
                val view = findViewById<View>(id)
                cachedValue = view
                val lifecycle = this@lazyView.viewLifecycleOwner.lifecycle
                val observer = object : LifecycleObserver {
                    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                    fun onDestroy() {
                        lifecycle.removeObserver(this)
                        cachedValue = null
                    }
                }
                lifecycle.addObserver(observer)
                view
            }
        }
    }
}
