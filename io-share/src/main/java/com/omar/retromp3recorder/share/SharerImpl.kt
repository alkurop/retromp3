package com.omar.retromp3recorder.share

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.share.Sharer.Event.Error
import com.omar.retromp3recorder.share.Sharer.Event.SharingOk
import com.omar.retromp3recorder.utils.Constants.MAIN_THREAD
import com.omar.retromp3recorder.utils.FileUriCreator
import io.reactivex.rxjava3.core.Completable
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.subjects.PublishSubject
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class SharerImpl @Inject internal constructor(
    private val fileUriCreator: FileUriCreator,
    private val context: Context,
    @param:Named(MAIN_THREAD) private val mainThreadScheduler: Scheduler
) : Sharer {
    private val events = PublishSubject.create<Sharer.Event>()
    override fun share(file: File): Completable {
        return if (!file.exists()) {
            Completable.fromAction {
                events.onNext(Error(Stringer(R.string.file_not_exists)))
            }
        } else Completable
            .fromAction {
                val uri = collectForShare(file)
                val intent = initShareIntent(uri)
                context.startActivity(
                    Intent.createChooser(
                        intent,
                        context.getString(R.string.sh_select)
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            }
            .onErrorComplete {
                val cause = it.message
                val message =
                    if (cause == null) Stringer(R.string.sharing_failed) else Stringer.ofString(
                        cause
                    )
                events.onNext(Error(message))
                Timber.e(it)
                true
            }
            .subscribeOn(mainThreadScheduler)
    }

    override fun observeEvents(): Observable<Sharer.Event> {
        return events
    }

    @SuppressLint("SetWorldReadable")
    private fun collectForShare(file: File): Uri {
        file.setReadable(true, false)
        return fileUriCreator.createSharableUri(file)
    }

    private fun initShareIntent(uri: Uri): Intent {
        val intent = Intent()
        intent.action = Intent.ACTION_SEND
        intent.putExtra(Intent.EXTRA_STREAM, uri)
        intent.type = "audio/*"
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        return intent
    }
}
