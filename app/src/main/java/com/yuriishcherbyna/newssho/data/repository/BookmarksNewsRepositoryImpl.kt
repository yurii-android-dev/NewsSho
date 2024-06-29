package com.yuriishcherbyna.newssho.data.repository

import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.firestore
import com.yuriishcherbyna.newssho.domain.model.NewsItem
import com.yuriishcherbyna.newssho.domain.repository.BookmarksNewsRepository
import com.yuriishcherbyna.newssho.domain.util.DataError
import com.yuriishcherbyna.newssho.domain.util.Result
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val TAG = "BookmarksNewsRepositoryImpl"

class BookmarksNewsRepositoryImpl @Inject constructor(
    private val auth: FirebaseAuth
): BookmarksNewsRepository {

    private val db = Firebase.firestore

    private val userId = auth.currentUser?.uid

    override fun saveNews(newsItem: NewsItem): Result<Unit, DataError.Network> {
        return try {
            userId?.let { id ->
                db.collection("users").document(id).collection("bookmarks")
                    .add(newsItem)
            }
            Log.d(TAG, "Saving news item: ${newsItem.title}")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.d(TAG, "Error saving news item: ${e.message}")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun getAllNews(): Result<List<NewsItem>, DataError.Network> {
        return try {
            userId?.let { id ->
                val response = db.collection("users").document(id)
                    .collection("bookmarks").get().await()
                val news = response.map { document ->
                    NewsItem(
                        id = document.id,
                        title = document.getString("title").orEmpty(),
                        url = document.getString("url").orEmpty(),
                        urlToImage = document.getString("urlToImage"),
                        publishedAt = document.getString("publishedAt").orEmpty(),
                        sourceName = document.getString("sourceName").orEmpty()
                    )
                }
                Log.d(TAG, "Successfully fetching bookmarks")
                Result.Success(news)
            } ?: Result.Success(emptyList())
        } catch (e: Exception) {
            Log.d(TAG, "Error fetching bookmarks: ${e.message}")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun deleteNews(savedNewsId: String): Result<Unit, DataError.Network> {
        return try {
            userId?.let { id ->
                db.collection("users").document(id)
                    .collection("bookmarks").document(savedNewsId).delete().await()
            }
            Log.d(TAG, "Deleting news item with id: $savedNewsId")
            Result.Success(Unit)
        } catch (e: Exception) {
            Log.d(TAG, "Error deleting news item: ${e.message}")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }

    override suspend fun toogleBookmark(newsItem: NewsItem): Result<Unit, DataError.Network> {
        return try {
            when (val news = getAllNews()) {
                is Result.Error -> {
                    Log.d(TAG, "Error fetching bookmarks")
                    Result.Error(news.error)
                }
                is Result.Success -> {
                    val savedNews = news.data
                    val isSavedNews = savedNews.find { it.url == newsItem.url }
                    if (isSavedNews == null) {
                        saveNews(newsItem)
                    } else {
                        isSavedNews.id?.let { deleteNews(it) }
                    }
                    Result.Success(Unit)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, "Error toggling bookmark: ${e.message}")
            Result.Error(DataError.Network.UNKNOWN)
        }
    }
}