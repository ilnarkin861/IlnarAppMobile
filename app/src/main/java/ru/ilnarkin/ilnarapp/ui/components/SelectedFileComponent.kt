package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.models.SelectedFileInfo
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@Composable
fun SelectedFileComponent(
	file: SelectedFileInfo,
	delete: (file: SelectedFileInfo) -> Unit)
{
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(bottom = 10.dp)
			.border(
				width = 1.dp,
				color = AppTheme.colors.colorGrey.copy(alpha = 0.3f),
				shape = RoundedCornerShape(10.dp))
		)
	{
		Row(
			modifier = Modifier
				.fillMaxWidth(),
			verticalAlignment = Alignment.CenterVertically,
			horizontalArrangement = Arrangement.SpaceBetween)
		{
			Box(
				modifier = Modifier
					.padding(10.dp)
					.size(50.dp)
					.clip(RoundedCornerShape(5.dp)))
			{
				AsyncImage(
					modifier = Modifier.fillMaxSize(),
					model = file.uri,
					contentDescription = null,
					contentScale = ContentScale.Crop,
					placeholder = ColorPainter(Color.LightGray),
					error = ColorPainter(Color.Red)
				)
			}

			Box()
			{
				IconButton(onClick = {
					delete(file)
				})
				{
					Icon(
						modifier = Modifier.size(25.dp),
						painter = painterResource(R.drawable.ic_close_circle),
						contentDescription = "",
						tint = AppTheme.colors.dangerColor)
				}
			}
		}

	}
}