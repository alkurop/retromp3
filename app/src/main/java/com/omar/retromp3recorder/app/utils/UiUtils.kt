package com.omar.retromp3recorder.app.utils

import android.view.View
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner

fun <T : View> Fragment.findViewById(@IdRes id: Int): T = this.requireView().findViewById(id)

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
                val observer = object : LifecycleEventObserver {
                    override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                        if (event == Lifecycle.Event.ON_DESTROY) {
                            lifecycle.removeObserver(this)
                            cachedValue = null
                        }
                    }
                }
                lifecycle.addObserver(observer)
                view
            }
        }
    }
}
