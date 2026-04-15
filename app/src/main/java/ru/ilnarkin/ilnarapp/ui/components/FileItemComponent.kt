package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.ilnarkin.ilnarapp.R


@Composable
fun FileItemComponent(
	url: String,
	isSelected: Boolean = false,
	onClick: () -> Unit,
	onLongClick: () -> Unit)
{
	Box(
		modifier = Modifier
			.padding(4.dp)
			.aspectRatio(1f)
			.combinedClickable(
				onClick = {
					onClick()
				},
				onLongClick = {
					onLongClick()
				}
			)
		)
	{
		AsyncImage(
			modifier = Modifier.fillMaxSize(),
			model = url,
			contentDescription = null,
			contentScale = ContentScale.Crop,
			placeholder = ColorPainter(Color.LightGray),
			error = ColorPainter(Color.Red)
		)


		if (isSelected) {
			Box(
				modifier = Modifier
					.fillMaxSize()
					.background(Color.Black.copy(alpha = 0.6f)),
				contentAlignment = Alignment.BottomEnd
			) {
				Icon(
					modifier = Modifier
						.size(36.dp)
						.padding(4.dp),
					painter = painterResource(R.drawable.ic_check_circle),
					contentDescription = null,
					tint = Color.White,
				)
			}
		}
	}
}