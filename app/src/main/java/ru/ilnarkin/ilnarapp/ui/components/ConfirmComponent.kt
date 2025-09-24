package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
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

	val dialogState = rememberMaterialDialogState()
	if (showed) dialogState.show()

	MaterialDialog(
		dialogState = dialogState,
		shape = MaterialTheme.shapes.small,
		onCloseRequest = { MaterialDialogState.Saver() },
		buttons = {
			positiveButton(
				text = "Да",
				textStyle = TextStyle(
					color = colorResource(R.color.danger_color),
					fontFamily = getInterFont(),
					fontWeight = FontWeight.SemiBold),
				onClick = {
					dialogState.hide()
					action(true)
				}
			)

			negativeButton(
				text = "Нет",
				textStyle = TextStyle(
					color = colorResource(R.color.title_color),
					fontFamily = getInterFont(),
					fontWeight = FontWeight.SemiBold),
				onClick = {
					dialogState.hide()
					action(false)
				}
			)

		}
	) {
		Column(
			modifier = Modifier.background(Color.White),

			) {
			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 15.dp, bottom = 10.dp),
				horizontalArrangement = Arrangement.Center
			) {
				Icon(
					modifier = Modifier.size(60.dp),
					painter = painterResource(R.drawable.ic_warning),
					contentDescription = "",
					tint = colorResource(R.color.warning_color))
			}

			Row(
				modifier = Modifier.fillMaxWidth().padding(start = 10.dp, end = 10.dp, bottom = 30.dp),
				horizontalArrangement = Arrangement.Center
			) {
				Text(
					text = "Точно хочешь удалить?",
					fontWeight = FontWeight.Bold,
					fontSize = 16.sp,
					fontFamily = getInterFont(),
					color = colorResource(R.color.warning_color)
				)
			}
		}

	}

}