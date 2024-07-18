package com.yuriishcherbyna.newssho.data.remote

import com.yuriishcherbyna.newssho.BuildConfig
import com.yuriishcherbyna.newssho.data.remote.dto.Category
import com.yuriishcherbyna.newssho.data.remote.dto.NewsDto
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {

    @GET("top-headlines")
    suspend fun getLatestNews(
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
        @Query("country") country: String,
        @Query("category") category: Category? = null
    ): NewsDto

    @GET("everything")
    suspend fun searchNews(
        @Query("apiKey") apiKey: String = BuildConfig.NEWS_API_KEY,
        @Query("q") query: String,
        @Query("page") page: Int
    ): NewsDto

}