package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
fun EmptyListMessageComponent(text: String) {
	Box(
		modifier = Modifier.background(colorResource(R.color.app_bg_color)).fillMaxSize(),
		contentAlignment = Alignment.Center){
		Text(text,
			fontSize = 16.sp,
			fontFamily = getInterFont(),
			color = colorResource(R.color.primary_color)
		)
	}

}