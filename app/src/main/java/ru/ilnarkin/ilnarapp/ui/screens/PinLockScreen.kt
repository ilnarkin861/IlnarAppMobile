package ru.ilnarkin.ilnarapp.ui.screens

import android.content.Context
import android.content.Intent
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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import ru.ilnarkin.ilnarapp.MainActivity
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.KEY_PIN
import ru.ilnarkin.ilnarapp.helpers.KEY_TOKEN
import ru.ilnarkin.ilnarapp.helpers.PREFS_NAME
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.components.ConfirmComponent
import ru.ilnarkin.ilnarapp.ui.components.PinKeypadItemComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent


@Composable
fun PinLockScreen(navController: NavController) {

	val context = LocalContext.current
	val intent = Intent(context, MainActivity::class.java)
	val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

	val orientation = LocalConfiguration.current.orientation
	val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
	val modifier = if(isLandscape) Modifier.wrapContentHeight() else Modifier


	val font = getInterFont()
	val inputPin = remember { mutableStateListOf<Int>() }
	var incorrectPin by remember { mutableStateOf(false) }
	var showConfirmAlert by remember { mutableStateOf(false) }
	val dialogState = rememberMaterialDialogState()
	val scrollState = rememberScrollState()

	if (inputPin.size == 4){
		LaunchedEffect(true) {

			dialogState.show()

			delay(2000)

			val pin = sharedPreferences.getString(KEY_PIN, null)

			if (pin != null && pin != inputPin.joinToString("")){
				incorrectPin = true
				dialogState.hide()
			}

			else{
				dialogState.hide()
				context.startActivity(intent)
			}

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
						Box(modifier = Modifier.padding(15.dp)
							.alpha(if (inputPin.size > it) 1f else 0.5f)
							.background(
								color =  Color.DarkGray,
								shape = CircleShape)
							.size(15.dp)) {  }
					}
				}
			}

			if (incorrectPin){
				Row(modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center) {
					Text("Неверный PIN-код",
						color = colorResource(R.color.danger_color),
						fontFamily = font,
						fontSize = 16.sp)
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
								color = Color.Gray,
								fontFamily = font,
								fontSize = 20.sp,
								fontWeight = FontWeight.SemiBold
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
								color = Color.Gray,
								fontFamily = font,
								fontSize = 20.sp,
								fontWeight = FontWeight.SemiBold
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
								color = Color.Gray,
								fontFamily = font,
								fontSize = 20.sp,
								fontWeight = FontWeight.SemiBold
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
						color = Color.Gray,
						textAlign = TextAlign.Center,
						fontFamily = font,
						fontSize = 14.sp,
						fontWeight = FontWeight.SemiBold
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
						color = Color.Gray,
						fontFamily = font,
						fontSize = 20.sp,
						fontWeight = FontWeight.SemiBold
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
						tint = Color.Gray)
				}
			}
		}
	}


	MaterialDialog(
		dialogState = dialogState,
		shape = MaterialTheme.shapes.small,
		onCloseRequest = { MaterialDialogState.Saver() },
	){
		Column(modifier = Modifier.background(Color.White).padding(horizontal = 16.dp, vertical = 20.dp)) {
			Row(modifier = Modifier.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically) {

				ProgressIndicatorComponent(size = 40, color = colorResource(R.color.primary_color))

				Text("Проверка PIN-кода",
					modifier = Modifier.padding(start = 15.dp),
					color = colorResource(R.color.text_color),
					fontFamily = font,
					fontSize = 16.sp)
			}
		}
	}

	ConfirmComponent(
		showed = showConfirmAlert,
		text = "Чтобы восстановить PIN-код, нужно будет заново зайти в систему. Продолжить?",
		action = {confirmed ->

			if (confirmed){
				sharedPreferences.edit{ putString(KEY_TOKEN, null) }

				navController.navigate(NavRoutes.WelcomeScreen.route){
					popUpTo(navController.graph.findStartDestination().id) {
						inclusive = true
					}
				}
			}

			showConfirmAlert = false
		}
	)
}