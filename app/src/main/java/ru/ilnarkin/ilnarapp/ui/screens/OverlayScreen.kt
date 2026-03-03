package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.routes.NavRoutes


@Composable
fun OverlayScreen(navController: NavController) {

    // Нужен для того, чтобы сразу не показывались appBar'ы

    LaunchedEffect(Unit) {
        delay(600)

        navController.navigate(NavRoutes.NotesScreen.route) {
            popUpTo(NavRoutes.OverlayScreen.route) { inclusive = true }
        }
    }

    Box(Modifier.fillMaxSize().background(colorResource(R.color.app_bg_color)))
}