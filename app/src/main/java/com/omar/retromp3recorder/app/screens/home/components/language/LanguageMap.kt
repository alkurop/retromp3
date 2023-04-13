package com.omar.retromp3recorder.app.screens.home.components.language

import androidx.annotation.StringRes
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.domain.RecognitionLanguage

@StringRes
fun RecognitionLanguage.getDisplayNameRes() = when (this) {
    RecognitionLanguage.English -> R.string.lang_english
    RecognitionLanguage.IndianEnglish -> R.string.lang_indian_english
    RecognitionLanguage.Russian -> R.string.lang_russian
    RecognitionLanguage.French -> R.string.lang_french
    RecognitionLanguage.Chinese -> R.string.lang_chinese
    RecognitionLanguage.German -> R.string.lang_german
    RecognitionLanguage.Spanish -> R.string.lang_spanish
    RecognitionLanguage.Portuguese -> R.string.lang_portuguese
    RecognitionLanguage.Turkish -> R.string.lang_turkish
    RecognitionLanguage.Vietnamese -> R.string.lang_vietnamese
    RecognitionLanguage.Italian -> R.string.lang_italian
    RecognitionLanguage.Dutch -> R.string.lang_dutch
    RecognitionLanguage.Catalan -> R.string.lang_catalan
    RecognitionLanguage.Farsi -> R.string.lang_farsi
    RecognitionLanguage.Ukrainian -> R.string.lang_ukrainian
    RecognitionLanguage.Kazakh -> R.string.lang_kazakh
    RecognitionLanguage.Japanese -> R.string.lang_japanese
    RecognitionLanguage.Hindi -> R.string.lang_hindi
    RecognitionLanguage.Czeck -> R.string.lang_czeck
    RecognitionLanguage.Polish -> R.string.lang_polish
    RecognitionLanguage.Uzbek -> R.string.lang_uzbek
    RecognitionLanguage.Korean -> R.string.lang_korean
}
