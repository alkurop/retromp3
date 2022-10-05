package com.omar.retromp3recorder.app.ui.utils

import com.github.alkurop.ghostinshell.Shell

fun <T : Any> T?.toShell() = Shell(this)