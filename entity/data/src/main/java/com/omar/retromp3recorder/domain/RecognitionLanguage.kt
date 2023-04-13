package com.omar.retromp3recorder.domain


enum class RecognitionLanguage {
    English, IndianEnglish, Russian, French, Chinese, German, Spanish, Portuguese, Turkish, Vietnamese, Italian, Dutch, Catalan, Farsi, Ukrainian, Kazakh, Japanese, Hindi, Czeck, Polish, Uzbek, Korean
}


enum class LanguageState {
    Available, Usable, Downloading
}

data class LanguageAvailability(
    val language: RecognitionLanguage, val state: LanguageState
)
