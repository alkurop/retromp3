package com.omar.retromp3recorder.app.uiutils

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable


fun <T : Any> Observable<T>.subscribe(
    lifecycleOwner: LifecycleOwner,
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)? = null
) {
    val observable = this.observeOn(AndroidSchedulers.mainThread())
    val disposable =
        if (onError != null) observable.subscribe(onNext, onError)
        else observable.subscribe(onNext)
    val lifecycle = lifecycleOwner.lifecycle
    val observer = object : LifecycleEventObserver {
        override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
            if (event == Lifecycle.Event.ON_DESTROY) {
                disposable.dispose()
                lifecycle.removeObserver(this)
            }
        }
    }
    lifecycle.addObserver(observer)
}

fun <T : Any> Observable<T>.observe(
    lifecycleOwner: LifecycleOwner,
    onNext: (T) -> Unit,
    onError: ((Throwable) -> Unit)
) = this.subscribe(lifecycleOwner, onNext, onError)

fun <T : Any> Observable<T>.observe(
    lifecycleOwner: LifecycleOwner,
    onNext: (T) -> Unit
) = this.subscribe(lifecycleOwner, onNext)