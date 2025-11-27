package ru.ilnarkin.ilnarapp.appbars

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.routes.NavRoutes


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar (navController: NavController) {

	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val route = navBackStackEntry?.destination?.route

	val title = when(route) {
		NavRoutes.NotesScreen.route -> stringResource(R.string.notes_title)
		NavRoutes.TagsScreen.route -> stringResource(R.string.tags_title)
		NavRoutes.ArchiveScreen.route -> stringResource(R.string.archives_title)
		NavRoutes.SearchScreen.route -> stringResource(R.string.search_title)
		NavRoutes.SettingsScreen.route -> stringResource(R.string.settings_title)
		else -> stringResource(R.string.app_name)
	}

	TopAppBar(
		modifier = Modifier.padding(bottom = 2.dp),
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = Color.White,
			navigationIconContentColor = colorResource(R.color.primary_color),
			titleContentColor = colorResource(R.color.primary_color)
		),

		expandedHeight = dimensionResource(R.dimen.top_bar_height),

		title = {
			Text(text = title,
				fontSize = dimensionResource(R.dimen.top_bar_title_font_size).value.sp,
				fontFamily = getInterFont(),
				fontWeight = FontWeight.ExtraBold)
		},

		actions = {
			IconButton(
				colors = IconButtonDefaults.iconButtonColors(
					contentColor = colorResource(R.color.primary_color)
				),
				onClick = {
					if (route != NavRoutes.SettingsScreen.route){
						navController.navigate(NavRoutes.SettingsScreen.route) {
							launchSingleTop = true
							restoreState = false

							popUpTo(NavRoutes.SettingsScreen.route){
								saveState = true
							}
						}
					}
				}) {
				Icon(
					painter = painterResource(R.drawable.ic_settings),
					contentDescription = "")
			}
		}
	)
}