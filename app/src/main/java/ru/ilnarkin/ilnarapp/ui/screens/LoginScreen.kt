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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.helpers.validEmail
import ru.ilnarkin.ilnarapp.models.UserAuthData
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


@Composable
fun LoginScreen(
	navController: NavController,
	userViewModel: UserViewModel = koinViewModel()
	) {

	val font = getInterFont()

	val scrollState = rememberScrollState()

	val email = remember { mutableStateOf("") }
	var emailIsError by remember { mutableStateOf(false) }
	var emailNotValid by remember { mutableStateOf(false) }

	val password = remember { mutableStateOf("") }
	var passwordIsError by remember { mutableStateOf(false) }

	var loading by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	var showAlert by remember { mutableStateOf(false) }

	val state by userViewModel.uiState.collectAsStateWithLifecycle()


	Column(Modifier.fillMaxSize()
		.verticalScroll(scrollState)
		.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))
		.background(colorResource(R.color.app_bg_color))){

		Row(
			modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
			horizontalArrangement = Arrangement.Center) {
				Image(
					painter = painterResource(R.drawable.ic_lock),
					contentDescription = "Lock",
					alpha = 0.4f)
		}

		Column(Modifier.fillMaxWidth().padding(top = 50.dp)) {
			Row(Modifier.fillMaxWidth()) {
				OutlinedTextField(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 5.dp),
					textStyle = TextStyle(
						fontFamily = font,
						fontSize = 15.sp,
					),
					value = email.value,
					label = { Text("Email") },
					isError = emailIsError || (!email.value.isEmpty() && !validEmail(email.value)),
					onValueChange = {text ->
						email.value = text
						emailIsError = email.value.isEmpty()
					},
					colors = OutlinedTextFieldDefaults.colors(
						unfocusedBorderColor = colorResource(R.color.inputs_border_color),
						focusedBorderColor = colorResource(R.color.primary_color),
						unfocusedLabelColor = colorResource(R.color.inputs_placeholder_color),
						focusedLabelColor = colorResource(R.color.primary_color),
						focusedTextColor = colorResource(R.color.text_color),
						unfocusedTextColor = colorResource(R.color.text_color),
						errorLabelColor = colorResource(R.color.danger_color),
						errorBorderColor = colorResource(R.color.danger_color)
					),
					shape = RoundedCornerShape(10.dp))
			}

			if (emailIsError){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Обязательное поле",
					color = colorResource(R.color.danger_color),
					fontFamily = font,
					fontSize = 13.sp
				)
			}

			if (!email.value.isEmpty() && !validEmail(email.value)){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Некорректный email",
					color = colorResource(R.color.danger_color),
					fontFamily = font,
					fontSize = 13.sp
				)
			}

			Row(Modifier.fillMaxWidth()) {
				OutlinedTextField(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 10.dp, bottom = 5.dp),
					textStyle = TextStyle(
						fontFamily = font,
						fontSize = 15.sp,
					),
					visualTransformation = PasswordVisualTransformation(),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					value = password.value,
					label = { Text("Пароль") },
					isError = passwordIsError,
					onValueChange = {text ->
						password.value = text
						passwordIsError = password.value.isEmpty()
					},
					colors = OutlinedTextFieldDefaults.colors(
						unfocusedBorderColor = colorResource(R.color.inputs_border_color),
						focusedBorderColor = colorResource(R.color.primary_color),
						unfocusedLabelColor = colorResource(R.color.inputs_placeholder_color),
						focusedLabelColor = colorResource(R.color.primary_color),
						focusedTextColor = colorResource(R.color.text_color),
						unfocusedTextColor = colorResource(R.color.text_color),
						errorLabelColor = colorResource(R.color.danger_color),
						errorBorderColor = colorResource(R.color.danger_color)
					),
					shape = RoundedCornerShape(10.dp))
			}

			if (passwordIsError){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Обязательное поле",
					color = colorResource(R.color.danger_color),
					fontFamily = font,
					fontSize = 13.sp
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
						containerColor = colorResource(R.color.primary_color),
						disabledContainerColor = colorResource(R.color.primary_color).copy(alpha = 0.8f)),
					onClick = {
						emailIsError = email.value.isEmpty()
						passwordIsError = password.value.isEmpty()
						emailNotValid = !email.value.isEmpty() && !validEmail(email.value)


						if(!emailIsError && !passwordIsError && !emailNotValid){

							loading = true

							scope.launch {
								async {
									userViewModel.login(UserAuthData(email = email.value, password = password.value))
								}.await()

								loading = false

								if (state.success){
									navController.navigate(NavRoutes.PinResetScreen.route){
										popUpTo(navController.graph.findStartDestination().id) {
											inclusive = true
										}
									}
								}
							}
						}
					}
				) {
					if (loading){
						CircularProgressIndicator(
							modifier = Modifier.size(20.dp),
							strokeWidth = 2.dp,
							color = Color.White,
							trackColor = Color.Transparent,
						)
					}
					else{
						Text(
							text = "Авторизоваться",
							fontFamily = font,
							fontSize = 16.sp,
							fontWeight = FontWeight.SemiBold
						)
					}
				}
			}
		}
	}

	AlertComponent(
		success = false,
		message = "Неверный email или пароль",
		showed = showAlert,
		action = { showAlert = false }
	)
}

