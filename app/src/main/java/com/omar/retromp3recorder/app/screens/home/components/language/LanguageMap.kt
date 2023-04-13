package com.omar.retromp3recorder.app.screens.home.components.language

import androidx.annotation.StringRes
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.domain.SpeechRecognitionLanguage

@StringRes
fun SpeechRecognitionLanguage.getDisplayNameRes() = when (this) {
    SpeechRecognitionLanguage.English -> R.string.lang_english
    SpeechRecognitionLanguage.IndianEnglish -> R.string.lang_indian_english
    SpeechRecognitionLanguage.Russian -> R.string.lang_russian
    SpeechRecognitionLanguage.French -> R.string.lang_french
    SpeechRecognitionLanguage.Chinese -> R.string.lang_chinese
    SpeechRecognitionLanguage.German -> R.string.lang_german
    SpeechRecognitionLanguage.Spanish -> R.string.lang_spanish
    SpeechRecognitionLanguage.Portuguese -> R.string.lang_portuguese
    SpeechRecognitionLanguage.Turkish -> R.string.lang_turkish
    SpeechRecognitionLanguage.Vietnamese -> R.string.lang_vietnamese
    SpeechRecognitionLanguage.Italian -> R.string.lang_italian
    SpeechRecognitionLanguage.Dutch -> R.string.lang_dutch
    SpeechRecognitionLanguage.Catalan -> R.string.lang_catalan
    SpeechRecognitionLanguage.Farsi -> R.string.lang_farsi
    SpeechRecognitionLanguage.Ukrainian -> R.string.lang_ukrainian
    SpeechRecognitionLanguage.Kazakh -> R.string.lang_kazakh
    SpeechRecognitionLanguage.Japanese -> R.string.lang_japanese
    SpeechRecognitionLanguage.Hindi -> R.string.lang_hindi
    SpeechRecognitionLanguage.Czeck -> R.string.lang_czeck
    SpeechRecognitionLanguage.Polish -> R.string.lang_polish
    SpeechRecognitionLanguage.Uzbek -> R.string.lang_uzbek
    SpeechRecognitionLanguage.Korean -> R.string.lang_korean
}
