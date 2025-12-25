package ru.ilnarkin.ilnarapp.ui.screens

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import ru.ilnarkin.ilnarapp.MainActivity
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.KEY_PIN
import ru.ilnarkin.ilnarapp.helpers.PREFS_NAME
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.ui.components.PinKeypadItemComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent


@Composable
fun PinResetScreen() {

	val context = LocalContext.current
	val intent = Intent(context, MainActivity::class.java)
	val sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

	val orientation = LocalConfiguration.current.orientation
	val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE

	val modifier = if(isLandscape) Modifier.wrapContentHeight() else Modifier

	val font = getInterFont()

	val newPinTitle = "Введи новый PIN-код"
	val confirmPinTitle = "Подтверди PIN-код"
	val title = remember { mutableStateOf(newPinTitle) }

	var newPinEntered by remember { mutableStateOf(false) }
	var confirmPinEntered by remember { mutableStateOf(false) }

	val inputPin = remember { mutableStateListOf<Int>() }
	var pinCodeError by remember { mutableStateOf(false) }

	var newPin: String? by remember { mutableStateOf(null) }
	var confirmPin: String? by remember { mutableStateOf(null) }

	val dialogState = rememberMaterialDialogState()
	val scrollState = rememberScrollState()


	if (inputPin.size == 4){
		LaunchedEffect(true) {

			if (newPin != null && !confirmPinEntered){
				confirmPin = inputPin.joinToString("")
				confirmPinEntered = true
			}

			if (!newPinEntered && newPin == null){
				newPin = inputPin.joinToString("")
				newPinEntered = true
				title.value = confirmPinTitle
			}

			if (newPinEntered && confirmPinEntered){
				if (newPin != confirmPin){
					newPin = null
					confirmPin = null
					newPinEntered = false
					confirmPinEntered = false
					pinCodeError = true
					title.value = newPinTitle
				}

				else{
					dialogState.show()
					delay(2000)
					dialogState.hide()
					sharedPreferences.edit {putString(KEY_PIN, inputPin.joinToString(""))}
					context.startActivity(intent)
				}
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
				Text(title.value,
					color = colorResource(R.color.title_color),
					fontFamily = font,
					fontSize = 20.sp)
			}

			Row(modifier = Modifier.fillMaxWidth().padding(
				top = 20.dp),
				horizontalArrangement = Arrangement.Center) {
				Row(Modifier.padding(bottom = 40.dp)) {
					(0 until 4).forEach {
						Box(modifier = Modifier.padding(10.dp)
							.alpha(if (inputPin.size > it) 1f else 0.5f)
							.background(
								color =  Color.DarkGray,
								shape = CircleShape)
							.size(15.dp))
					}
				}
			}

			if (pinCodeError){
				Row(modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center) {
					Text("PIN-коды не совпадают",
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
								if(inputPin.size < 4) {
									pinCodeError = false
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
								if(inputPin.size < 4) {
									pinCodeError = false
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
								if(inputPin.size < 4) {
									pinCodeError = false
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
						if(inputPin.size < 4) {
							pinCodeError = false
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

			}
		}
	}


	MaterialDialog(
		dialogState = dialogState,
		shape = MaterialTheme.shapes.small,
		onCloseRequest = { MaterialDialogState.Saver() },
	){
		Column(modifier = Modifier.background(Color.White).padding(horizontal = 16.dp, vertical = 20.dp)) {
			Row(modifier = Modifier.fillMaxWidth(),	verticalAlignment = Alignment.CenterVertically) {

				ProgressIndicatorComponent(size = 40, color = colorResource(R.color.primary_color))

				Text("Подожди...",
					modifier = Modifier.padding(start = 15.dp),
					color = colorResource(R.color.text_color),
					fontFamily = font,
					fontSize = 16.sp)
			}
		}
	}
}