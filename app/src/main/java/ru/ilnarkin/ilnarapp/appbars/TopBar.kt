package ru.ilnarkin.ilnarapp.appbars

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar () {
	val borderColor = colorResource(R.color.border_color)

	Column(Modifier
		.fillMaxWidth()
		.height(dimensionResource(R.dimen.top_bar_height))
		.background(Color.White)
		.drawBehind {
			var borderStrokeWidth = 2.dp
			val strokeWidthPx = borderStrokeWidth.toPx()

			drawLine(
				color = borderColor,
				start = Offset(0f, size.height),
				end = Offset(size.width, size.height),
				strokeWidth = strokeWidthPx
			)
		}) {

		Row(
			Modifier.fillMaxWidth()
				.fillMaxHeight()
				.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding)),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween
		) {

			Box {
				Text(text = stringResource(R.string.notes_title),
					color = colorResource(R.color.primary_color),
					fontSize = dimensionResource(R.dimen.top_bar_title_font_size).value.sp,
					fontFamily = getInterFont(),
					fontWeight = FontWeight.ExtraBold)
			}
			
			Row(verticalAlignment = Alignment.CenterVertically){
				val color = colorResource(R.color.primary_color)

				IconButton(onClick = {}) {
					Icon(
						painter = painterResource(R.drawable.ic_settings),
						contentDescription = "",
						tint = color)
				}

				IconButton(onClick = {}) {
					Icon(
						painter = painterResource(R.drawable.ic_logout),
						contentDescription = "",
						tint = color)
				}
			}
		}
	}
}