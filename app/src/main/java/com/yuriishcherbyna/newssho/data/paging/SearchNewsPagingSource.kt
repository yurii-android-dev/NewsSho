package com.yuriishcherbyna.newssho.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.yuriishcherbyna.newssho.data.remote.NewsApi
import com.yuriishcherbyna.newssho.data.remote.mappers.toNewsItem
import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.util.Constants.DEFAULT_CURRENT_PAGE
import retrofit2.HttpException
import java.io.IOException

class SearchNewsPagingSource(
    private val api: NewsApi,
    private val query: String
): PagingSource<Int, NewsItem>(){

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsItem> {
        return try {
            val currentPage = params.key ?: DEFAULT_CURRENT_PAGE
            val searchNews = api.searchNews(
                query = query,
                page = currentPage
            ).articles
                .filter { !it.url.contains("remove") }
                .map { it.toNewsItem() }
            val endOfPaginationReached = searchNews.isEmpty()

            LoadResult.Page(
                data = searchNews,
                prevKey = if (currentPage == 1) null else currentPage - 1,
                nextKey = if (endOfPaginationReached) null else currentPage + 1
            )
        } catch (e: IOException) {
            LoadResult.Error(e)
        } catch (e: HttpException) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, NewsItem>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }

}