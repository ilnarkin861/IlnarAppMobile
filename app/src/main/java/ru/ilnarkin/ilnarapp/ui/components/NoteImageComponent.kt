package ru.ilnarkin.ilnarapp.ui.components


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
import ru.ilnarkin.ilnarapp.models.FileInfo
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@Composable
fun NoteImageComponent(
	image: FileInfo,
	delete: (image: FileInfo) -> Unit)
{
	Row(
		modifier = Modifier
			.fillMaxWidth()
			.padding(vertical = 15.dp),
		verticalAlignment = Alignment.CenterVertically,
		horizontalArrangement = Arrangement.SpaceBetween)
	{
		Box(
			modifier = Modifier
				.fillMaxWidth()
				.weight(0.9f)
				.size(50.dp)
				.clip(RoundedCornerShape(5.dp)))
		{
			Box(
				modifier = Modifier
					.size(80.dp)
					.clip(RoundedCornerShape(5.dp)))
			{
				AsyncImage(
					modifier = Modifier.fillMaxSize(),
					model = image.url,
					contentDescription = null,
					contentScale = ContentScale.Crop,
					placeholder = ColorPainter(Color.LightGray),
					error = ColorPainter(Color.Red)
				)
			}
		}

		Box(
			modifier = Modifier
				.fillMaxWidth()
				.weight(0.1f))
		{
			IconButton(onClick = { delete(image) })
			{
				Icon(
					modifier = Modifier.size(20.dp),
					painter = painterResource(R.drawable.ic_trash),
					contentDescription = "",
					tint = AppTheme.colors.dangerColor)
			}
		}
	}
}