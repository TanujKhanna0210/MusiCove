package com.example.musicove.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.net.Uri
import com.example.musicove.domain.model.AudioMetadata
import com.example.musicove.domain.repository.MusiCoveRepository
import com.example.musicove.util.audio.MetadataHelper
import com.example.musicove.util.audio.UserPreferences
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MusiCoveRepositoryImpl @Inject constructor(
    private val metadataHelper: MetadataHelper,
    private val userPreferences: UserPreferences
) : MusiCoveRepository {
    override suspend fun getAudios(): List<AudioMetadata> {
        return withContext(Dispatchers.IO) {
            metadataHelper.getAudios()
        }
    }

    override suspend fun loadCoverBitmap(context: Context, uri: Uri): Bitmap? {
        return withContext(Dispatchers.IO) {
            metadataHelper.loadCoverBitmap(
                context = context,
                uri = uri
            )
        }
    }

    override suspend fun likeOrUnlikeSong(id: Long) {
        withContext(Dispatchers.IO) {
            userPreferences.likeOrUnlikeSong(id = id)
        }
    }

    override fun getFavoriteSongs(): Flow<List<Long>> {
        return userPreferences.favoriteSongs
    }
}