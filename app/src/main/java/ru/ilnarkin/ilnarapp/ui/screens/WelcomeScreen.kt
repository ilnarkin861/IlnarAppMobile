package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


@Composable
fun WelcomeScreen(
	navController: NavController,
	userViewModel: UserViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject()
) {

	val snackBarHostState = remember { SnackbarHostState() }

	val state by userViewModel.uiState.collectAsState()


	LaunchedEffect(Unit) {

		userViewModel.checkAuth()

		if (state.isAuth){
			navController.navigate(NavRoutes.PinLockScreen.route){
				popUpTo(navController.graph.findStartDestination().id) {
					inclusive = true
				}
			}
		}

		errorManager.errorEvent.collect { error ->
			when(error){
				NetworkErrorType.NO_INTERNET -> {
					snackBarHostState.showSnackbar("Проверь интернет соединение", duration = SnackbarDuration.Short)
				}

				NetworkErrorType.SERVER_ERROR -> {
					snackBarHostState.showSnackbar("Сервер недоступен", duration = SnackbarDuration.Short)
				}

				NetworkErrorType.UNAUTHORIZED -> {
					navController.navigate(NavRoutes.LoginScreen.route){
						popUpTo(navController.graph.findStartDestination().id) {
							inclusive = true
						}
					}
				}
			}
		}
	}


	Box(modifier = Modifier
		.fillMaxSize()
		.background(colorResource(R.color.primary_color)),
		contentAlignment = Alignment.Center){

		Image(
			painter = painterResource(R.drawable.logo),
			contentDescription = "Logo")

		SnackbarHost(
			hostState = snackBarHostState,
			modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter)
		){data ->
			Snackbar(
				snackbarData = data,
				containerColor = Color.White,
				contentColor = colorResource(R.color.text_color)
			)
		}
	}
}