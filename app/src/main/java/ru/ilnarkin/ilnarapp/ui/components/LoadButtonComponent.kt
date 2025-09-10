package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import ru.ilnarkin.ilnarapp.R


@Composable
fun LoadButtonComponent(nextButton: Boolean = true, action: () -> Unit) {
	var painter: Painter = if (nextButton)
		painterResource(R.drawable.ic_arrow_down)
	else painterResource(R.drawable.ic_arrow_up)

	Row(
		modifier = Modifier.fillMaxWidth(),
		horizontalArrangement = Arrangement.Center
	){
		IconButton(

			onClick = { action() }) {
			Icon(
				modifier = Modifier.fillMaxWidth().size(50.dp),
				painter = painter,
				contentDescription = "Загрузить",
				tint = Color.Gray
			)
		}
	}
}