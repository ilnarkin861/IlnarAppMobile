package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.UserInfo
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.services.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.EmailFormComponent
import ru.ilnarkin.ilnarapp.ui.components.PasswordFormComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
	navController: NavController,
	userViewModel: UserViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject()
	) {

	val testPassword = "qwerty1234"

	var emailFormDialogShowed by remember { mutableStateOf(false) }

	var passwordFormDialogShowed by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	var success by remember { mutableStateOf(true) }

	val alertTitle = remember { mutableStateOf("") }
	var showAlert by remember { mutableStateOf(false) }

	var userInfoLoading by remember { mutableStateOf(false) }

	val state by userViewModel.uiState.collectAsState()

	val snackBarHostState = remember { SnackbarHostState() }


	LaunchedEffect(Unit) {
		errorManager.errorEvent.collect { error ->
			when(error) {
				NetworkErrorType.NO_INTERNET,  NetworkErrorType.SERVER_ERROR ->{
					val message = if (error == NetworkErrorType.NO_INTERNET) NO_INTERNET_ERROR_MESSAGE else  SERVER_ERROR_MESSAGE

					snackBarHostState.showSnackbar(message)
				}

				NetworkErrorType.UNAUTHORIZED -> {
					userViewModel.dismissAlert()

					navController.navigate(NavRoutes.LoginScreen.route) {
						popUpTo(0) { inclusive = true }

						launchSingleTop = true
					}

					return@collect
				}
			}
		}
	}


	Box(Modifier.fillMaxSize().padding(top = 30.dp)){

		Column(Modifier.fillMaxWidth()) {
			Row(Modifier.fillMaxWidth().clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = ripple(),
				onClick = {

					scope.launch {
						userInfoLoading = true

						val userInfo = userViewModel.getUserInfo()

						if (userInfo != null){
							emailFormDialogShowed = true
						}

						userInfoLoading = false
					}
				}
			)) {

				Row(Modifier.fillMaxWidth().padding(horizontal = AppTheme.dimensions.containerHorizontalPadding, vertical = 20.dp),
					horizontalArrangement = Arrangement.SpaceBetween) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							modifier = Modifier.size(25.dp),
							painter = painterResource(R.drawable.ic_mail),
							contentDescription = "Mail",
							tint = AppTheme.colors.colorGrey
						)
						Text(text = "Изменить Email",
							modifier = Modifier.padding(start = 10.dp),
							color = AppTheme.colors.colorGrey,
							style = AppTheme.typography.settingsItemText)
					}

					Row(modifier = Modifier.size(25.dp),
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically) {

						if (userInfoLoading){
							ProgressIndicatorComponent(15, AppTheme.colors.colorGrey.copy(alpha = 0.7f))
						}

						else{
							Icon(
								modifier = Modifier.size(15.dp),
								painter = painterResource(R.drawable.ic_arrow_right),
								contentDescription = "Arrow right",
								tint = AppTheme.colors.colorGrey.copy(alpha = 0.7f)
							)
						}
					}
				}
			}

			HorizontalDivider(
				modifier = Modifier.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding),
				thickness = 1.dp,
				color = AppTheme.colors.borderColor)

			Row(Modifier.fillMaxWidth().clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = ripple(),
				onClick = { passwordFormDialogShowed = true }
			)) {
				Row(Modifier.fillMaxWidth().padding(horizontal = AppTheme.dimensions.containerHorizontalPadding, vertical = 20.dp),
					horizontalArrangement = Arrangement.SpaceBetween,
					verticalAlignment = Alignment.CenterVertically) {
					Row(verticalAlignment = Alignment.CenterVertically) {
						Icon(
							modifier = Modifier.size(25.dp),
							painter = painterResource(R.drawable.ic_password),
							contentDescription = "Password",
							tint = AppTheme.colors.colorGrey
						)
						Text(text = "Сменить пароль",
							modifier = Modifier.padding(start = 10.dp),
							color = AppTheme.colors.colorGrey,
							style = AppTheme.typography.settingsItemText)
					}

					Row(modifier = Modifier.size(25.dp),
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically) {
						Icon(
							modifier = Modifier.size(15.dp),
							painter = painterResource(R.drawable.ic_arrow_right),
							contentDescription = "Arrow right",
							tint = AppTheme.colors.colorGrey.copy(alpha = 0.7f)
						)
					}
				}
			}

		}

		SnackbarHost(
			hostState = snackBarHostState,
			modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter)
		){data ->
			Snackbar(
				snackbarData = data,
				containerColor = AppTheme.colors.primaryColor,
				contentColor = Color.White
			)
		}

	}


	AlertComponent(
		success = state.success,
		message = state.message,
		showed = state.showAlert,
		action = { userViewModel.dismissAlert()	}
	)


	// Email change form
	if (emailFormDialogShowed){
		BasicAlertDialog(
			onDismissRequest = {},
			properties = DialogProperties(
				dismissOnBackPress = false,
				dismissOnClickOutside = false
			)
		) {
			Surface(
				shape = MaterialTheme.shapes.small,
				tonalElevation = AlertDialogDefaults.TonalElevation
			) {
				EmailFormComponent(
					email = state.data!!.email,

					action = {email ->

						val result = userViewModel.changeEmail(UserInfo(email = email))

						if (result != null){
							emailFormDialogShowed = false
						}
					},

					close = { emailFormDialogShowed = false }
				)
			}
		}
	}


	// Password change form

	if (passwordFormDialogShowed){
		BasicAlertDialog(
			onDismissRequest = {},
			properties = DialogProperties(
				dismissOnBackPress = false,
				dismissOnClickOutside = false
			)
		) {
			Surface(
				shape = MaterialTheme.shapes.small,
				tonalElevation = AlertDialogDefaults.TonalElevation
			) {
				PasswordFormComponent(
					action = { passwordModel ->

						delay(2000)

						if (passwordModel.oldPassword != testPassword){
							success = false
							alertTitle.value = "Неверный старый пароль"
							showAlert = true
						}

						else{
							passwordFormDialogShowed = false
						}
					},

					close = { passwordFormDialogShowed = false }
				)
			}
		}
	}
}