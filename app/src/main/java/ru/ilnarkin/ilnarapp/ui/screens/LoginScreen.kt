package ru.ilnarkin.ilnarapp.ui.screens

import android.content.Intent
import android.util.Patterns
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.MainActivity
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent


@Composable
@Preview(showBackground = true, showSystemUi = true, device = PIXEL_3)
fun LoginScreen() {

	val context = LocalContext.current
	val intent = Intent(context, MainActivity::class.java)

	val testEmail = "info@example.com"
	val testPassword = "qwerty1234"

	val font = getInterFont()
	val scrollState = rememberScrollState()
	val email = remember { mutableStateOf("") }
	val password = remember { mutableStateOf("") }
	var emailIsError by remember { mutableStateOf(false) }
	var emailNotValid by remember { mutableStateOf(false) }
	var passwordIsError by remember { mutableStateOf(false) }
	var loading by remember { mutableStateOf(false) }
	val scope = rememberCoroutineScope()

	var showAlert by remember { mutableStateOf(false) }

	Column(Modifier.fillMaxSize()
		.verticalScroll(scrollState)
		.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))
		.background(colorResource(R.color.app_bg_color))){

		Row(
			modifier = Modifier.fillMaxWidth().padding(top = 100.dp),
			horizontalArrangement = Arrangement.Center) {
				Image(
					painter = painterResource(R.drawable.logo_blue),
					contentDescription = "Logo")
		}


		Column(Modifier.fillMaxWidth().padding(top = 80.dp)) {
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
								delay(1500)
							}.invokeOnCompletion {

								if (email.value != testEmail || password.value != testPassword){
									showAlert = true
									loading = false
								}

								else{
									loading = false
									context.startActivity(intent)
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
							text = "Войти",
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



fun validEmail(email: String): Boolean{
	return Patterns.EMAIL_ADDRESS.matcher(email).matches()
}