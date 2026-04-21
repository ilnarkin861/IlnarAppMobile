package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.ilnarkin.ilnarapp.models.FileInfo


@Composable
fun NoteImageGrid(images: List<FileInfo>) {
	val count = images.size
	val shape = RoundedCornerShape(10.dp)

	Column(
		modifier = Modifier
			.fillMaxWidth()
			.clip(shape)
	) {
		when {
			count == 1 -> ImageItem(images[0].url, Modifier.aspectRatio(16/9f))

			count == 2 -> Row {
				ImageItem(images[0].url, Modifier.weight(1f).aspectRatio(1f))
				Spacer(Modifier.width(2.dp))
				ImageItem(images[1].url, Modifier.weight(1f).aspectRatio(1f))
			}

			count == 3 -> Row(Modifier.height(300.dp)) {
				ImageItem(images[0].url, Modifier.weight(1f).fillMaxHeight())
				Spacer(Modifier.width(2.dp))
				Column(Modifier.weight(1f)) {
					ImageItem(images[1].url, Modifier.weight(1f).fillMaxWidth())
					Spacer(Modifier.height(2.dp))
					ImageItem(images[2].url, Modifier.weight(1f).fillMaxWidth())
				}
			}

			count == 4 -> Column {
				Row {
					ImageItem(images[0].url, Modifier.weight(1f).aspectRatio(1.5f))
					Spacer(Modifier.width(2.dp))
					ImageItem(images[1].url, Modifier.weight(1f).aspectRatio(1.5f))
				}
				Spacer(Modifier.height(2.dp))
				Row {
					ImageItem(images[2].url, Modifier.weight(1f).aspectRatio(1.5f))
					Spacer(Modifier.width(2.dp))
					ImageItem(images[3].url, Modifier.weight(1f).aspectRatio(1.5f))
				}
			}

			count == 5 -> Column {
				Row(Modifier.height(180.dp)) {
					ImageItem(images[0].url, Modifier.weight(1f))
					Spacer(Modifier.width(2.dp))
					ImageItem(images[1].url, Modifier.weight(1f))
				}
				Spacer(Modifier.height(2.dp))
				Row(Modifier.height(120.dp)) {
					ImageItem(images[2].url, Modifier.weight(1f))
					Spacer(Modifier.width(2.dp))
					ImageItem(images[3].url, Modifier.weight(1f))
					Spacer(Modifier.width(2.dp))
					ImageItem(images[4].url, Modifier.weight(1f))
				}
			}

			count >= 6 -> Column {
				val displayImages = images.take(6)
				displayImages.chunked(3).forEachIndexed { rowIndex, rowImages ->
					Row(Modifier.height(120.dp)) {
						rowImages.forEachIndexed { colIndex, image ->
							Box(Modifier.weight(1f)) {
								ImageItem(image.url, Modifier.fillMaxSize())
							}
							if (colIndex < 2) Spacer(Modifier.width(2.dp))
						}
					}
					if (rowIndex == 0) Spacer(Modifier.height(2.dp))
				}
			}
		}
	}
}

@Composable
fun ImageItem(url: String, modifier: Modifier = Modifier) {
	AsyncImage(
		modifier = modifier,
		model = url,
		contentDescription = null,
		contentScale = ContentScale.Crop
	)
}