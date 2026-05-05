package ru.ilnarkin.ilnarapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_NOTE_TITLE
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteDetailsComponent(note: Note?)
{
	val titleColor = if (note?.title != null) AppTheme.colors.titleColor else AppTheme.colors.titleColor.copy(alpha = 0.4f)
	val containerPadding = AppTheme.dimensions.containerHorizontalPadding
	val listState = rememberLazyGridState()
	var imageCarouselVisible by remember { mutableStateOf(false) }
	var imageInitialIndex by remember { mutableIntStateOf(1) }


	Column(
		modifier = Modifier
			.fillMaxSize()
			.padding(start = containerPadding, top = 30.dp, end = containerPadding, bottom = 40.dp)
			.verticalScroll(rememberScrollState()))
	{
		Row {
			Text(
				color = titleColor,
				text = note?.title ?: DEFAULT_NOTE_TITLE,
				style = AppTheme.typography.noteDetailsTitle)
		}

		Row(modifier = Modifier.padding(top = 10.dp))
		{
			Text(
				text = DateTimeFormatter
					.ofPattern("d MMMM yyyy, EEEE")
					.format(LocalDate.parse(note!!.date)),
				style = AppTheme.typography.noteDetailsDate,
				color = AppTheme.colors.colorGrey)
		}

		Row(modifier = Modifier.padding(top = 15.dp, bottom = 20.dp))
		{
			HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
		}

		Row {
			Text(
				text = note!!.text,
				style = AppTheme.typography.noteDetailsText,
				color = AppTheme.colors.textColor)
		}

		Row(modifier = Modifier.padding(top = 15.dp, bottom = 20.dp))
		{
			HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
		}

		Row(modifier = Modifier.padding(bottom = 10.dp))
		{
			Text(
				text = "Тип: ",
				style = AppTheme.typography.noteDetailsText.copy(fontWeight = FontWeight.Bold),
				color = AppTheme.colors.titleColor)

			Text(
				text = note!!.noteType.title,
				style = AppTheme.typography.noteDetailsText,
				color = AppTheme.colors.textColor)
		}

		if (note!!.archive != null){
			Row(modifier = Modifier.padding(bottom = 10.dp))
			{
				Text(
					text = "Архив: ",
					style = AppTheme.typography.noteDetailsText.copy(fontWeight = FontWeight.Bold),
					color = AppTheme.colors.titleColor)

				Text(
					text = note.archive!!.title,
					style = AppTheme.typography.noteDetailsText,
					color = AppTheme.colors.textColor)
			}
		}

		if(!note.tags.isEmpty()){
			FlowRow (
				modifier = Modifier.padding(top = 30.dp, bottom = 40.dp),
				horizontalArrangement = Arrangement.spacedBy(10.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp))
			{

				note.tags.forEach { tag ->
					Text(
						modifier = Modifier
							.border(
								width = 1.dp,
								color = AppTheme.colors.primaryColor,
								shape = RoundedCornerShape(10.dp))
							.padding(horizontal = 20.dp, vertical = 10.dp),
						text = tag.title,
						style = AppTheme.typography.noteDetailsTags,
						color = AppTheme.colors.primaryColor)
				}
			}
		}

		if (note.noteImages.isNotEmpty()){
			LazyVerticalGrid(
				modifier = Modifier.weight(1f),
				state = listState,
				columns = GridCells.Fixed(2),
				contentPadding = PaddingValues(bottom = 30.dp))
			{
				itemsIndexed(note.noteImages){index, image ->

					Box(
						modifier = Modifier
							.padding(4.dp)
							.clip(shape = RoundedCornerShape(10.dp))
							.aspectRatio(1f)
							.clickable(
								onClick = {
									imageInitialIndex = index
									imageCarouselVisible = true
								}
							)
						)
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
			}
		}
	}


	if (imageCarouselVisible) {
		note?.let {
			ImageCarouselComponent(
				initialIndex = imageInitialIndex,
				images = it.noteImages,
				onClose = { imageCarouselVisible = false }
			)
		}
	}
}