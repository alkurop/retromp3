package com.omar.retromp3recorder.app.screens.home.components.language

import androidx.annotation.StringRes
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.domain.VoskRecognitionLanguage

@StringRes
fun VoskRecognitionLanguage.getDisplayNameRes() = when (this) {
    VoskRecognitionLanguage.English -> R.string.lang_english
    VoskRecognitionLanguage.IndianEnglish -> R.string.lang_indian_english
    VoskRecognitionLanguage.Russian -> R.string.lang_russian
    VoskRecognitionLanguage.French -> R.string.lang_french
    VoskRecognitionLanguage.Chinese -> R.string.lang_chinese
    VoskRecognitionLanguage.German -> R.string.lang_german
    VoskRecognitionLanguage.Spanish -> R.string.lang_spanish
    VoskRecognitionLanguage.Portuguese -> R.string.lang_portuguese
    VoskRecognitionLanguage.Turkish -> R.string.lang_turkish
    VoskRecognitionLanguage.Vietnamese -> R.string.lang_vietnamese
    VoskRecognitionLanguage.Italian -> R.string.lang_italian
    VoskRecognitionLanguage.Dutch -> R.string.lang_dutch
    VoskRecognitionLanguage.Catalan -> R.string.lang_catalan
    VoskRecognitionLanguage.Farsi -> R.string.lang_farsi
    VoskRecognitionLanguage.Ukrainian -> R.string.lang_ukrainian
    VoskRecognitionLanguage.Kazakh -> R.string.lang_kazakh
    VoskRecognitionLanguage.Japanese -> R.string.lang_japanese
    VoskRecognitionLanguage.Hindi -> R.string.lang_hindi
    VoskRecognitionLanguage.Czeck -> R.string.lang_czeck
    VoskRecognitionLanguage.Polish -> R.string.lang_polish
    VoskRecognitionLanguage.Uzbek -> R.string.lang_uzbek
    VoskRecognitionLanguage.Korean -> R.string.lang_korean
}
