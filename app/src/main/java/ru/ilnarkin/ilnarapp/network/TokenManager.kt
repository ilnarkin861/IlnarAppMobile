package ru.ilnarkin.ilnarapp.network

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map


val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session_prefs")


class TokenManager(private val context: Context) {
	private val dataStore = context.dataStore
	private val AUTH_TOKEN_KEY = stringPreferencesKey("auth_token_key")


	suspend fun saveAuthToken(token: String?){
		dataStore.edit { preferences ->
			preferences[AUTH_TOKEN_KEY] = token ?: ""
		}
	}


	suspend fun getAuthToken() : String? {
		return dataStore.data.map { preferences ->
			preferences[AUTH_TOKEN_KEY]
		}.first()
	}


	suspend fun clearAuthToken(){
		dataStore.edit { preferences ->
			preferences.clear()
		}
	}
}