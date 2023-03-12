package com.omar.retromp3recorder.app.utils

fun String.toFileName(): String = this.split("/").last().split(".").first()
