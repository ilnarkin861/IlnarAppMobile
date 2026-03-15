package ru.ilnarkin.ilnarapp.appbars

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@Composable
fun BottomNavigationBar(navController: NavController)
{

	val navBackStackEntry by navController.currentBackStackEntryAsState()
	val currentRoute = navBackStackEntry?.destination?.route
	val borderColor = AppTheme.colors.borderColor

	val colors = NavigationBarItemDefaults.colors(
		selectedIconColor = AppTheme.colors.primaryColor,
		unselectedIconColor = AppTheme.colors.bottomNavigationColor,
		selectedTextColor = AppTheme.colors.primaryColor,
		unselectedTextColor = AppTheme.colors.bottomNavigationColor,
		indicatorColor = Color.Transparent)


	NavigationBar(
		modifier = Modifier.drawBehind {
				val borderStrokeWidth = 2.dp
				val strokeWidthPx = borderStrokeWidth.toPx()
				drawLine(
					color = borderColor,
					start = Offset(x = 0f, y = 0f),
					end = Offset(x = size.width, y = 0f),
					strokeWidth = strokeWidthPx
				)
		},
		containerColor = Color.White)
	{
		NavigationBarItem(
			selected = currentRoute == NavRoutes.NotesScreen.route,
			colors = colors,
			icon = {
				Icon(painter = painterResource(R.drawable.ic_notes), contentDescription = "")
			},
			label = {
				Text(text = stringResource(R.string.notes_title), style = AppTheme.typography.navBarItemTitle)
			},
			onClick = {
				if (currentRoute != NavRoutes.NotesScreen.route){
					navController.navigate(NavRoutes.NotesScreen.route) {
						launchSingleTop = true
						restoreState = false

						currentRoute?.let {
							popUpTo(it){
								saveState = true
							}
						}
					}
				}
			}
		)
		NavigationBarItem(
			selected = currentRoute == NavRoutes.TagsScreen.route,
			colors = colors,
			icon = {
				Icon(painter = painterResource(R.drawable.ic_hashtag), contentDescription = "")
			},
			label = {
				Text(text = stringResource(R.string.tags_title),	style = AppTheme.typography.navBarItemTitle)
			},
			onClick = {
				if (currentRoute != NavRoutes.TagsScreen.route){
					navController.navigate(NavRoutes.TagsScreen.route) {
						launchSingleTop = true
						restoreState = false

						currentRoute?.let {
							popUpTo(it){
								saveState = true
							}
						}
					}
				}
			}
		)
		NavigationBarItem(
			selected = currentRoute == NavRoutes.ArchiveScreen.route,
			colors = colors,
			icon = {
				Icon(painter = painterResource(R.drawable.ic_archive), contentDescription = "")
			},
			label = {
				Text(text = stringResource(R.string.archives_title), style = AppTheme.typography.navBarItemTitle)
			},
			onClick = {
				if (currentRoute != NavRoutes.ArchiveScreen.route){
					navController.navigate(NavRoutes.ArchiveScreen.route) {
						launchSingleTop = true
						restoreState = false

						currentRoute?.let {
							popUpTo(it){
								saveState = true
							}
						}
					}
				}
			}
		)
		NavigationBarItem(
			selected = currentRoute == NavRoutes.SettingsScreen.route,
			colors = colors,
			icon = {
				Icon(painter = painterResource(R.drawable.ic_user_settings), contentDescription = "")
			},
			label = {
				Text(text = stringResource(R.string.settings_title),style = AppTheme.typography.navBarItemTitle)
			},
			onClick = {
				if (currentRoute != NavRoutes.SettingsScreen.route){
					navController.navigate(NavRoutes.SettingsScreen.route) {
						launchSingleTop = true
						restoreState = false

						currentRoute?.let {
							popUpTo(it){
								saveState = true
							}
						}
					}
				}
			}
		)
	}
}