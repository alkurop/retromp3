package com.omar.retromp3recorder.app.ui.menu.logic

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class MenuViewModel : ViewModel() {
    @Inject
    lateinit var interactor: MenuInteractor

    private val compositeDisposable = CompositeDisposable()

    val state = BehaviorSubject.create<MenuView.State>()
    val input = PublishSubject.create<MenuView.Input>()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
