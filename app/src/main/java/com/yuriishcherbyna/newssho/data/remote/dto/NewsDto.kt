package com.yuriishcherbyna.newssho.data.remote.dto

import com.squareup.moshi.Json

data class NewsDto(
    @Json(name = "status") val status: String,
    @Json(name = "totalResults") val totalResults: Int,
    @Json(name = "articles") val articles: List<NewsItemDto>
)
