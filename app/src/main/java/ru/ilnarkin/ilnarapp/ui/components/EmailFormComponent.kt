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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.helpers.validEmail
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@Composable
fun EmailFormComponent(
	email: String,
	action: suspend (email: String) -> Unit,
	close: () -> Unit
) {

	val updatedEmail = rememberSaveable { mutableStateOf(email) }

	var emailIsError by rememberSaveable { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	var saving by rememberSaveable { mutableStateOf(false) }


	Column(modifier = Modifier.background(Color.White)) {
		Column(
			modifier = Modifier.fillMaxWidth()
				.padding(top = 30.dp, start = 15.dp, end = 15.dp, bottom = 40.dp)
		){
			Row(
				modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
			){
				Text(
					color = AppTheme.colors.titleColor,
					text = "Изменить email",
					style = AppTheme.typography.modalTitleText
				)
			}

			Row(
				modifier = Modifier.fillMaxWidth()
			){
				OutlinedTextField(
					modifier = Modifier.fillMaxWidth(),
					textStyle = AppTheme.typography.formInputText,
					value = updatedEmail.value,
					singleLine = true,
					isError = emailIsError || (!updatedEmail.value.isEmpty() && !validEmail(updatedEmail.value)),
					label = { Text("Изменить email") },
					onValueChange = {text ->

						updatedEmail.value = text
						emailIsError = updatedEmail.value.isEmpty() },

					colors = OutlinedTextFieldDefaults.colors(
						unfocusedBorderColor = AppTheme.colors.inputsBorderColor,
						focusedBorderColor = AppTheme.colors.primaryColor,
						unfocusedLabelColor = AppTheme.colors.inputsPlaceholderColor,
						focusedLabelColor = AppTheme.colors.primaryColor,
						focusedTextColor = AppTheme.colors.textColor,
						unfocusedTextColor = AppTheme.colors.textColor
					),
					shape = RoundedCornerShape(10.dp))
			}

			if (emailIsError){
				Row(modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)) {
					Text(
						text = "Обязательное поле",
						style = AppTheme.typography.errorText,
						color = AppTheme.colors.dangerColor
					)
				}
			}

			if (!updatedEmail.value.isEmpty() && !validEmail(updatedEmail.value)){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Некорректный email",
					style = AppTheme.typography.errorText,
					color = AppTheme.colors.dangerColor
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
						emailIsError = updatedEmail.value.isEmpty()

						if(!emailIsError && validEmail(updatedEmail.value)){

							scope.launch {
								saving = true

								try {
									action(updatedEmail.value)
								}finally {
								    saving = false
								}
							}
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