package com.yuriishcherbyna.newssho.domain.repository

import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.data.remote.dto.NewsItemDto
import com.yuriishcherbyna.newssho.domain.util.DataError
import com.yuriishcherbyna.newssho.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getLatestNews(category: Category): Flow<Result<List<NewsItemDto>, DataError.Network>>

    fun searchNews(query: String): Flow<Result<List<NewsItemDto>, DataError.Network>>

}