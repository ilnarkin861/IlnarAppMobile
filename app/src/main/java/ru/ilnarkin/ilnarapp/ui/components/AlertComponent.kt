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
fun AlertComponent(
	success: Boolean = true,
	message: String,
	visible: Boolean = false,
	action: () -> Unit)
{

	if (visible) {
		BasicAlertDialog(
			onDismissRequest = { },
			properties = DialogProperties(
				dismissOnBackPress = false,
				dismissOnClickOutside = false
			))
		{
			Surface(
				shape = MaterialTheme.shapes.small,
				tonalElevation = AlertDialogDefaults.TonalElevation)
			{
				Column(modifier = Modifier.background(Color.White)) {
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 15.dp, bottom = 10.dp),
						horizontalArrangement = Arrangement.Center)
					{
						if (!success)
							Icon(
								modifier = Modifier.size(70.dp),
								painter = painterResource(R.drawable.ic_error),
								contentDescription = "",
								tint = AppTheme.colors.dangerColor)
						else{
							Icon(
								modifier = Modifier.size(70.dp),
								painter = painterResource(R.drawable.ic_success),
								contentDescription = "",
								tint = AppTheme.colors.primaryColor)
						}
					}

					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(start = 10.dp, end = 10.dp, bottom = 25.dp),
						horizontalArrangement = Arrangement.Center)
					{
						Text(
							text = message,
							style = AppTheme.typography.modalText,
							color = if (!success) AppTheme.colors.dangerColor else AppTheme.colors.primaryColor)
					}

					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 30.dp, bottom = 30.dp),
						horizontalArrangement = Arrangement.Center)
					{
						Text(
							modifier = Modifier.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
								onClick = {	action() }),
							text = "Понятно",
							color = AppTheme.colors.colorGrey,
							style = AppTheme.typography.textButton
						)
					}
				}
			}
		}
	}
}