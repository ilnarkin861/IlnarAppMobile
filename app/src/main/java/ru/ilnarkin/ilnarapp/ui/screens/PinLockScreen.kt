package ru.ilnarkin.ilnarapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.delay
import ru.ilnarkin.ilnarapp.MainActivity
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent


@Composable
@Preview(showBackground = true, showSystemUi = true, device = PIXEL_3)
fun PinLockScreen() {

	val context = LocalContext.current
	val intent = Intent(context, MainActivity::class.java)

	val testPin = "1234"
	val font = getInterFont()
	val inputPin = remember { mutableStateListOf<Int>() }
	var incorrectPin by remember { mutableStateOf(false) }
	val dialogState = rememberMaterialDialogState()

	if (inputPin.size == 4){
		LaunchedEffect(true) {

			dialogState.show()

			delay(2000)

			if (inputPin.joinToString("") != testPin){
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


	Column(Modifier.fillMaxSize().padding(
		start = dimensionResource(R.dimen.container_horizontal_padding),
		end = dimensionResource(R.dimen.container_horizontal_padding),
		top = 100.dp),
		verticalArrangement = Arrangement.SpaceBetween) {

		Column {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center) {
				Image(
					painter = painterResource(R.drawable.logo_blue),
					contentDescription = "Logo",
					alpha = 0.6f)
			}

			Row(modifier = Modifier.fillMaxWidth().padding(top = 30.dp),
				horizontalArrangement = Arrangement.Center) {
				Row(Modifier.padding(bottom = 40.dp)) {
					(0 until 4).forEach {
						Box(modifier = Modifier.padding(10.dp)
							.alpha(if (inputPin.size > it) 1f else 0.5f)
							.background(
								color =  Color.DarkGray,
								shape = CircleShape)
							.size(20.dp)) {  }
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

		Column(modifier = Modifier.fillMaxWidth().padding(bottom = 100.dp)) {
			Row(modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center) {
				Row {
					(1..3).forEach {
						PinKeyComponent(
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
						PinKeyComponent(
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
						PinKeyComponent(
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

				PinKeyComponent(
					onClick = {	},

					bordered = false
				) {

					Text(
						"Забыл\nкод?",
						color = Color.Gray,
						textAlign = TextAlign.Center,
						fontFamily = font,
						fontSize = 14.sp,
						fontWeight = FontWeight.SemiBold
					)
				}

				PinKeyComponent(
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

				PinKeyComponent(
					onClick = { if(!inputPin.isEmpty()) inputPin.removeAt(inputPin.lastIndex) },
					bordered = false
				) {
					Icon(
						modifier = Modifier.size(30.dp),
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
}


@Composable
fun PinKeyComponent(
	onClick: () -> Unit,
	bordered: Boolean = true,
	content: @Composable () -> Unit
) {
	Surface(
		modifier = Modifier.padding(10.dp)
			.clip(shape = CircleShape)
			.border(width = if (bordered) 1.dp else 0.dp,
				color = if (bordered) Color.Gray else Color.Transparent,
				shape = CircleShape
			).size(70.dp),
		onClick = onClick,
	) {
		Box(modifier = Modifier.fillMaxSize().background(colorResource(R.color.app_bg_color)),
			contentAlignment = Alignment.Center) {  content() }
	}
}