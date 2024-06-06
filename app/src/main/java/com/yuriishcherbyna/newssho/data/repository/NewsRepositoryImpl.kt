package com.yuriishcherbyna.newssho.data.repository

import com.yuriishcherbyna.newssho.data.remote.NewsApi
import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.data.remote.dto.NewsItemDto
import com.yuriishcherbyna.newssho.domain.repository.NewsRepository
import com.yuriishcherbyna.newssho.domain.util.DataError
import com.yuriishcherbyna.newssho.domain.util.Result
import com.yuriishcherbyna.newssho.domain.util.getCountryFromLocale
import com.yuriishcherbyna.newssho.domain.util.httpCodeToNetworkError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class NewsRepositoryImpl @Inject constructor(
    private val api: NewsApi
): NewsRepository {

    override fun getLatestNews(category: Category): Flow<Result<List<NewsItemDto>, DataError.Network>> {
        return flow {
            try {
                val response = api.getLatestNews(
                    country = getCountryFromLocale(),
                    category = category
                )
                emit(Result.Success(response.articles))
            } catch (e: HttpException) {
                val error = httpCodeToNetworkError(e.code())
                emit(Result.Error(error))
            } catch (e: IOException) {
                emit(Result.Error(DataError.Network.NO_INTERNET_CONNECTION))
            }
        }
    }

    override fun searchNews(query: String): Flow<Result<List<NewsItemDto>, DataError.Network>> {
        return flow {
            try {
                val response = api.searchNews(
                    query = query
                )
                emit(Result.Success(response.articles))
            } catch (e: HttpException) {
                val error = httpCodeToNetworkError(e.code())
                emit(Result.Error(error))
            } catch (e: IOException) {
                emit(Result.Error(DataError.Network.NO_INTERNET_CONNECTION))
            }
        }
    }
}