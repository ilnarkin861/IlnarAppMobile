package ru.ilnarkin.ilnarapp.helpers

import androidx.compose.ui.text.googlefonts.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.googlefonts.GoogleFont
import ru.ilnarkin.ilnarapp.R


fun getInterFont(): FontFamily{
	val provider = GoogleFont.Provider(
		providerAuthority = "com.google.android.gms.fonts",
		providerPackage = "com.google.android.gms",
		certificates = R.array.com_google_android_gms_fonts_certs
	)

	return FontFamily(Font(googleFont = GoogleFont("Inter"), provider))
}