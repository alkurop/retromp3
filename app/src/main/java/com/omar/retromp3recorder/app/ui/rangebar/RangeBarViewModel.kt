package com.omar.retromp3recorder.app.ui.rangebar

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class RangeBarViewModel : ViewModel() {

    @Inject
    lateinit var interactor: RangeBarInteractor

    val state = BehaviorSubject.create<RangeBarView.State>()
    val input = PublishSubject.create<RangeBarView.Input>()

    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        input
            .compose(interactor.processIO())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
