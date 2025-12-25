package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
fun NetworkErrorComponent(action: () -> Unit) {

	val fontFamily = getInterFont()


	Box(
		modifier = Modifier.background(colorResource(R.color.app_bg_color)).fillMaxSize(),
		contentAlignment = Alignment.Center){

		Column {
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			){
				Image(
					painter = painterResource(R.drawable.network_error),
					contentDescription = "Network error",
					alpha = 0.5f)
			}

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center
			){
				Text(
					"Что-то с интернетом",
					color = colorResource(R.color.title_color),
					fontFamily = fontFamily,
					fontWeight = FontWeight.Bold,
					fontSize = 18.sp)
			}

			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
				horizontalArrangement = Arrangement.Center
			){
				Text(
					"Проверь подключение и попробуй еще раз",
					color = colorResource(R.color.text_color),
					fontFamily = fontFamily,
					fontSize = 12.sp)
			}

			Row(
				modifier = Modifier.fillMaxWidth().padding(top = 25.dp),
				horizontalArrangement = Arrangement.Center
			){
				Text(
					"Попробовать снова",
					modifier = Modifier.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = { action() }
					),
					color = colorResource(R.color.primary_color),
					fontFamily = fontFamily,
					fontWeight = FontWeight.SemiBold,
					fontSize = 12.sp)
			}
		}
	}
}