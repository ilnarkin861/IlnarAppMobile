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
fun AlertComponent(
	success: Boolean = true,
	message: String,
	showed: Boolean = false,
	action: () -> Unit
) {
	val dialogState = rememberMaterialDialogState()

	val font = getInterFont()


	if (showed) {
		dialogState.show()
	}


	MaterialDialog(
		dialogState = dialogState,
		shape = MaterialTheme.shapes.small,
		onCloseRequest = { MaterialDialogState.Saver() },
	) {
		Column(modifier = Modifier.background(Color.White)) {
			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 10.dp),
				horizontalArrangement = Arrangement.Center
			) {
				if (!success)
					Icon(
						modifier = Modifier.size(60.dp),
						painter = painterResource(R.drawable.ic_error),
						contentDescription = "",
						tint = colorResource(R.color.danger_color))
				else{
					Icon(
						modifier = Modifier.size(60.dp),
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
							dialogState.hide()
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