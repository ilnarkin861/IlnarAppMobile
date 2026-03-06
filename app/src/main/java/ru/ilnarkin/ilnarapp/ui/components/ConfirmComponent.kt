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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConfirmComponent(
	showed: Boolean = false,
	text: String = "Точно хочешь удалить?",
	action: (confirmed: Boolean) -> Unit) {

	val interactionSource = remember { MutableInteractionSource() }


	if (showed){
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

				Column(modifier = Modifier
					.background(Color.White)
					.padding(20.dp)) {
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 10.dp, bottom = 10.dp),
						horizontalArrangement = Arrangement.Center
					) {
						Icon(
							modifier = Modifier.size(60.dp),
							painter = painterResource(R.drawable.ic_warning),
							contentDescription = "",
							tint = AppTheme.colors.warningColor)
					}

					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(start = 10.dp, end = 10.dp, bottom = 25.dp),
						horizontalArrangement = Arrangement.Center
					) {
						Text(
							text = text,
							style = AppTheme.typography.modalText,
							color = AppTheme.colors.warningColor
						)
					}

					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 30.dp, bottom = 15.dp),
						horizontalArrangement = Arrangement.Center
					) {
						Row {
							Text(
								modifier = Modifier
									.padding(horizontal = 15.dp)
									.clickable(
										interactionSource = interactionSource,
										indication = null,
										onClick = {	action(false) }
									),
								text = "Нет",
								style = AppTheme.typography.textButton,
								color = AppTheme.colors.colorGrey,
							)

							Text(
								modifier = Modifier
									.padding(horizontal = 15.dp)
									.clickable(
										interactionSource = interactionSource,
										indication = null,
										onClick = {
											action(true)
										}
									),
								text = "Да",
								style = AppTheme.typography.textButton,
								color = AppTheme.colors.dangerColor
							)
						}
					}
				}
			}
		}
	}
}