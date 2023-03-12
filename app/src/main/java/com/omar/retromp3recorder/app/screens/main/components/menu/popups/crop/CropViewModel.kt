package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.domain.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class CropViewModel : ViewModel() {
    val state = BehaviorSubject.create<CropContract.State>()
    val input = PublishSubject.create<CropContract.Input>()

    @Inject
    lateinit var interactor: CropInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .compose(CropMapper.mapState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
