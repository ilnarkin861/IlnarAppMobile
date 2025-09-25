package ru.ilnarkin.ilnarapp.ui.components

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
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Preview(showBackground = true, showSystemUi = true, device = PIXEL_3)
@Composable
fun NoteDetailsComponent() {

	val containerPadding = dimensionResource(R.dimen.container_horizontal_padding)
	val fontFamily = getInterFont()
	val noteFullText = """
		В маленьком городке, расположенном у подножия гор, ежегодно проходит фестиваль дружбы. Это событие собирает людей из разных уголков региона, и каждый год его темы отличаются.

		В этом году открыл его известный местный музыкант, который исполнил песни о дружбе и единстве. На главной площади горько улыбалась выступление детей из местной школы. Их танец, который они подготовили специально для этого дня, зацепил сердца всех зрителей.
	""".trimIndent()

	Column(Modifier.fillMaxSize()
		.padding(start = containerPadding, top = 30.dp, end = containerPadding)
		.verticalScroll(rememberScrollState())){

		Row {
			Text(
				color = colorResource(R.color.title_color),
				text = "Событие: Фестиваль дружбы",
				fontFamily = fontFamily,
				fontSize = dimensionResource(R.dimen.note_title_font_size).value.sp,
				fontWeight = FontWeight.Bold
			)
		}

		Row(Modifier.padding(top = 10.dp)) {
			Text(
				text = "25.09.2025",
				fontFamily = fontFamily,
				color = colorResource(R.color.grey),
				fontSize = dimensionResource(R.dimen.note_date_font_size).value.sp
			)
		}

		Row(Modifier.padding(top = 15.dp, bottom = 20.dp)) {
			HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
		}

		Row {
			Text(
				text = noteFullText,
				fontFamily = fontFamily,
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
				fontFamily = fontFamily,
				fontWeight = FontWeight.Bold,
				fontSize = 15.sp,
				color = colorResource(R.color.title_color),
			)

			Text(
				text = "Событие",
				fontFamily = fontFamily,
				fontSize = 15.sp,
				color = colorResource(R.color.text_color),
			)
		}

		Row(Modifier.padding(bottom = 10.dp)) {
			Text(
				text = "Архив: ",
				fontFamily = fontFamily,
				fontWeight = FontWeight.Bold,
				fontSize = 15.sp,
				color = colorResource(R.color.title_color),
			)

			Text(
				text = "Архив 1",
				fontFamily = fontFamily,
				fontSize = 15.sp,
				color = colorResource(R.color.text_color),
			)
		}

		FlowRow (
			modifier = Modifier.padding(top = 30.dp),
			horizontalArrangement = Arrangement.spacedBy(10.dp),
			verticalArrangement = Arrangement.spacedBy(8.dp)
			) {

			Text(
				modifier = Modifier
					.border(
						width = 1.dp,
						color = colorResource(R.color.primary_color),
						shape = RoundedCornerShape(10.dp))
					.padding(horizontal = 20.dp, vertical = 10.dp),
				text = "#Lorem Ipsum",
				fontSize = 13.sp,
				color = colorResource(R.color.primary_color))

			Text(
				modifier = Modifier
					.border(
						width = 1.dp,
						color = colorResource(R.color.primary_color),
						shape = RoundedCornerShape(10.dp))
					.padding(horizontal = 20.dp, vertical = 10.dp),
				text = "#dummy text of",
				fontSize = 12.sp,
				color = colorResource(R.color.primary_color))

			Text(
				modifier = Modifier
					.border(
						width = 1.dp,
						color = colorResource(R.color.primary_color),
						shape = RoundedCornerShape(10.dp))
					.padding(horizontal = 20.dp, vertical = 10.dp),
				text = "#Lorem",
				fontSize = 12.sp,
				color = colorResource(R.color.primary_color))

			Text(
				modifier = Modifier
					.border(
						width = 1.dp,
						color = colorResource(R.color.primary_color),
						shape = RoundedCornerShape(10.dp))
					.padding(horizontal = 20.dp, vertical = 10.dp),
				text = "#Lorem",
				fontSize = 12.sp,
				color = colorResource(R.color.primary_color))

			Text(
				modifier = Modifier
					.border(
						width = 1.dp,
						color = colorResource(R.color.primary_color),
						shape = RoundedCornerShape(10.dp))
					.padding(horizontal = 20.dp, vertical = 10.dp),
				text = "#The standard chunk of",
				fontSize = 12.sp,
				color = colorResource(R.color.primary_color))

			Text(
				modifier = Modifier
					.border(
						width = 1.dp,
						color = colorResource(R.color.primary_color),
						shape = RoundedCornerShape(10.dp))
					.padding(horizontal = 20.dp, vertical = 10.dp),
				text = "#Lorem",
				fontSize = 12.sp,
				color = colorResource(R.color.primary_color))
		}
	}
}