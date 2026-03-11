package ru.ilnarkin.ilnarapp

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import ru.ilnarkin.ilnarapp.appbars.BottomNavigationBar
import ru.ilnarkin.ilnarapp.appbars.TopBar
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.screens.ArchiveScreen
import ru.ilnarkin.ilnarapp.ui.screens.LoginScreen
import ru.ilnarkin.ilnarapp.ui.screens.NotesScreen
import ru.ilnarkin.ilnarapp.ui.screens.OverlayScreen
import ru.ilnarkin.ilnarapp.ui.screens.PinLockScreen
import ru.ilnarkin.ilnarapp.ui.screens.PinResetScreen
import ru.ilnarkin.ilnarapp.ui.screens.SettingsScreen
import ru.ilnarkin.ilnarapp.ui.screens.TagsScreen
import ru.ilnarkin.ilnarapp.ui.screens.WelcomeScreen
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.ui.theme.IlnarAppTheme


class MainActivity : ComponentActivity() {
	@RequiresApi(Build.VERSION_CODES.O)
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			IlnarAppTheme { Main() }
		}
	}
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Main(){

	val navController = rememberNavController()

	val navBackStackEntry by navController.currentBackStackEntryAsState()

	val currentRoute = navBackStackEntry?.destination?.route

	val borderColor = AppTheme.colors.borderColor

	Column(Modifier.fillMaxSize()
		.statusBarsPadding()
		.navigationBarsPadding()
		.displayCutoutPadding()
		.background(AppTheme.colors.appBgColor)) {

		if (currentRoute != null && !currentRoute.contains("welcome")){
			Row(Modifier.fillMaxWidth()
				.background(Color.White)
				.drawBehind {
					val borderStrokeWidth = 2.dp
					val strokeWidthPx = borderStrokeWidth.toPx()

					drawLine(
						color = borderColor,
						start = Offset(0f, size.height),
						end = Offset(size.width, size.height),
						strokeWidth = strokeWidthPx
					)
				}) {
				TopBar(navController)
			}
		}

		NavHost(
			navController = navController,
			startDestination = NavRoutes.WelcomeScreen.route,
			modifier = Modifier.fillMaxSize().weight(1f)) {
			composable(NavRoutes.NotesScreen.route) { NotesScreen(navController) }
			composable(NavRoutes.TagsScreen.route) { TagsScreen(navController) }
			composable(NavRoutes.ArchiveScreen.route) { ArchiveScreen(navController) }
			composable(NavRoutes.SettingsScreen.route) { SettingsScreen(navController) }

			composable(NavRoutes.WelcomeScreen.route) { WelcomeScreen(navController) }
			composable(NavRoutes.OverlayScreen.route) { OverlayScreen(navController) }

			composable(NavRoutes.LoginScreen.route) { LoginScreen(navController) }

			composable(
				NavRoutes.PinLockScreen.route,
				exitTransition = {
					slideOutOfContainer(
						towards = AnimatedContentTransitionScope.SlideDirection.Left,
						animationSpec = tween(500)
					)
				}) { PinLockScreen(navController) }

			composable(
				NavRoutes.PinResetScreen.route,
				exitTransition = {
					slideOutOfContainer(
						towards = AnimatedContentTransitionScope.SlideDirection.Left,
						animationSpec = tween(500)
					)
				}) { PinResetScreen(navController) }
		}


		if (currentRoute != null && !currentRoute.contains("welcome")){
			BottomNavigationBar(navController)
		}
	}
}