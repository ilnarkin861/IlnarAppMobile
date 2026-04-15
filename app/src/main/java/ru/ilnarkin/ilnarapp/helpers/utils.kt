package ru.ilnarkin.ilnarapp.helpers

import android.util.Patterns
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import ru.ilnarkin.ilnarapp.R


const val API_URL = "http://192.168.1.11:8080"
const val DEFAULT_NOTE_TITLE = "Я хз как назвать"
const val DEFAULT_ERROR_MESSAGE = "Что-то пошло не так. Попробуй еще"
const val NO_INTERNET_ERROR_MESSAGE = "Проверь интернет соединение"
const val SERVER_ERROR_MESSAGE = "Сервер недоступен"
const val PREFS_NAME = "AppSettings"
const val KEY_PIN = "pin"
const val KEY_TOKEN = "token"
const val NOTE_TYPES_ENDPOINT = "notetypes"
const val NOTES_ENDPOINT = "notes"
const val TAGS_ENDPOINT = "tags"
const val ARCHIVES_ENDPOINT = "archives"
const val USER_ENDPOINT = "user"
const val FILES_ENDPOINT = "files"


fun getInterFont(): FontFamily{
	val provider = GoogleFont.Provider(
		providerAuthority = "com.google.android.gms.fonts",
		providerPackage = "com.google.android.gms",
		certificates = R.array.com_google_android_gms_fonts_certs
	)

	return FontFamily(Font(googleFont = GoogleFont("Inter"), provider))
}


fun validEmail(email: String): Boolean{
	return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}


fun getFileName(context: Context, uri: Uri): String {
	var result: String? = null
	if (uri.scheme == "content") {
		val cursor = context.contentResolver.query(uri, null, null, null, null)
		try {
			if (cursor != null && cursor.moveToFirst()) {
				val index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
				if (index != -1) {
					result = cursor.getString(index)
				}
			}
		} finally {
			cursor?.close()
		}
	}

	if (result == null) {
		result = uri.path
		val cut = result?.lastIndexOf('/') ?: -1
		if (cut != -1) {
			result = result?.substring(cut + 1)
		}
	}
	return result ?: "unknown_file"
}
