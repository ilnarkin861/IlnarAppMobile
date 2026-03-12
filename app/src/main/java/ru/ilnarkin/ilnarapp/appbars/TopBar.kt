package ru.ilnarkin.ilnarapp.appbars

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar (navController: NavController) {

	val navBackStackEntry by navController.currentBackStackEntryAsState()

	val route = navBackStackEntry?.destination?.route

	val title = when(route) {
		NavRoutes.NotesScreen.route -> stringResource(R.string.notes_title)
		NavRoutes.TagsScreen.route -> stringResource(R.string.tags_title)
		NavRoutes.ArchiveScreen.route -> stringResource(R.string.archives_title)
		NavRoutes.SettingsScreen.route -> stringResource(R.string.settings_title)
		else -> ""
	}


	TopAppBar(
		modifier = Modifier.padding(bottom = 2.dp),
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = Color.White,
			navigationIconContentColor = AppTheme.colors.primaryColor,
			titleContentColor = AppTheme.colors.primaryColor
		),

		expandedHeight = AppTheme.dimensions.topBarHeight,

		title = {
			Text(text = title, style = AppTheme.typography.appBarTitle)
		},

		actions = {
			IconButton(
				modifier = Modifier.padding(end = 10.dp).size(30.dp),
				colors = IconButtonDefaults.iconButtonColors(
					contentColor = AppTheme.colors.primaryColor
				),
				onClick = {
					navController.navigate(NavRoutes.PinLockScreen.route) {
						popUpTo(0) { inclusive = true }

						launchSingleTop = true
					}
				}
			) {
				Icon(
					painter = painterResource(R.drawable.ic_logout),
					contentDescription = ""
				)
			}
		}
	)
}