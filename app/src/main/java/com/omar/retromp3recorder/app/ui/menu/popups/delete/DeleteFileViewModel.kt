package com.omar.retromp3recorder.app.ui.menu.popups.delete

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.App
import com.omar.retromp3recorder.utils.domain.disposedBy
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.subjects.BehaviorSubject
import io.reactivex.rxjava3.subjects.PublishSubject
import javax.inject.Inject

class DeleteFileViewModel : ViewModel() {
    val state = BehaviorSubject.create<DeleteFileContract.State>()
    val input = PublishSubject.create<DeleteFileContract.Input>()

    @Inject
    lateinit var interactor: DeleteFileInteractor
    private val compositeDisposable = CompositeDisposable()

    init {
        App.appComponent.getComponent().inject(this)
        input
            .compose(interactor.processIO())
            .compose(DeleteFileOutputMapper.mapOutputToState())
            .subscribe(state::onNext)
            .disposedBy(compositeDisposable)
    }

    override fun onCleared() {
        compositeDisposable.clear()
    }
}
