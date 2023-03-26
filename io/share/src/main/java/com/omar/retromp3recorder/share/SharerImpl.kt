package com.omar.retromp3recorder.share

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import com.github.alkurop.stringerbell.Stringer
import com.omar.retromp3recorder.share.Sharer.Event.Error
import com.omar.retromp3recorder.utils.platform.FileUriCreator
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import timber.log.Timber
import java.io.File
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SharerImpl @Inject internal constructor(
    private val fileUriCreator: FileUriCreator,
    @ApplicationContext private val context: Context,
) : Sharer {
    private val events = MutableSharedFlow<Sharer.Event>(replay = 0)
    override suspend fun share(file: File) {
        if (!file.exists()) {
            events.emit(Error(Stringer(R.string.file_not_exists)))
        } else {
            val uri = collectForShare(file)
            val intent = initShareIntent(uri)
            try {
                context.startActivity(
                    Intent.createChooser(
                        intent, context.getString(R.string.sh_select)
                    ).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                )
            } catch (throwable: Throwable) {
                Timber.e(throwable)
                val cause = throwable.message
                val message =
                    if (cause == null) Stringer(R.string.sharing_failed) else Stringer.ofString(
                        cause
                    )
                events.emit(Error(message))
            }
        }
    }

    override fun flow(): Flow<Sharer.Event> {
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
