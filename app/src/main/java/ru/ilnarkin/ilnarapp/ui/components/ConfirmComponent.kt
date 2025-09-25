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
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
fun ConfirmComponent(showed: Boolean = false, action: (confirmed: Boolean) -> Unit) {

	val interactionSource = remember { MutableInteractionSource() }
	val dialogState = rememberMaterialDialogState()
	val fontFamily = getInterFont()

	if (showed) dialogState.show()

	MaterialDialog(
		dialogState = dialogState,
		shape = MaterialTheme.shapes.small,
		onCloseRequest = { MaterialDialogState.Saver() },
	) {
		Column(modifier = Modifier.background(Color.White).padding(20.dp)) {
			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 10.dp),
				horizontalArrangement = Arrangement.Center
			) {
				Icon(
					modifier = Modifier.size(60.dp),
					painter = painterResource(R.drawable.ic_warning),
					contentDescription = "",
					tint = colorResource(R.color.warning_color))
			}

			Row(
				modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 25.dp),
				horizontalArrangement = Arrangement.Center
			) {
				Text(
					text = "Точно хочешь удалить?",
					fontWeight = FontWeight.Bold,
					fontSize = 18.sp,
					fontFamily = fontFamily,
					color = colorResource(R.color.warning_color)
				)
			}

			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 30.dp, bottom = 15.dp),
				horizontalArrangement = Arrangement.Center
			) {
				Row {
					Text(
						modifier = Modifier.padding(horizontal = 15.dp).clickable(
							interactionSource = interactionSource,
							indication = null,
							onClick = {
								dialogState.hide()
								action(false)
							}
						),
						text = "Нет",
						fontWeight = FontWeight.SemiBold,
						fontSize = 15.sp,
						color = Color.Gray,
						fontFamily = fontFamily,
					)

					Text(
						modifier = Modifier.padding(horizontal = 15.dp).clickable(
							interactionSource = interactionSource,
							indication = null,
							onClick = {
								dialogState.hide()
								action(true)
							}
						),
						text = "Да",
						fontWeight = FontWeight.SemiBold,
						fontSize = 15.sp,
						color = colorResource(R.color.danger_color),
						fontFamily = fontFamily,
					)
				}
			}
		}
	}
}