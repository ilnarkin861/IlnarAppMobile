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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.TopBarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar (navController: NavController)
{
	val topBarViewModel: TopBarViewModel = koinViewModel()
	val uiState = topBarViewModel.uiState
	val title = uiState.title


	TopAppBar(
		modifier = Modifier.padding(bottom = 2.dp),
		colors = TopAppBarDefaults.topAppBarColors(
			containerColor = Color.White,
			navigationIconContentColor = AppTheme.colors.primaryColor,
			titleContentColor = AppTheme.colors.primaryColor
		),
		expandedHeight = AppTheme.dimensions.topBarHeight,
		navigationIcon = {
			if (uiState.showBackButton){
				IconButton(onClick = uiState.onBackClick) {
					Icon(
						modifier = Modifier.size(24.dp),
						painter = painterResource(R.drawable.ic_arrow_back),
						contentDescription = null)
				}
			}
		},
		title = {
			Text(
				text = title,
				style = AppTheme.typography.appBarTitle)
		},
		actions = {
			IconButton(
				modifier = Modifier
					.padding(end = 10.dp)
					.size(30.dp),
				colors = IconButtonDefaults.iconButtonColors(contentColor = AppTheme.colors.primaryColor),
				onClick = {
					navController.navigate(NavRoutes.PinLockScreen.route) {
						popUpTo(0) { inclusive = true }

						launchSingleTop = true
					}
				})
			{
				Icon(
					painter = painterResource(R.drawable.ic_logout),
					contentDescription = "")
			}
		}
	)
}