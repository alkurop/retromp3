package com.omar.retromp3recorder.app.ui.files.preview

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class CurrentFileViewModel : ViewModel() {
    val state = BehaviorSubject.create<CurrentFileView.State>()
    val input = PublishSubject.create<CurrentFileView.Input>()

    @Inject
    lateinit var interactor: CurrentFileInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .compose(CurrentFileOutputMapper.mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
