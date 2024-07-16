package com.example.musicove.util.audio

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class UserPreferences @Inject constructor(
    @ApplicationContext val context: Context
) {

    companion object {
        private const val MUSI_COVE_PREFERENCES = "musiCovePlayer"
        private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
            MUSI_COVE_PREFERENCES
        )
        private val FAVORITE_SONGS_KEY = stringPreferencesKey("FAVORITE_SONGS")
    }

    private val gson = Gson()

    private val _favoriteSongs: Flow<String?>
        get() = context.dataStore.data.map { preferences ->
            preferences[FAVORITE_SONGS_KEY]
        }

    val favoriteSongs: Flow<List<Long>> = flow {
        _favoriteSongs.collect { json ->
            emit(value = fromStringToListLong(json ?: "[]"))
        }
    }

    suspend fun likeOrUnlikeSong(id: Long) {
        context.dataStore.edit { preferences ->
            val songs = favoriteSongs.first().toMutableList()
            if (songs.contains(id)) {
                songs.remove(id)
            } else {
                songs.add(id)
            }

            preferences[FAVORITE_SONGS_KEY] = fromListLongToString(listLong = songs)
        }
    }

    private fun fromStringToListLong(string: String): List<Long> {
        return try {
            gson.fromJson(
                string,
                object : TypeToken<List<Long>>() {}.type
            )
        } catch (exp: JsonSyntaxException) {
            emptyList()
        }
    }

    private fun fromListLongToString(listLong: List<Long>): String {
        return try {
            gson.toJson(
                listLong,
                object : TypeToken<List<Long>>() {}.type
            )
        } catch (exp: JsonSyntaxException) {
            "[]"
        }
    }

}