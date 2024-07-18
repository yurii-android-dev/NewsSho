package com.yuriishcherbyna.newssho.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.yuriishcherbyna.newssho.data.paging.SearchNewsPagingSource
import com.yuriishcherbyna.newssho.data.remote.NewsApi
import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.data.remote.mappers.toNewsItem
import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.domain.repository.NewsRepository
import com.yuriishcherbyna.newssho.domain.util.DataError
import com.yuriishcherbyna.newssho.domain.util.Result
import com.yuriishcherbyna.newssho.domain.util.getCountryFromLocale
import com.yuriishcherbyna.newssho.domain.util.httpCodeToNetworkError
import com.yuriishcherbyna.newssho.util.Constants.DEFAULT_PAGE_SIZE
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi
): NewsRepository {

    override fun getLatestNews(category: Category): Flow<Result<List<NewsItem>, DataError.Network>> {
        return flow {
            try {
                val response = api.getLatestNews(
                    country = getCountryFromLocale(),
                    category = category
                ).articles
                    .filter { !it.url.contains("remove") }
                    .map { it.toNewsItem() }
                emit(Result.Success(response))
            } catch (e: HttpException) {
                val error = httpCodeToNetworkError(e.code())
                emit(Result.Error(error))
            } catch (e: IOException) {
                emit(Result.Error(DataError.Network.NO_INTERNET_CONNECTION))
            }
        }
    }

    override fun searchNews(query: String): Flow<PagingData<NewsItem>> {
        return Pager(
            config = PagingConfig(pageSize = DEFAULT_PAGE_SIZE),
            pagingSourceFactory = { SearchNewsPagingSource(api, query) }
        ).flow
    }
}