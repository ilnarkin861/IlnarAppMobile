package ru.ilnarkin.ilnarapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Note
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteItemComponent(
	note: Note,
	viewAction: (note: Note) -> Unit,
	editAction: (note: Note) -> Unit,
	deleteAction: suspend (note: Note) -> Unit
	) {

	var deleting by remember { mutableStateOf(false) }
	val scope = rememberCoroutineScope()
	var showConfirmAlert by remember { mutableStateOf(false) }
	val font = getInterFont()

	Box(
		Modifier.fillMaxSize()
			.defaultMinSize(minHeight = 150.dp)
			.padding(bottom = 15.dp)
			.clip(RoundedCornerShape(10.dp))
			.background(color = Color.White)

	) {


		Column (Modifier.padding(
			start = 10.dp,
			top = 15.dp,
			end = 15.dp,
			bottom = 20.dp
		)){
			Row {
				Text(
					text = note.title,
					fontFamily = font,
					fontWeight = FontWeight.SemiBold,
					color = colorResource(R.color.title_color),
					fontSize = dimensionResource(R.dimen.note_item_title_font_size).value.sp
				)
			}

			Row(Modifier.padding(top = 5.dp)) {
				Text(
					text = DateTimeFormatter
						.ofPattern("dd.MM.yyyy")
						.format(LocalDate.parse(note.date)),
					fontFamily = font,
					color = colorResource(R.color.grey),
					fontSize = dimensionResource(R.dimen.note_item_date_font_size).value.sp
				)
			}

			Row(Modifier.padding(vertical = 15.dp)) {
				HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
			}

			Row {
				Text(
					text = note.text,
					maxLines = 3,
					fontFamily = font,
					lineHeight = 1.5.em,
					color = colorResource(R.color.text_color),
					fontSize = dimensionResource(R.dimen.note_item_text_font_size).value.sp
				)
			}

			Row(
				Modifier.padding(top = 30.dp).fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically,
				horizontalArrangement = Arrangement.SpaceBetween
				) {
				Row (
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.Center
					) {
					Button(
						modifier = Modifier
							.size(width = 140.dp, height = 40.dp),
						colors = ButtonDefaults
							.buttonColors(containerColor = colorResource(R.color.primary_color)),
						shape = RoundedCornerShape(10.dp),
						onClick = { viewAction(note) }) {
						Text(
							text = "Подробнее",
							color = Color.White,
							fontFamily = font,
							fontWeight = FontWeight.SemiBold,
							fontSize = 12.sp)
					}
				}

				Row(verticalAlignment = Alignment.CenterVertically){
					IconButton(onClick = {
						editAction(note)
					}) {
						Icon(modifier = Modifier.size(22.dp),
							painter = painterResource(R.drawable.ic_edit), contentDescription = "",
							tint = colorResource(R.color.primary_color))
					}

					if (deleting){
						Row(Modifier.padding(start = 20.dp, end = 10.dp)) {
							ProgressIndicatorComponent(25, colorResource(R.color.danger_color))
						}
					}

					else{
						IconButton(onClick = {
							showConfirmAlert = true
						}) {
							Icon(
								modifier = Modifier.size(22.dp),
								painter = painterResource(R.drawable.ic_trash), contentDescription = "",
								tint = colorResource(R.color.danger_color))
						}
					}
				}
			}
		}
	}

	ConfirmComponent(
		showed = showConfirmAlert,
		action = {confirmed ->

			if (confirmed){
				deleting = true

				scope.launch {
					scope.async {
						deleteAction(note)
					}.await()
				}.invokeOnCompletion{ deleting = false }

			}

			showConfirmAlert = false
		}
	)
}