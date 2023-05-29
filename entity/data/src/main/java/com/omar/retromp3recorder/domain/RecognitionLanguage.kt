package com.omar.retromp3recorder.domain


enum class RecognitionLanguage {
    English, IndianEnglish, Russian, French, Chinese, German, Spanish, Portuguese, Turkish, Vietnamese, Italian, Dutch, Catalan, Farsi, Ukrainian, Kazakh, Japanese, Hindi, Czeck, Polish, Uzbek, Korean
}


sealed interface LanguageState {
    data class ToDownload(val isFailed: Boolean) : LanguageState
    object Available : LanguageState
    data class DownLoading(val percent: Int) : LanguageState
    object Installing : LanguageState
}

data class LanguageAvailability(
    val language: RecognitionLanguage, val state: LanguageState
)
