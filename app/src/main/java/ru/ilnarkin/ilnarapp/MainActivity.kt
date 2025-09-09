package ru.ilnarkin.ilnarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ilnarkin.ilnarapp.appbars.BottomNavigationBar
import ru.ilnarkin.ilnarapp.appbars.TopBar
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.screens.ArchiveScreen
import ru.ilnarkin.ilnarapp.ui.screens.NotesScreen
import ru.ilnarkin.ilnarapp.ui.screens.SearchScreen
import ru.ilnarkin.ilnarapp.ui.screens.SettingsScreen
import ru.ilnarkin.ilnarapp.ui.screens.TagsScreen


class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			Main()
		}
	}
}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Main(){

	val navController = rememberNavController()
	val borderColor = colorResource(R.color.border_color)

	Column(Modifier.displayCutoutPadding()) {
		Row(Modifier.fillMaxWidth()
			.background(Color.White)

			.drawBehind {
				var borderStrokeWidth = 2.dp
				val strokeWidthPx = borderStrokeWidth.toPx()

				drawLine(
					color = borderColor,
					start = Offset(0f, size.height),
					end = Offset(size.width, size.height),
					strokeWidth = strokeWidthPx
				)
			}) {
			Row(Modifier.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))) { TopBar(navController) }

		}

		NavHost(
			navController = navController,
			startDestination = NavRoutes.NotesScreen.route,
			modifier = Modifier.fillMaxSize()
				.weight(1f)
				.background(colorResource(R.color.app_bg_color))) {
			composable(NavRoutes.NotesScreen.route) { NotesScreen() }
			composable(NavRoutes.TagsScreen.route) { TagsScreen() }
			composable(NavRoutes.ArchiveScreen.route) { ArchiveScreen() }
			composable(NavRoutes.SearchScreen.route) { SearchScreen() }
			composable(NavRoutes.SettingsScreen.route) { SettingsScreen() }
		}
		BottomNavigationBar(navController)
	}
}