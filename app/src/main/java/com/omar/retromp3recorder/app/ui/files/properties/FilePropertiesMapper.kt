package com.omar.retromp3recorder.app.ui.files.properties

import com.omar.retromp3recorder.storage.db.AppDatabase
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import javax.inject.Inject

class FilePropertiesMapper @Inject constructor(
    private val appDatabase: AppDatabase,
    private val currentFileRepo: CurrentFileRepo,
    private val scheduler: Scheduler
) {
    fun observe(): Observable<PropertiesView.Output.CurrentFileProperties> {
        return currentFileRepo.observe()
            .flatMap { currentFile ->
                val file = currentFile.value
                if (file == null) Observable.just(
                    PropertiesView.Output.CurrentFileProperties(
                        null
                    )
                )
                else Observable.fromCallable {
                    PropertiesView.Output.CurrentFileProperties(
                        appDatabase.fileEntityDao().getByFilepath(file.path).firstOrNull()?.toFileWrapper()
                    )
                }
            }
            .subscribeOn(scheduler)
            .share()
    }
}
