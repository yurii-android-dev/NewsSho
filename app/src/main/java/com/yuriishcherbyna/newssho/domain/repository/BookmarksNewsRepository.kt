package com.yuriishcherbyna.newssho.domain.repository

import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.domain.util.DataError
import com.yuriishcherbyna.newssho.domain.util.Result

interface BookmarksNewsRepository {

    fun saveNews(newsItem: NewsItem): Result<Unit, DataError.Network>

    suspend fun getAllNews(): Result<List<NewsItem>, DataError.Network>

    suspend fun deleteNews(savedNewsId: String): Result<Unit, DataError.Network>

    suspend fun toogleBookmark(newsItem: NewsItem): Result<Unit, DataError.Network>

}