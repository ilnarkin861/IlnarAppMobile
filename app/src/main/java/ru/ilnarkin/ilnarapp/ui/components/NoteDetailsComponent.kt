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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteDetailsComponent(note: Note?) {

	val containerPadding = AppTheme.dimensions.containerHorizontalPadding


	Column(Modifier.fillMaxSize()
		.padding(start = containerPadding, top = 30.dp, end = containerPadding, bottom = 40.dp)
		.verticalScroll(rememberScrollState())){

		Row {
			Text(
				color = AppTheme.colors.titleColor,
				text = note?.title ?: "Без названия",
				style = AppTheme.typography.noteTitle
			)
		}

		Row(Modifier.padding(top = 10.dp)) {
			Text(
				text = DateTimeFormatter
					.ofPattern("d MMMM yyyy, EEEE")
					.format(LocalDate.parse(note!!.date)),
				style = AppTheme.typography.noteDate,
				color = AppTheme.colors.colorGrey
			)
		}

		Row(Modifier.padding(top = 15.dp, bottom = 20.dp)) {
			HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
		}

		Row {
			Text(
				text = note!!.text,
				style = AppTheme.typography.noteText,
				color = AppTheme.colors.textColor
			)
		}

		Row(Modifier.padding(top = 15.dp, bottom = 20.dp)) {
			HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
		}

		Row(Modifier.padding(bottom = 10.dp)) {
			Text(
				text = "Тип: ",
				style = AppTheme.typography.noteDetailsText.copy(fontWeight = FontWeight.Bold),
				color = AppTheme.colors.titleColor,
			)

			Text(
				text = note!!.noteType.title,
				style = AppTheme.typography.noteDetailsText,
				color = AppTheme.colors.textColor,
			)
		}

		if (note!!.archive != null){
			Row(Modifier.padding(bottom = 10.dp)) {
				Text(
					text = "Архив: ",
					style = AppTheme.typography.noteDetailsText.copy(fontWeight = FontWeight.Bold),
					color = AppTheme.colors.titleColor,
				)

				Text(
					text = note.archive!!.title,
					style = AppTheme.typography.noteDetailsText,
					color = AppTheme.colors.textColor,
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
								color = AppTheme.colors.primaryColor,
								shape = RoundedCornerShape(10.dp))
							.padding(horizontal = 20.dp, vertical = 10.dp),
						text = tag.title,
						style = AppTheme.typography.noteTags,
						color = AppTheme.colors.primaryColor)
				}
			}
		}
	}
}