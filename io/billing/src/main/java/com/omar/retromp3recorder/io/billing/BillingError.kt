package com.omar.retromp3recorder.io.billing

sealed class BillingError(message: String) : Throwable(message) {
    class ConsoleError(_message: String) : BillingError(_message)
    object UserCanceled : BillingError("User canceled")
    class OtherError(message: String) : BillingError(message)
    class ConnectionError(_message: String) : BillingError(_message)
}

fun Throwable.isUserCanceled() = this is BillingError.UserCanceled
