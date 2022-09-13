package com.omar.retromp3recorder.bl.system

import com.omar.retromp3recorder.share.Sharer
import com.omar.retromp3recorder.storage.repo.CurrentFileRepo
import com.omar.retromp3recorder.utils.takeOne
import io.reactivex.rxjava3.core.Completable
import java.io.File
import javax.inject.Inject

class ShareUC @Inject constructor(
    private val sharingModule: Sharer,
    private val currentFileRepo: CurrentFileRepo
) {
    fun execute(): Completable {
        return currentFileRepo
            .observe()
            .takeOne()
            .flatMapCompletable { fileName ->
                sharingModule.share(File(fileName.value!!.path))
            }
    }
}
