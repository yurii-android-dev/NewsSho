package com.yuriishcherbyna.newssho.domain.util

sealed interface DataError : Error {
    enum class Network: DataError {
        NO_INTERNET_CONNECTION,
        REQUEST_TIMEOUT,
        TOO_MANY_REQUESTS,
        SERVER_ERROR,
        UNAUTHORIZED,
        UNKNOWN
    }
}

fun httpCodeToNetworkError(code: Int): DataError.Network {
    return when (code) {
        400 -> DataError.Network.REQUEST_TIMEOUT
        401 -> DataError.Network.UNAUTHORIZED
        429 -> DataError.Network.TOO_MANY_REQUESTS
        500 -> DataError.Network.SERVER_ERROR
        else -> DataError.Network.UNKNOWN
    }
}