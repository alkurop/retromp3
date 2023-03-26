package com.omar.retromp3recorder.bl.audio.record

import com.omar.retromp3recorder.utils.platform.Mp3TagsEditor
import com.omar.retromp3recorder.utils.platform.RecordingTagsDefaultProvider
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class SaveMp3TagsUCSuspend @Inject constructor(
    private val mp3TagsEditor: Mp3TagsEditor,
    private val recordingTagsDefaultProvider: RecordingTagsDefaultProvider,
) {
    private var coroutineContext: CoroutineContext = Job()

    suspend fun execute(filepath: String) {
        coroutineContext.cancel()
        coroutineContext = Job()
        withContext(coroutineContext) {
            mp3TagsEditor.setTags(
                filepath,
                recordingTagsDefaultProvider.provideDefaults().copy(
                    title = mp3TagsEditor.getFilenameFromPath(filepath)
                )
            )
        }
    }
}
