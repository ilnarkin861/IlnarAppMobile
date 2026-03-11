package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


data class PasswordModel(val oldPassword: String, val newPassword: String)


@Composable
fun PasswordFormComponent(
	action: suspend (passwordModel: PasswordModel) -> Unit,
	close: () -> Unit
) {
	val passwordLength = 8

	val oldPassword = remember { mutableStateOf("") }
	val newPassword = remember { mutableStateOf("") }
	val confirmPassword = remember { mutableStateOf("") }

	var passwordLengthError by remember { mutableStateOf(false) }
	var passwordsMatch by remember { mutableStateOf(false) }

	var oldPasswordIsError by remember { mutableStateOf(false) }
	var newPasswordIsError by remember { mutableStateOf(false) }
	var confirmPasswordIsError by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	var saving by remember { mutableStateOf(false) }

	val inputColors = OutlinedTextFieldDefaults.colors(
		unfocusedBorderColor = AppTheme.colors.inputsBorderColor,
		focusedBorderColor = AppTheme.colors.primaryColor,
		unfocusedLabelColor = AppTheme.colors.inputsPlaceholderColor,
		focusedLabelColor = AppTheme.colors.primaryColor,
		focusedTextColor = AppTheme.colors.textColor,
		unfocusedTextColor = AppTheme.colors.textColor
	)


	Column(modifier = Modifier.background(Color.White)) {
		Column(
			modifier = Modifier.fillMaxWidth()
				.padding(top = 30.dp, start = 15.dp, end = 15.dp, bottom = 40.dp)
		){

			Row(
				modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
			){
				Text(
					text = "Изменить пароль",
					color = AppTheme.colors.titleColor,
					style = AppTheme.typography.modalTitleText
				)
			}

			// Old password field
			Row(
				modifier = Modifier.fillMaxWidth()
			){
				OutlinedTextField(
					modifier = Modifier.fillMaxWidth(),
					textStyle = AppTheme.typography.formInputText,
					visualTransformation = PasswordVisualTransformation(),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					value = oldPassword.value,
					singleLine = true,
					isError = oldPasswordIsError,
					label = { Text("Старый пароль") },
					onValueChange = {text ->

						oldPassword.value = text
						oldPasswordIsError = oldPassword.value.isEmpty() },

					colors = inputColors,
					shape = RoundedCornerShape(10.dp))
			}


			if (oldPasswordIsError){
				Row(modifier = Modifier.padding(top = 5.dp)) {
					Text(
						text = "Обязательное поле",
						color = AppTheme.colors.dangerColor,
						style = AppTheme.typography.errorText
					)
				}
			}

			// New password field
			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
			){
				OutlinedTextField(
					modifier = Modifier.fillMaxWidth(),
					textStyle = AppTheme.typography.formInputText,
					visualTransformation = PasswordVisualTransformation(),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					value = newPassword.value,
					singleLine = true,
					isError = newPasswordIsError,
					label = { Text("Новый пароль") },
					onValueChange = {text ->
						newPassword.value = text
						newPasswordIsError = newPassword.value.isEmpty()
						passwordLengthError = newPassword.value.length < passwordLength },

					colors = inputColors,
					shape = RoundedCornerShape(10.dp))
			}

			if (newPasswordIsError){
				Row(modifier = Modifier.padding(top = 5.dp)) {
					Text(
						text = "Обязательное поле",
						color = AppTheme.colors.dangerColor,
						style = AppTheme.typography.errorText
					)
				}
			}

			if (!newPassword.value.isEmpty() && passwordLengthError){
				Text(
					modifier = Modifier.padding(top = 5.dp),
					text = "Длина пароля не должна быть меньше $passwordLength символов",
					color = AppTheme.colors.dangerColor,
					style = AppTheme.typography.errorText
				)
			}

			// Confirm password field
			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 20.dp)
			){
				OutlinedTextField(
					modifier = Modifier.fillMaxWidth(),
					textStyle = AppTheme.typography.formInputText,
					visualTransformation = PasswordVisualTransformation(),
					keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
					value = confirmPassword.value,
					singleLine = true,
					isError = confirmPasswordIsError || !confirmPassword.value.isEmpty() && !passwordsMatch,
					label = { Text("Подтверди пароль") },
					onValueChange = {text ->

						confirmPassword.value = text
						confirmPasswordIsError = confirmPassword.value.isEmpty()
						passwordsMatch = newPassword.value == confirmPassword.value
					},
					colors = inputColors,
					shape = RoundedCornerShape(10.dp))
			}

			if (confirmPasswordIsError){
				Row(modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)) {
					Text(
						text = "Обязательное поле",
						color = AppTheme.colors.dangerColor,
						style = AppTheme.typography.errorText
					)
				}
			}

			if (!confirmPassword.value.isEmpty() && !passwordsMatch){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Пароли не совпадают",
					color = AppTheme.colors.dangerColor,
					style = AppTheme.typography.errorText
				)
			}

			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 30.dp)
			){
				Button(
					modifier = Modifier.fillMaxWidth().height(60.dp),
					enabled = !saving,
					shape = RoundedCornerShape(10.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = AppTheme.colors.primaryColor,
						disabledContainerColor = AppTheme.colors.primaryColor.copy(alpha = 0.8f)),
					onClick = {
						oldPasswordIsError = oldPassword.value.isEmpty()
						newPasswordIsError = newPassword.value.isEmpty()
						confirmPasswordIsError = confirmPassword.value.isEmpty()
						passwordLengthError = newPassword.value.length < passwordLength
						passwordsMatch = !confirmPassword.value.isEmpty() && (newPassword.value == confirmPassword.value)

						val formIsValid = !oldPasswordIsError && !newPasswordIsError && !confirmPasswordIsError
								&& !passwordLengthError && passwordsMatch

						if(formIsValid){

							val passwordModel = PasswordModel(oldPassword = oldPassword.value, newPassword = newPassword.value)
							saving = true

							scope.launch {
								scope.async {
									action(passwordModel) }.await()
							}.invokeOnCompletion { saving = false }
						}
					}
				) {

					if (saving){
						Row(modifier = Modifier.fillMaxSize(),
							horizontalArrangement = Arrangement.Center,
							verticalAlignment = Alignment.CenterVertically) {
							CircularProgressIndicator(
								modifier = Modifier.size(20.dp),
								strokeWidth = 2.dp,
								color = Color.White
							)
						}
					}

					else{
						Text(
							text = "Изменить",
							style = AppTheme.typography.inputButtonText
						)
					}
				}
			}

			if (!saving){
				Row(
					modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
					horizontalArrangement = Arrangement.Center
				) {
					Text(
						modifier = Modifier.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = {	close()	}
						),
						text = "Закрыть",
						color = AppTheme.colors.colorGrey,
						style = AppTheme.typography.textButton
					)
				}
			}
		}
	}
}