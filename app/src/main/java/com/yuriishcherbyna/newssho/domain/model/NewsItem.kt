package com.yuriishcherbyna.newssho.domain.model

data class NewsItem(
    val id: String? = null,
    val title: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceName: String
)
