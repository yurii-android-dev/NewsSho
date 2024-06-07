package com.yuriishcherbyna.newssho.presentation.util

import com.yuriishcherbyna.newssho.R
import com.yuriishcherbyna.newssho.domain.util.DataError

fun DataError.Network.toNetworkErrorMessageId(): Int {
    return when(this) {
        DataError.Network.NO_INTERNET_CONNECTION -> R.string.no_internet
        DataError.Network.REQUEST_TIMEOUT -> R.string.the_request_timed_out
        DataError.Network.TOO_MANY_REQUESTS -> R.string.too_many_requests
        DataError.Network.SERVER_ERROR -> R.string.server_error
        DataError.Network.UNAUTHORIZED -> R.string.unauthorized
        DataError.Network.UNKNOWN -> R.string.unknown_error
    }
}