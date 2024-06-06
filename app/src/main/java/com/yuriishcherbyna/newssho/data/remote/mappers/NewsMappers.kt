package com.yuriishcherbyna.newssho.data.remote.mappers

import com.yuriishcherbyna.newssho.data.remote.dto.NewsItemDto
import com.yuriishcherbyna.newssho.domain.model.NewsItem

fun NewsItemDto.toNewsItem(): NewsItem {
    return NewsItem(
        title = title,
        url = url,
        urlToImage = urlToImage,
        publishedAt = publishedAt,
        sourceName = source.name
    )
}

