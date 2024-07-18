package com.yuriishcherbyna.newssho.domain.repository

import androidx.paging.PagingData
import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.domain.util.DataError
import com.yuriishcherbyna.newssho.domain.util.Result
import kotlinx.coroutines.flow.Flow

interface NewsRepository {

    fun getLatestNews(category: Category): Flow<Result<List<NewsItem>, DataError.Network>>

    fun searchNews(query: String): Flow<PagingData<NewsItem>>

}