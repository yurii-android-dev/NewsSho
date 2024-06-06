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