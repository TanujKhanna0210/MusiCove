package com.example.musicove.di

import com.example.musicove.data.repository.MusiCoveRepositoryImpl
import com.example.musicove.domain.repository.MusiCoveRepository
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
    abstract fun bindMusiCoveRepository(
        repository: MusiCoveRepositoryImpl
    ): MusiCoveRepository
}