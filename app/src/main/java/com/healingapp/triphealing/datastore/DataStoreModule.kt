package com.healingapp.triphealing.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

class DataStoreModule(private val context : Context) {
    private val Context.dataStore by
    preferencesDataStore(name = "dataStore")

    private val tokenKey = stringPreferencesKey("TOKEN_KEY")
    private val idKey = stringPreferencesKey("ID")
    private val pwKey = stringPreferencesKey("PW")
    //private val intkey = intPreferencesKey("key_name")

    val text : Flow<String> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map {preferences ->
            preferences[tokenKey] ?: ""
        }

    suspend fun setText(text : String){
        context.dataStore.edit { preferences ->
            preferences[tokenKey] = text
        }
    }

    val id: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[idKey] ?: ""
        }

    val pw: Flow<String> = context.dataStore.data
        .map { preferences ->
            preferences[pwKey] ?: ""
        }

    // 추가: ID와 PW 값을 설정하는 메서드
    suspend fun setId(id: String) {
        context.dataStore.edit { preferences ->
            preferences[idKey] = id
        }
    }

    suspend fun setPw(pw: String) {
        context.dataStore.edit { preferences ->
            preferences[pwKey] = pw
        }
    }
}