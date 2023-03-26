package com.omar.retromp3recorder.utils.domain

interface ServiceDealer {
    fun startWakelockService()
    fun stopWakelockService()
    fun startMediaProjectionService()
    fun stopMediaProjectionService()
}
