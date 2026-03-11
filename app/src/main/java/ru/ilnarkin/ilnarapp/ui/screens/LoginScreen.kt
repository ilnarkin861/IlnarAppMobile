package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.validEmail
import ru.ilnarkin.ilnarapp.models.UserLoginData
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.services.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


@Composable
fun LoginScreen(
	navController: NavController,
	userViewModel: UserViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject()
	) {

	val scrollState = rememberScrollState()

	val email = remember { mutableStateOf("") }
	var emailIsError by remember { mutableStateOf(false) }
	var emailNotValid by remember { mutableStateOf(false) }

	val password = remember { mutableStateOf("") }
	var passwordIsError by remember { mutableStateOf(false) }

	var loading by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	val message = remember { mutableStateOf("") }
	var showMessage by remember { mutableStateOf(false) }

	val state by userViewModel.uiState.collectAsStateWithLifecycle()

	val inputColor = OutlinedTextFieldDefaults.colors(
		unfocusedBorderColor = AppTheme.colors.inputsBorderColor,
		focusedBorderColor = AppTheme.colors.primaryColor,
		unfocusedLabelColor = AppTheme.colors.inputsPlaceholderColor,
		focusedLabelColor = AppTheme.colors.primaryColor,
		focusedTextColor = AppTheme.colors.textColor,
		unfocusedTextColor = AppTheme.colors.textColor,
		errorLabelColor = AppTheme.colors.dangerColor,
		errorBorderColor = AppTheme.colors.dangerColor
	)


	if (!state.success){
		LaunchedEffect(Unit) {
			errorManager.errorEvent.collect { error ->

				when(error){
					NetworkErrorType.NO_INTERNET, NetworkErrorType.SERVER_ERROR -> {
						showMessage = true
						message.value = if(error == NetworkErrorType.NO_INTERNET) NO_INTERNET_ERROR_MESSAGE else SERVER_ERROR_MESSAGE
					}

					NetworkErrorType.UNAUTHORIZED -> { }
				}
			}
		}
	}


	Column(Modifier.fillMaxSize()
		.verticalScroll(scrollState)
		.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))
		.background(AppTheme.colors.appBgColor)){

		Row(
			modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
			horizontalArrangement = Arrangement.Center) {
				Image(
					painter = painterResource(R.drawable.ic_lock),
					contentDescription = "Lock",
					alpha = 0.4f)
		}

		if (showMessage){
			Row(modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 2.dp),
				horizontalArrangement = Arrangement.Center) {
				Text(message.value,
					color = AppTheme.colors.dangerColor,
					style = AppTheme.typography.authMessageText)
			}
		}

		Column(Modifier.fillMaxWidth().padding(top = 50.dp)) {
			Row(Modifier.fillMaxWidth()) {
				OutlinedTextField(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 5.dp),
					textStyle = AppTheme.typography.formInputText,
					value = email.value,
					label = { Text("Email") },
					isError = emailIsError || (!email.value.isEmpty() && !validEmail(email.value)),
					onValueChange = {text ->
						email.value = text
						emailIsError = email.value.isEmpty()
					},
					colors = inputColor,
					shape = RoundedCornerShape(10.dp))
			}

			if (emailIsError){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Обязательное поле",
					color = AppTheme.colors.dangerColor,
					style = AppTheme.typography.errorText
				)
			}

			if (!email.value.isEmpty() && !validEmail(email.value)){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Некорректный email",
					color = AppTheme.colors.dangerColor,
					style = AppTheme.typography.errorText
				)
			}

			Row(Modifier.fillMaxWidth()) {
				OutlinedTextField(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 10.dp, bottom = 5.dp),
					textStyle = AppTheme.typography.formInputText,
					visualTransformation = PasswordVisualTransformation(),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					value = password.value,
					label = { Text("Пароль") },
					isError = passwordIsError,
					onValueChange = {text ->
						password.value = text
						passwordIsError = password.value.isEmpty()
					},
					colors = inputColor,
					shape = RoundedCornerShape(10.dp))
			}

			if (passwordIsError){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Обязательное поле",
					color = AppTheme.colors.dangerColor,
					style = AppTheme.typography.errorText
				)
			}

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 40.dp, bottom = 80.dp)
			) {
				Button(
					modifier = Modifier
						.fillMaxWidth()
						.height(60.dp),
					enabled = !loading,
					shape = RoundedCornerShape(10.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = AppTheme.colors.primaryColor,
						disabledContainerColor = AppTheme.colors.primaryColor.copy(alpha = 0.8f)),
					onClick = {
						emailIsError = email.value.isEmpty()
						passwordIsError = password.value.isEmpty()
						emailNotValid = !email.value.isEmpty() && !validEmail(email.value)

						if(!emailIsError && !passwordIsError && !emailNotValid){
							scope.launch {
								loading = true

								val result = userViewModel.login(UserLoginData(email = email.value, password = password.value))

								if (result != null){
									navController.navigate(NavRoutes.PinResetScreen.route){
										popUpTo(NavRoutes.LoginScreen.route) {
											inclusive = true
										}
									}
								}

								else{
									message.value = userViewModel.uiState.value.message
									showMessage = true
								}

								loading = false
							}
						}
					}
				) {
					if (loading){
						CircularProgressIndicator(
							modifier = Modifier.size(20.dp),
							strokeWidth = 2.dp,
							color = Color.White,
							trackColor = Color.Transparent
						)
					}
					else{
						Text(
							text = "Авторизоваться",
							style = AppTheme.typography.inputButtonText
						)
					}
				}
			}
		}
	}
}

