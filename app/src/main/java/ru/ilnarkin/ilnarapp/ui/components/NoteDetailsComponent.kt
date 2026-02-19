package ru.ilnarkin.ilnarapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Note
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteDetailsComponent(note: Note?) {

	val containerPadding = dimensionResource(R.dimen.container_horizontal_padding)

	val font = getInterFont()


	Column(Modifier.fillMaxSize()
		.padding(start = containerPadding, top = 30.dp, end = containerPadding)
		.verticalScroll(rememberScrollState())){

		Row {
			Text(
				color = colorResource(R.color.title_color),
				text = note?.title ?: "Без названия",
				fontFamily = font,
				fontSize = dimensionResource(R.dimen.note_title_font_size).value.sp,
				fontWeight = FontWeight.Bold
			)
		}

		Row(Modifier.padding(top = 10.dp)) {
			Text(
				text = DateTimeFormatter.ofPattern("dd.MM.yyyy").format(LocalDate.parse(note!!.date)),
				fontFamily = font,
				color = colorResource(R.color.grey),
				fontSize = dimensionResource(R.dimen.note_date_font_size).value.sp
			)
		}

		Row(Modifier.padding(top = 15.dp, bottom = 20.dp)) {
			HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
		}

		Row {
			Text(
				text = note!!.text,
				fontFamily = font,
				lineHeight = 1.5.em,
				color = colorResource(R.color.text_color),
				fontSize = dimensionResource(R.dimen.note_text_font_size).value.sp
			)
		}

		Row(Modifier.padding(top = 15.dp, bottom = 20.dp)) {
			HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
		}

		Row(Modifier.padding(bottom = 10.dp)) {
			Text(
				text = "Тип: ",
				fontFamily = font,
				fontWeight = FontWeight.Bold,
				fontSize = 15.sp,
				color = colorResource(R.color.title_color),
			)

			Text(
				text = note!!.noteType.title,
				fontFamily = font,
				fontSize = 15.sp,
				color = colorResource(R.color.text_color),
			)
		}

		if (note!!.archive != null){
			Row(Modifier.padding(bottom = 10.dp)) {
				Text(
					text = "Архив: ",
					fontFamily = font,
					fontWeight = FontWeight.Bold,
					fontSize = 15.sp,
					color = colorResource(R.color.title_color),
				)

				Text(
					text = note.archive!!.title,
					fontFamily = font,
					fontSize = 15.sp,
					color = colorResource(R.color.text_color),
				)
			}
		}

		if(!note.tags.isEmpty()){
			FlowRow (
				modifier = Modifier.padding(top = 30.dp),
				horizontalArrangement = Arrangement.spacedBy(10.dp),
				verticalArrangement = Arrangement.spacedBy(8.dp)
			) {

				note.tags.forEach { tag ->
					Text(
						modifier = Modifier
							.border(
								width = 1.dp,
								color = colorResource(R.color.primary_color),
								shape = RoundedCornerShape(10.dp))
							.padding(horizontal = 20.dp, vertical = 10.dp),
						text = tag.title,
						fontSize = 13.sp,
						color = colorResource(R.color.primary_color))
				}
			}
		}
	}
}