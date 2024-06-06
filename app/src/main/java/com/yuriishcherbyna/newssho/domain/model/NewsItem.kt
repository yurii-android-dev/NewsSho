package com.yuriishcherbyna.newssho.domain.model

data class NewsItem(
    val title: String,
    val url: String,
    val urlToImage: String?,
    val publishedAt: String,
    val sourceName: String
)
