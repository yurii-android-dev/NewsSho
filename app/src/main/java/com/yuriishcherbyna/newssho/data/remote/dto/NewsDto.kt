package com.yuriishcherbyna.newssho.data.remote.dto

import com.squareup.moshi.Json

data class NewsDto(
    @Json(name = "source") val source: SourceDto,
    @Json(name = "articles") val articles: List<NewsItemDto>
)
