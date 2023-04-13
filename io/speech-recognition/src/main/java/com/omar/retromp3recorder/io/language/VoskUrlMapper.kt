package com.omar.retromp3recorder.io.language

import com.omar.retromp3recorder.domain.RecognitionLanguage
import com.omar.retromp3recorder.domain.RecognitionLanguage.*

fun RecognitionLanguage.getUrl():String {
    return when (this) {
        English -> "https://alphacephei.com/vosk/models/vosk-model-small-en-us-0.15.zip"
        IndianEnglish -> "https://alphacephei.com/vosk/models/vosk-model-small-en-in-0.4.zip"
        Russian -> "https://alphacephei.com/vosk/models/vosk-model-small-ru-0.22.zip"
        French -> "https://alphacephei.com/vosk/models/vosk-model-small-fr-0.22.zip"
        Chinese -> "https://alphacephei.com/vosk/models/vosk-model-small-cn-0.22.zip"
        German -> "https://alphacephei.com/vosk/models/vosk-model-small-de-0.15.zip"
        Spanish -> "https://alphacephei.com/vosk/models/vosk-model-small-es-0.42.zip"
        Portuguese -> "https://alphacephei.com/vosk/models/vosk-model-small-pt-0.3.zip"
        Turkish -> "https://alphacephei.com/vosk/models/vosk-model-small-tr-0.3.zip"
        Vietnamese -> "https://alphacephei.com/vosk/models/vosk-model-small-vn-0.4.zip"
        Italian -> "https://alphacephei.com/vosk/models/vosk-model-small-it-0.22.zip"
        Dutch -> "https://alphacephei.com/vosk/models/vosk-model-small-nl-0.22.zip"
        Catalan -> "https://alphacephei.com/vosk/models/vosk-model-small-ca-0.4.zip"
        Farsi -> "https://alphacephei.com/vosk/models/vosk-model-small-fa-0.4.zip"
        Ukrainian -> "https://alphacephei.com/vosk/models/vosk-model-small-uk-v3-nano.zip"
        Kazakh -> "https://alphacephei.com/vosk/models/vosk-model-small-kz-0.15.zip"
        Japanese -> "https://alphacephei.com/vosk/models/vosk-model-small-ja-0.22.zip"
        Hindi -> "https://alphacephei.com/vosk/models/vosk-model-small-hi-0.22.zip"
        Czeck -> "https://alphacephei.com/vosk/models/vosk-model-small-cs-0.4-rhasspy.zip"
        Polish -> "https://alphacephei.com/vosk/models/vosk-model-small-pl-0.22.zip"
        Uzbek -> "https://alphacephei.com/vosk/models/vosk-model-small-uz-0.22.zip"
        Korean -> "https://alphacephei.com/vosk/models/vosk-model-small-ko-0.22.zip"
    }

}

fun RecognitionLanguage.getFilename(): String = this.getUrl().split("/")[0]
