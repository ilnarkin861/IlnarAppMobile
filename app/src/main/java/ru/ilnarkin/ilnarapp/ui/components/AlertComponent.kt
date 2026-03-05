package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertComponent(
	success: Boolean = true,
	message: String,
	showed: Boolean = false,
	action: () -> Unit
) {

	val font = getInterFont()

	if (showed) {
		BasicAlertDialog(
			onDismissRequest = { },
			properties = DialogProperties(
				dismissOnBackPress = false,
				dismissOnClickOutside = false
			)
		) {
			Surface(
				shape = MaterialTheme.shapes.small,
				tonalElevation = AlertDialogDefaults.TonalElevation
			) {
				Column(modifier = Modifier.background(Color.White)) {
					Row(
						modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 10.dp),
						horizontalArrangement = Arrangement.Center
					) {
						if (!success)
							Icon(
								modifier = Modifier.size(70.dp),
								painter = painterResource(R.drawable.ic_error),
								contentDescription = "",
								tint = colorResource(R.color.danger_color))
						else{
							Icon(
								modifier = Modifier.size(70.dp),
								painter = painterResource(R.drawable.ic_success),
								contentDescription = "",
								tint = colorResource(R.color.primary_color))
						}
					}

					Row(
						modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 25.dp),
						horizontalArrangement = Arrangement.Center
					) {
						Text(
							text = message,
							fontSize = 16.sp,
							fontFamily = font,
							textAlign = TextAlign.Center,
							color = if (!success) colorResource(R.color.danger_color)
							else colorResource(R.color.primary_color)
						)
					}

					Row(
						modifier = Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 30.dp),
						horizontalArrangement = Arrangement.Center
					) {
						Text(
							modifier = Modifier.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
								onClick = {
									action()
								}
							),
							text = "Понятно",
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
}