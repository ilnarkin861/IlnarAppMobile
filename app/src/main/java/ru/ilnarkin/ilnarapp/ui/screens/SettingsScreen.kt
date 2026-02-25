package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.EmailFormComponent
import ru.ilnarkin.ilnarapp.ui.components.PasswordFormComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
@Preview(showBackground = true, showSystemUi = true, device = PIXEL_3)
fun SettingsScreen() {

	val testEmail = "info@example.com"
	val testPassword = "qwerty1234"

	val font = getInterFont()

	var emailFormDialogShowed by remember { mutableStateOf(false) }

	var passwordFormDialogShowed by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	var success by remember { mutableStateOf(true) }

	val alertTitle = remember { mutableStateOf("") }
	var showAlert by remember { mutableStateOf(false) }

	var emailLoading by remember { mutableStateOf(false) }


	Column(Modifier.fillMaxSize().padding(top = 30.dp)) {

		Row(Modifier.fillMaxWidth().clickable(
			interactionSource = remember { MutableInteractionSource() },
			indication = ripple(),
			onClick = {

				scope.launch {
					emailLoading = true

					delay(1500)

					emailFormDialogShowed = true
				}
			}
		)) {

			Row(Modifier.fillMaxWidth().padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding), vertical = 20.dp),
				horizontalArrangement = Arrangement.SpaceBetween) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						modifier = Modifier.size(25.dp),
						painter = painterResource(R.drawable.ic_mail),
						contentDescription = "Mail",
						tint = colorResource(R.color.grey)
					)
					Text(text = "Изменить Email",
						modifier = Modifier.padding(start = 10.dp),
						fontFamily = font,
						fontSize = 16.sp,
						color = colorResource(R.color.grey))
				}

				Row(modifier = Modifier.size(25.dp),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically) {

					if (emailLoading){
						ProgressIndicatorComponent(15, colorResource(R.color.grey).copy(alpha = 0.7f))
					}

					else{
						Icon(
							modifier = Modifier.size(15.dp),
							painter = painterResource(R.drawable.ic_arrow_right),
							contentDescription = "Arrow right",
							tint = colorResource(R.color.grey).copy(alpha = 0.7f)
						)
					}
				}
			}
		}

		HorizontalDivider(
			modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding)),
			thickness = 1.dp,
			color = colorResource(R.color.border_color))

		Row(Modifier.fillMaxWidth().clickable(
			interactionSource = remember { MutableInteractionSource() },
			indication = ripple(),
			onClick = { passwordFormDialogShowed = true }
		)) {
			Row(Modifier.fillMaxWidth().padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding), vertical = 20.dp),
				horizontalArrangement = Arrangement.SpaceBetween,
				verticalAlignment = Alignment.CenterVertically) {
				Row(verticalAlignment = Alignment.CenterVertically) {
					Icon(
						modifier = Modifier.size(25.dp),
						painter = painterResource(R.drawable.ic_password),
						contentDescription = "Password",
						tint = colorResource(R.color.grey)
					)
					Text(text = "Сменить пароль",
						modifier = Modifier.padding(start = 10.dp),
						fontFamily = font,
						fontSize = 16.sp,
						color = colorResource(R.color.grey))
				}

				Row(modifier = Modifier.size(25.dp),
					horizontalArrangement = Arrangement.Center,
					verticalAlignment = Alignment.CenterVertically) {
					Icon(
						modifier = Modifier.size(15.dp),
						painter = painterResource(R.drawable.ic_arrow_right),
						contentDescription = "Arrow right",
						tint = colorResource(R.color.grey).copy(alpha = 0.7f)
					)
				}
			}
		}
	}


	AlertComponent(
		success = success,
		message = alertTitle.value,
		showed = showAlert,
		action = { showAlert = false }
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
					email = testEmail,
					action = {
						emailFormDialogShowed = false
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