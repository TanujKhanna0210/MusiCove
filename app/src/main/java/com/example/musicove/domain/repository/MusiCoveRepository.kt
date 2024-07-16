package com.example.musicove.domain.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.musicove.domain.model.AudioMetadata
import kotlinx.coroutines.flow.Flow

interface MusiCoveRepository {

    suspend fun getAudios(): List<AudioMetadata>

    suspend fun loadCoverBitmap(context: Context, uri: Uri): Bitmap?

    suspend fun likeOrUnlikeSong(id: Long)

    fun getFavoriteSongs(): Flow<List<Long>>
}