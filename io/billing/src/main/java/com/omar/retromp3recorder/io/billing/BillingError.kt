package com.omar.retromp3recorder.io.billing

sealed class BillingError(message: String) : Throwable(message) {
    class ConsoleError(message: String) : BillingError(message)
    object UserCanceled : BillingError("User canceled")
    class OtherError(message: String) : BillingError(message)
    class ConnectionError(message: String) : BillingError(message)
}

fun Throwable.isUserCanceled() = this is BillingError.UserCanceled
