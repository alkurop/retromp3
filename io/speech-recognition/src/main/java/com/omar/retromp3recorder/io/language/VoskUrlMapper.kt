package com.omar.retromp3recorder.io.language

import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.domain.RecognitionLanguage.*

fun RecognitionLanguage.getUrl():String {
    return when (this) {
        English -> "$BASE_URL/vosk-model-small-en-us-0.15.zip"
        IndianEnglish -> "$BASE_URL/vosk-model-small-en-in-0.4.zip"
        Russian -> "$BASE_URL/vosk-model-small-ru-0.22.zip"
        French -> "$BASE_URL/vosk-model-small-fr-0.22.zip"
        Chinese -> "$BASE_URL/vosk-model-small-cn-0.22.zip"
        German -> "$BASE_URL/vosk-model-small-de-0.15.zip"
        Spanish -> "$BASE_URL/vosk-model-small-es-0.42.zip"
        Portuguese -> "$BASE_URL/vosk-model-small-pt-0.3.zip"
        Turkish -> "$BASE_URL/vosk-model-small-tr-0.3.zip"
        Vietnamese -> "$BASE_URL/vosk-model-small-vn-0.4.zip"
        Italian -> "$BASE_URL/vosk-model-small-it-0.22.zip"
        Dutch -> "$BASE_URL/vosk-model-small-nl-0.22.zip"
        Catalan -> "$BASE_URL/vosk-model-small-ca-0.4.zip"
        Farsi -> "$BASE_URL/vosk-model-small-fa-0.4.zip"
        Ukrainian -> "$BASE_URL/vosk-model-small-uk-v3-nano.zip"
        Kazakh -> "$BASE_URL/vosk-model-small-kz-0.15.zip"
        Japanese -> "$BASE_URL/vosk-model-small-ja-0.22.zip"
        Hindi -> "$BASE_URL/vosk-model-small-hi-0.22.zip"
        Czeck -> "$BASE_URL/vosk-model-small-cs-0.4-rhasspy.zip"
        Polish -> "$BASE_URL/vosk-model-small-pl-0.22.zip"
        Uzbek -> "$BASE_URL/vosk-model-small-uz-0.22.zip"
        Korean -> "$BASE_URL/vosk-model-small-ko-0.22.zip"
    }
}

private const val BASE_URL = "https://alphacephei.com/vosk/models"

fun RecognitionLanguage.getFilename(): String = this.getUrl().split("/").last()

fun RecognitionLanguage.getModelDir(): String = this.getFilename().split(".zip").first()
