package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
fun MessageComponent(text: String) {
	Text(
		text = text,
		fontFamily = getInterFont(),
		color = Color.Gray,
		fontSize = 15.sp
	)
}