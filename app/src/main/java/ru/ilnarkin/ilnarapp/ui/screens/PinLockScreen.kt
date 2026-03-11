package ru.ilnarkin.ilnarapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.components.ConfirmComponent
import ru.ilnarkin.ilnarapp.ui.components.PinKeypadItemComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinLockScreen(
	navController: NavController,
	userViewModel: UserViewModel = koinViewModel()

) {

	val orientation = LocalConfiguration.current.orientation
	val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

	val modifier = if(isLandscape) Modifier.wrapContentHeight() else Modifier

	val inputPin = remember { mutableStateListOf<Int>() }
	var incorrectPin by remember { mutableStateOf(false) }

	var showConfirmAlert by remember { mutableStateOf(false) }

	val scrollState = rememberScrollState()

	var showLoading by remember { mutableStateOf(false) }


	if (inputPin.size == 4){
		LaunchedEffect(true) {

			showLoading = true

			val pin = userViewModel.getPinCode()

			if (pin != inputPin.joinToString("")){
				incorrectPin = true
			}

			else{
				navController.navigate(NavRoutes.OverlayScreen.route) {
					popUpTo(NavRoutes.PinLockScreen.route) { inclusive = true }
				}
			}

			showLoading = false

			inputPin.clear()
		}
	}


	Column(modifier.fillMaxSize().padding(
		start = dimensionResource(R.dimen.container_horizontal_padding),
		end = dimensionResource(R.dimen.container_horizontal_padding)).verticalScroll(scrollState),
		verticalArrangement = Arrangement.SpaceBetween) {

		Column(Modifier.padding(top = 100.dp)) {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center) {
				Image(
					painter = painterResource(R.drawable.ic_pin_lock),
					contentDescription = "Lock",
					alpha = 0.4f)
			}

			Row(modifier = Modifier.fillMaxWidth().padding(
				top = 20.dp),
				horizontalArrangement = Arrangement.Center) {
				Row(Modifier.padding(bottom = 40.dp)) {
					(0 until 4).forEach {
						Box(modifier = Modifier.padding(10.dp)
							.alpha(if (inputPin.size > it) 1f else 0.5f)
							.background(
								color = Color.DarkGray,
								shape = CircleShape)
							.size(15.dp)) {  }
					}
				}
			}

			if (incorrectPin){
				Row(modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center) {
					Text("Неверный PIN-код",
						color = AppTheme.colors.dangerColor,
						style = AppTheme.typography.errorText)
				}
			}
		}

		Column(modifier = Modifier.fillMaxWidth().padding(
			top = if(isLandscape) 100.dp else 0.dp,
			bottom = 50.dp
		)) {
			Row(modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center) {
				Row {
					(1..3).forEach {
						PinKeypadItemComponent(
							onClick = {
								incorrectPin = false

								if(inputPin.size < 4) {
									inputPin.add(it)
								}
							}
						) {
							Text(
								it.toString(),
								color = AppTheme.colors.colorGrey,
								style = AppTheme.typography.keyPadItemText
							)
						}
					}
				}
			}

			Row(modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center) {
				Row {
					(4..6).forEach {
						PinKeypadItemComponent(
							onClick = {
								incorrectPin = false

								if(inputPin.size < 4) {
									inputPin.add(it)
								}
							}
						) {
							Text(
								it.toString(),
								color = AppTheme.colors.colorGrey,
								style = AppTheme.typography.keyPadItemText
							)
						}
					}
				}
			}

			Row(modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center) {
				Row {
					(7..9).forEach {
						PinKeypadItemComponent(
							onClick = {
								incorrectPin = false

								if(inputPin.size < 4) {
									inputPin.add(it)
								}
							}
						) {
							Text(
								it.toString(),
								color = AppTheme.colors.colorGrey,
								style = AppTheme.typography.keyPadItemText
							)
						}
					}
				}
			}

			Row(modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center) {

				PinKeypadItemComponent(
					onClick = {
						showConfirmAlert = true
					},

					bordered = false
				) {

					Text(
						"Я забыл\nкод",
						color = AppTheme.colors.colorGrey,
						style = AppTheme.typography.forgotPinText
					)
				}

				PinKeypadItemComponent(
					onClick = {
						incorrectPin = false

						if(inputPin.size < 4) {
							inputPin.add(0)
						}
					}
				) {
					Text(
						"0",
						color = AppTheme.colors.colorGrey,
						style = AppTheme.typography.keyPadItemText
					)
				}

				PinKeypadItemComponent(
					onClick = { if(!inputPin.isEmpty()) inputPin.removeAt(inputPin.lastIndex) },
					bordered = false
				) {
					Icon(
						modifier = Modifier.size(30.dp).alpha(0.5f),
						painter = painterResource(R.drawable.ic_backspace),
						contentDescription = "Delete",
						tint = AppTheme.colors.colorGrey)
				}
			}
		}
	}



	if (showLoading){
		BasicAlertDialog(
			onDismissRequest = {},
			properties = DialogProperties(
				dismissOnBackPress = false,
				dismissOnClickOutside = false
			)) {
			Surface(
				shape = MaterialTheme.shapes.small,
				tonalElevation = AlertDialogDefaults.TonalElevation
			) {
				Column(modifier = Modifier.background(Color.White).padding(horizontal = 16.dp, vertical = 20.dp)) {
					Row(modifier = Modifier.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically) {

						ProgressIndicatorComponent(size = 40, color = AppTheme.colors.primaryColor)

						Text("Проверка PIN-кода",
							modifier = Modifier.padding(start = 15.dp),
							color = AppTheme.colors.textColor,
							style = AppTheme.typography.pinModalText)
					}
				}
			}
		}
	}


	ConfirmComponent(
		showed = showConfirmAlert,
		text = "Чтобы восстановить PIN-код, нужно будет заново зайти в систему. Продолжить?",
		action = {confirmed ->

			if (confirmed){

				userViewModel.clearToken()

				navController.navigate(NavRoutes.WelcomeScreen.route){
					popUpTo(NavRoutes.PinLockScreen.route) {
						inclusive = true
					}
				}
			}

			showConfirmAlert = false
		}
	)
}