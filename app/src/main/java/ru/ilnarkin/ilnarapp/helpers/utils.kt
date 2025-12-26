package ru.ilnarkin.ilnarapp.helpers

import android.util.Patterns
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.googlefonts.GoogleFont
import ru.ilnarkin.ilnarapp.R


const val PREFS_NAME = "AppSettings"
const val KEY_PIN = "pin"
const val KEY_TOKEN = "token"
const val API_URL = "http://192.168.1.11:8080"


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