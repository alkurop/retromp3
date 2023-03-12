package com.omar.retromp3recorder.app.screens.settings

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.domain.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class SettingsViewModel : ViewModel() {
    val state = BehaviorSubject.create<SettingsView.State>()
    val input = PublishSubject.create<SettingsView.Input>()

    @Inject
    lateinit var interactor: SettingsInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.inject(this)
        input
            .compose(interactor.processIO())
            .compose(SettingsViewOutputMapper.mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
