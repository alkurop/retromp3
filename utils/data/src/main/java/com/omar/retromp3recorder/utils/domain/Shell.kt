package com.omar.retromp3recorder.utils.domain

import com.github.alkurop.ghostinshell.Shell


fun <T> T?.toShell()  = Shell(this)
