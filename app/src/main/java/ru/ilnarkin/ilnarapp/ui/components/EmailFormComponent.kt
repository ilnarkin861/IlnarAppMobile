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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.helpers.validEmail


@Composable
fun EmailFormComponent(
	email: String,
	action: suspend (email: String) -> Unit,
	close: () -> Unit
) {

	val font = getInterFont()

	val mutableEmail = remember { mutableStateOf(email) }

	var emailIsError by remember { mutableStateOf(false) }

	val interactionSource = remember { MutableInteractionSource() }

	val scope = rememberCoroutineScope()

	var saving by remember { mutableStateOf(false) }



	Column(modifier = Modifier.background(Color.White)) {
		Column(
			modifier = Modifier.fillMaxWidth()
				.padding(top = 30.dp, start = 15.dp, end = 15.dp, bottom = 40.dp)
		){
			Row(
				modifier = Modifier.fillMaxWidth().padding(bottom = 10.dp)
			){
				Text(
					color = colorResource(R.color.title_color),
					text = "Изменить email",
					fontFamily = font,
					fontSize = 18.sp,
					fontWeight = FontWeight.Bold
				)
			}

			Row(
				modifier = Modifier.fillMaxWidth()
			){
				OutlinedTextField(
					modifier = Modifier.fillMaxWidth(),
					textStyle = TextStyle(
						fontFamily = font,
						fontSize = 15.sp,
					),
					value = mutableEmail.value,
					singleLine = true,
					isError = emailIsError || (!mutableEmail.value.isEmpty() && !validEmail(mutableEmail.value)),
					label = { Text("Изменить email") },
					onValueChange = {text ->

						mutableEmail.value = text
						emailIsError = mutableEmail.value.isEmpty() },

					colors = OutlinedTextFieldDefaults.colors(
						unfocusedBorderColor = colorResource(R.color.inputs_border_color),
						focusedBorderColor = colorResource(R.color.primary_color),
						unfocusedLabelColor = colorResource(R.color.inputs_placeholder_color),
						focusedLabelColor = colorResource(R.color.primary_color),
						focusedTextColor = colorResource(R.color.text_color),
						unfocusedTextColor = colorResource(R.color.text_color)
					),
					shape = RoundedCornerShape(10.dp))
			}

			if (emailIsError){
				Row(modifier = Modifier.padding(top = 5.dp, bottom = 10.dp)) {
					Text(
						text = "Обязательное поле",
						color = colorResource(R.color.danger_color),
						fontFamily = font,
						fontSize = 13.sp
					)
				}
			}

			if (!mutableEmail.value.isEmpty() && !validEmail(mutableEmail.value)){
				Text(
					modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
					text = "Некорректный email",
					color = colorResource(R.color.danger_color),
					fontFamily = font,
					fontSize = 13.sp
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
						containerColor = colorResource(R.color.primary_color),
						disabledContainerColor = colorResource(R.color.primary_color).copy(alpha = 0.8f)),
					onClick = {
						emailIsError = mutableEmail.value.isEmpty()

						if(!emailIsError){
							saving = true

							scope.launch {
								scope.async {
									action(mutableEmail.value) }.await()
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
							fontFamily = font,
							fontSize = 16.sp,
							fontWeight = FontWeight.SemiBold
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
							interactionSource = interactionSource,
							indication = null,
							onClick = {	close()	}
						),
						text = "Закрыть",
						fontWeight = FontWeight.SemiBold,
						fontSize = 15.sp,
						color = Color.Gray,
						fontFamily = font,
					)
				}
			}
		}
	}
}