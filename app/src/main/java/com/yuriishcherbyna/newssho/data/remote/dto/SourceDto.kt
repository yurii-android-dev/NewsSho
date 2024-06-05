package com.yuriishcherbyna.newssho.data.remote.dto

import com.squareup.moshi.Json

data class SourceDto(
    @Json(name = "id") val id: String?,
    @Json(name = "name") val name: String
)
