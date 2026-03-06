package ru.ilnarkin.ilnarapp.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext


data class AppColors(
    val appBgColor: Color,
    val primaryColor: Color,
    val borderColor: Color,
    val bottomNavigationColor: Color,
    val dangerColor: Color,
    val warningColor: Color,
    val titleColor: Color,
    val textColor: Color,
    val inputsBorderColor: Color,
    val inputsPlaceholderColor: Color,
    val colorGrey: Color
)


object AppTheme {
    val colors: AppColors
        @Composable
        get() = LocalAppColors.current
}

val LocalAppColors = staticCompositionLocalOf {
    AppColors(
        appBgColor = Color.Unspecified,
        primaryColor = Color.Unspecified,
        borderColor = Color.Unspecified,
        bottomNavigationColor = Color.Unspecified,
        dangerColor = Color.Unspecified,
        warningColor = Color.Unspecified,
        titleColor = Color.Unspecified,
        textColor = Color.Unspecified,
        inputsBorderColor = Color.Unspecified,
        inputsPlaceholderColor = Color.Unspecified,
        colorGrey = Color.Unspecified
    )
}


@Composable
fun IlnarAppTheme(content: @Composable () -> Unit) {

    val colors = AppColors(
        appBgColor = AppBgColor,
        primaryColor = PrimaryColor,
        borderColor = BorderColor,
        bottomNavigationColor = BottomNavigationColor,
        dangerColor = DangerColor,
        warningColor = WarningColor,
        titleColor = TitleColor,
        textColor = TextColor,
        inputsBorderColor = InputsBorderColor,
        inputsPlaceholderColor = InputsPlaceholderColor,
        colorGrey = ColorGrey,
    )

    val materialColors = lightColorScheme(
        primary = colors.primaryColor,
        // перенаправьте нужные цвета
    )

    CompositionLocalProvider(LocalAppColors provides colors) {
        MaterialTheme(colorScheme = materialColors) {
            content()
        }
    }
}