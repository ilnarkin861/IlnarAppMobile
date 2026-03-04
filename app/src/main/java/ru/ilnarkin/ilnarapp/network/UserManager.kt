package ru.ilnarkin.ilnarapp.network

import android.content.SharedPreferences
import androidx.core.content.edit
import ru.ilnarkin.ilnarapp.helpers.KEY_PIN
import ru.ilnarkin.ilnarapp.helpers.KEY_TOKEN


class UserManager(private val prefs: SharedPreferences) {

	fun saveAuthToken(token: String?){
		prefs.edit(commit = true) { putString(KEY_TOKEN, token) }
	}


	fun getAuthToken() : String? {
		return prefs.getString(KEY_TOKEN, null)
	}


	fun clearAuthToken(){
		prefs.edit { remove(KEY_TOKEN) }
	}


	fun setPinCode(pinCode: String){
		prefs.edit(commit = true) {putString(KEY_PIN, pinCode)}
	}


	fun getPinCode(): String?{
		return prefs.getString(KEY_PIN, null)
	}


	fun clearPinCode(){
		prefs.edit { remove(KEY_PIN) }
	}
}