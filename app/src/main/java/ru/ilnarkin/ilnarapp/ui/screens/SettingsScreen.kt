package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
@Preview(showBackground = true, showSystemUi = true, device = PIXEL_3)
fun SettingsScreen() {

	val font = getInterFont()
	val interactionSource = remember { MutableInteractionSource() }

	Column(Modifier.fillMaxSize().padding(top = 30.dp)) {

		Row(Modifier.fillMaxWidth().padding(vertical = 20.dp).clickable(
			interactionSource = interactionSource,
			indication = null,
			onClick = {}
		),
			horizontalArrangement = Arrangement.SpaceBetween) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Icon(
					modifier = Modifier.size(25.dp),
					painter = painterResource(R.drawable.ic_mail),
					contentDescription = "Mail",
					tint = colorResource(R.color.grey).copy(alpha = 0.7f)
				)
				Text(text = "Изменить Email",
					modifier = Modifier.padding(start = 10.dp),
					fontFamily = font,
					fontSize = 16.sp,
					color = colorResource(R.color.grey))
			}

			Row {
				Icon(
					modifier = Modifier.size(15.dp),
					painter = painterResource(R.drawable.ic_arrow_right),
					contentDescription = "Arrow right",
					tint = colorResource(R.color.grey).copy(alpha = 0.7f)
				)
			}
		}

		HorizontalDivider(
			thickness = 1.dp,
			color = colorResource(R.color.border_color))

		Row(Modifier.fillMaxWidth().padding(vertical = 20.dp).clickable(
			interactionSource = interactionSource,
			indication = null,
			onClick = {}
		),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Icon(
					modifier = Modifier.size(25.dp),
					painter = painterResource(R.drawable.ic_password),
					contentDescription = "Password",
					tint = colorResource(R.color.grey).copy(alpha = 0.7f)
				)
				Text(text = "Сменить пароль",
					modifier = Modifier.padding(start = 10.dp),
					fontFamily = font,
					fontSize = 16.sp,
					color = colorResource(R.color.grey))
			}

			Row {
				Icon(
					modifier = Modifier.size(15.dp),
					painter = painterResource(R.drawable.ic_arrow_right),
					contentDescription = "Arrow right",
					tint = colorResource(R.color.grey).copy(alpha = 0.7f)
				)
			}
		}

		HorizontalDivider(
			thickness = 1.dp,
			color = colorResource(R.color.border_color))

		Row(Modifier.fillMaxWidth().padding(vertical = 20.dp).clickable(
			interactionSource = interactionSource,
			indication = null,
			onClick = {}
		),
			horizontalArrangement = Arrangement.SpaceBetween,
			verticalAlignment = Alignment.CenterVertically) {
			Row(verticalAlignment = Alignment.CenterVertically) {
				Icon(
					modifier = Modifier.size(25.dp),
					painter = painterResource(R.drawable.ic_padlock),
					contentDescription = "Padlock",
					tint = colorResource(R.color.grey).copy(alpha = 0.7f)
				)
				Text(text = "Изменить PIN-код",
					modifier = Modifier.padding(start = 10.dp),
					fontFamily = font,
					fontSize = 16.sp,
					color = colorResource(R.color.grey))
			}

			Row {
				Icon(
					modifier = Modifier.size(15.dp),
					painter = painterResource(R.drawable.ic_arrow_right),
					contentDescription = "Arrow right",
					tint = colorResource(R.color.grey).copy(alpha = 0.7f)
				)
			}
		}
	}
}