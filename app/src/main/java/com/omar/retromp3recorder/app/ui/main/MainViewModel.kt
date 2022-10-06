package com.omar.retromp3recorder.app.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.app.ui.main.MainViewOutputMapper.mapOutputToState
import com.omar.retromp3recorder.storage.repo.global.PopupRepo
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

abstract class ViewModelWithId(val key: String) : ViewModel()

class FactoryWithId(private val key: String = "default") : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(String::class.java).newInstance(key)
    }
}

class MainViewModel(key: String) : ViewModelWithId(key) {
    val state = BehaviorSubject.create<MainView.State>()
    val input = PublishSubject.create<MainView.Input>()

    @Inject
    lateinit var interactor: MainViewInteractor
    @Inject
    lateinit var popupRepo: PopupRepo

    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .compose(mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }


}
