package com.yuriishcherbyna.newssho.di

import com.yuriishcherbyna.newssho.data.repository.BookmarksNewsRepositoryImpl
import com.yuriishcherbyna.newssho.data.repository.DataStoreRepositoryImpl
import com.yuriishcherbyna.newssho.data.repository.GoogleAuthRepositoryImpl
import com.yuriishcherbyna.newssho.data.repository.NewsRepositoryImpl
import com.yuriishcherbyna.newssho.domain.repository.AuthRepository
import com.yuriishcherbyna.newssho.domain.repository.BookmarksNewsRepository
import com.yuriishcherbyna.newssho.domain.repository.DataStoreRepository
import com.yuriishcherbyna.newssho.domain.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindDataStoreRepository(
        dataStoreRepositoryImpl: DataStoreRepositoryImpl
    ): DataStoreRepository

    @Binds
    @Singleton
    abstract fun bindAuthRepository(
        googleAuthRepositoryImpl: GoogleAuthRepositoryImpl
    ): AuthRepository

    @Binds
    @Singleton
    abstract fun bindNewsRepository(
        newsRepositoryImpl: NewsRepositoryImpl
    ): NewsRepository

    @Binds
    abstract fun bindsBookmarksNewsRepository(
        bookmarksNewsRepositoryImpl: BookmarksNewsRepositoryImpl
    ): BookmarksNewsRepository

}