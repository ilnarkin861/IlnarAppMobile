package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.services.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


@Composable
fun WelcomeScreen(
	navController: NavController,
	userViewModel: UserViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject()
) {

	val snackBarHostState = remember { SnackbarHostState() }

	val state by userViewModel.uiState.collectAsState()


	LaunchedEffect(state.isAuth) {
		if (state.isAuth) {

			val pinCode = userViewModel.getPinCode()

			if (pinCode != null){
				navController.navigate(NavRoutes.PinLockScreen.route) {
					popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
				}
			}

			else{
				navController.navigate(NavRoutes.PinResetScreen.route) {
					popUpTo(navController.graph.findStartDestination().id) { inclusive = true }
				}
			}
		}
	}

	LaunchedEffect(Unit) {

		lateinit var errorJob: Job

		errorJob = launch {
			errorManager.errorEvent.collect { error ->
				when(error) {
					NetworkErrorType.NO_INTERNET, NetworkErrorType.SERVER_ERROR -> {
						val message = if (error == NetworkErrorType.NO_INTERNET) NO_INTERNET_ERROR_MESSAGE else SERVER_ERROR_MESSAGE

						snackBarHostState.showSnackbar(message)
					}

					NetworkErrorType.UNAUTHORIZED -> {

						userViewModel.clearPinCode()

						navController.navigate(NavRoutes.LoginScreen.route) {
							popUpTo(navController.graph.startDestinationId) { inclusive = true }
						}

						errorJob.cancel()
					}
				}
			}
		}

		userViewModel.checkAuth()
	}


	Box(modifier = Modifier
		.fillMaxSize()
		.background(AppTheme.colors.primaryColor),
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
				contentColor = AppTheme.colors.textColor
			)
		}
	}
}