package com.yuriishcherbyna.newssho.data.remote.dto

import com.squareup.moshi.Json

data class NewsItemDto(
    @Json(name = "source") val source: SourceDto,
    @Json(name = "author") val author: String?,
    @Json(name = "title") val title: String,
    @Json(name = "description") val description: String?,
    @Json(name = "url") val url: String,
    @Json(name = "urlToImage") val urlToImage: String?,
    @Json(name = "publishedAt") val publishedAt: String,
    @Json(name = "content") val content: String?
)
