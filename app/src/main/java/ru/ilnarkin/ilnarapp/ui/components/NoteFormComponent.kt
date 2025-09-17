package ru.ilnarkin.ilnarapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.models.Tag
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormComponent() {

	val noteTypes = getNoteTypes()
	val archives = getArchives()
	val tags = getTags()

	var selectedNoteType by remember { mutableStateOf(noteTypes[0]) }
	var noteTypeMenuExpanded by remember { mutableStateOf(false) }

	var noteTitle = remember { mutableStateOf("") }

	var noteText = remember { mutableStateOf("") }
	var isNoteTextError by remember { mutableStateOf(false) }

	var noteDate by remember {mutableStateOf(LocalDate.now())}

	val formattedDate by remember {
		derivedStateOf {
			DateTimeFormatter
				.ofPattern("dd.MM.yyyy")
				.format(noteDate)
		}
	}

	val unSelectedArchiveTitle = "Архив не выбран"
	var selectedArchiveTitle by remember { mutableStateOf(unSelectedArchiveTitle) }
	var archiveSelected by remember { mutableStateOf(false) }
	var archiveMenuExpanded by remember { mutableStateOf(false) }
	var selectedArchive by remember { mutableStateOf(Archive(id = "", title = "")) }

	var addedTags = getAddedTags()
	var selectedTags = remember { mutableStateListOf<Tag>() }
	var selectedTagsCount = remember { mutableIntStateOf(0) }
	var uploadableTags = mutableListOf<Tag>()

	Column(Modifier.fillMaxSize()
		.padding(top = 30.dp, bottom = 50.dp)
		.verticalScroll(rememberScrollState())) {

		//Note type dropdown menu
		ExposedDropdownMenuBox(
			modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
			expanded = noteTypeMenuExpanded,
			onExpandedChange = {}
		) {
			OutlinedTextField(
				modifier = Modifier.fillMaxWidth()
					.menuAnchor(type = MenuAnchorType.PrimaryNotEditable),
				textStyle = TextStyle(
					fontFamily = getInterFont(),
					fontSize = 15.sp,
					),
				value = selectedNoteType.title,
				onValueChange = {},
				readOnly = true,
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedBorderColor = colorResource(R.color.inputs_border_color),
					focusedBorderColor = colorResource(R.color.primary_color),
					unfocusedLabelColor = colorResource(R.color.inputs_placeholder_color),
					focusedLabelColor = colorResource(R.color.primary_color),
					focusedTrailingIconColor = colorResource(R.color.primary_color),
					unfocusedTrailingIconColor = colorResource(R.color.primary_color),
					focusedTextColor = colorResource(R.color.text_color),
					unfocusedTextColor = colorResource(R.color.text_color)
				),
				shape = RoundedCornerShape(10.dp),
				trailingIcon = {
					Icon(
						painter = painterResource(R.drawable.ic_arrow_down),
						contentDescription = "")
				}
			)

			ExposedDropdownMenu(
				modifier = Modifier.background(Color.White),
				expanded = noteTypeMenuExpanded,
				onDismissRequest = { noteTypeMenuExpanded = false}
			) {
				noteTypes.forEach {noteType ->
					DropdownMenuItem(
						modifier = Modifier.background(Color.White),
						colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
						text = {
							Text(
								text = noteType.title,
								fontFamily = getInterFont(),
								fontSize = 15.sp
							)},
						onClick = {
							selectedNoteType = noteType
							noteTypeMenuExpanded = false
						}
					)
				}
			}
		}

		//Note title field
		OutlinedTextField(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 20.dp),
			textStyle = TextStyle(
				fontFamily = getInterFont(),
				fontSize = 15.sp,
			),
			value = noteTitle.value,
			singleLine = true,
			label = { Text("Заголовок") },
			onValueChange = {text -> noteTitle.value = text},
			colors = OutlinedTextFieldDefaults.colors(
				unfocusedBorderColor = colorResource(R.color.inputs_border_color),
				focusedBorderColor = colorResource(R.color.primary_color),
				unfocusedLabelColor = colorResource(R.color.inputs_placeholder_color),
				focusedLabelColor = colorResource(R.color.primary_color),
				focusedTextColor = colorResource(R.color.text_color),
				unfocusedTextColor = colorResource(R.color.text_color)
			),
			shape = RoundedCornerShape(10.dp))


		//Note text field
		OutlinedTextField(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 10.dp)
				.height(250.dp),
			textStyle = TextStyle(
				fontFamily = getInterFont(),
				fontSize = 15.sp,
			),
			value = noteText.value,
			minLines = 10,
			label = { Text("Текст") },
			isError = isNoteTextError,
			onValueChange = {text ->
				noteText.value = text
				isNoteTextError = noteText.value.isEmpty()
			},
			colors = OutlinedTextFieldDefaults.colors(
				unfocusedBorderColor = colorResource(R.color.inputs_border_color),
				focusedBorderColor = colorResource(R.color.primary_color),
				unfocusedLabelColor = colorResource(R.color.inputs_placeholder_color),
				focusedLabelColor = colorResource(R.color.primary_color),
				focusedTextColor = colorResource(R.color.text_color),
				unfocusedTextColor = colorResource(R.color.text_color),
				errorLabelColor = colorResource(R.color.danger_color),
				errorBorderColor = colorResource(R.color.danger_color)
			),
			shape = RoundedCornerShape(10.dp))

		if (isNoteTextError){
			Text(
				text = "Обязательное поле",
				color = colorResource(R.color.danger_color),
				fontFamily = getInterFont(),
				fontSize = 13.sp
			)
		}

		//Date field
		OutlinedTextField(
			modifier = Modifier.fillMaxWidth().padding(top = 10.dp, bottom = 28.dp),
			readOnly = true,
			enabled = false,
			value = formattedDate,
			onValueChange = {},
			colors = OutlinedTextFieldDefaults.colors(
				unfocusedBorderColor = colorResource(R.color.inputs_border_color),
				focusedBorderColor = colorResource(R.color.primary_color),
				focusedTextColor = colorResource(R.color.text_color),
				unfocusedTextColor = colorResource(R.color.text_color),
			),
			trailingIcon = {
				IconButton(onClick = {  }) {
					Icon(
						painter = painterResource(R.drawable.ic_calendar),
						contentDescription = "Выбрать дату",
						tint = colorResource(R.color.primary_color)
					)
				}
			},
			shape = RoundedCornerShape(10.dp)
		)


		// Archive dropdown
		ExposedDropdownMenuBox(
			modifier = Modifier.fillMaxWidth().padding(bottom = 20.dp),
			expanded = archiveMenuExpanded,
			onExpandedChange = { archiveMenuExpanded = !archiveMenuExpanded }
		){
			OutlinedTextField(
				modifier = Modifier
					.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				value = selectedArchiveTitle,
				onValueChange = {},
				readOnly = true,
				textStyle = TextStyle(
					fontFamily = getInterFont(),
					fontSize = 15.sp,
				),
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedBorderColor = colorResource(R.color.inputs_border_color),
					focusedBorderColor = colorResource(R.color.primary_color),
					unfocusedLabelColor = colorResource(R.color.inputs_placeholder_color),
					focusedLabelColor = colorResource(R.color.primary_color),
					focusedTrailingIconColor = colorResource(R.color.primary_color),
					unfocusedTrailingIconColor = colorResource(R.color.primary_color),
					focusedTextColor = colorResource(R.color.text_color),
					unfocusedTextColor = colorResource(R.color.text_color)
				),
				shape = RoundedCornerShape(10.dp),
				trailingIcon = {
					Icon(
						painter = painterResource(R.drawable.ic_arrow_down),
						contentDescription = "")
				}
			)
			ExposedDropdownMenu(
				modifier = Modifier.background(Color.White),
				expanded = archiveMenuExpanded,
				onDismissRequest = { archiveMenuExpanded = false}
			) {
				DropdownMenuItem(
					modifier = Modifier.background(Color.White),
					colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
					text = {
						Text(
							text = unSelectedArchiveTitle,
							fontFamily = getInterFont(),
							fontSize = 15.sp
							)},
					onClick = {
						selectedArchiveTitle = unSelectedArchiveTitle
						archiveSelected = false
						archiveMenuExpanded = false
					}
				)
				archives.forEach {archive ->
					DropdownMenuItem(
						modifier = Modifier.background(Color.White),
						colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
						text = {
							Text(
								text = archive.title,
								fontFamily = getInterFont(),
								fontSize = 15.sp
							)},
						onClick = {
							selectedArchive = archive
							selectedArchiveTitle = archive.title
							archiveSelected = true
							archiveMenuExpanded = false
						}
					)
				}
			}
		}


		//Selectable tags
		Column(
			Modifier.fillMaxWidth().padding(top = 20.dp)
		) {
			Row(Modifier.fillMaxWidth().padding(bottom = 20.dp)) {
				Text(
					color = colorResource(R.color.title_color),
					text = "Выбрать теги (${selectedTagsCount.intValue})",
					fontFamily = getInterFont(),
					fontSize = 18.sp,
					fontWeight = FontWeight.Bold
				)
			}
			tags.forEachIndexed {index, tag ->
				Row(Modifier.fillMaxWidth().padding(vertical = 15.dp)) {
					TagCheckboxComponent(tag, onChecked = {})
				}

				if (index != tags.count() -1){
					HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
				}
			}
		}

		Row(Modifier.fillMaxWidth().padding(top = 20.dp, bottom = 40.dp)) {
			Text(
				color = colorResource(R.color.primary_color),
				text = "Загрузить еще",
				fontFamily = getInterFont(),
				fontSize = 15.sp,
				fontWeight = FontWeight.Bold
			)
		}


		//Added tags
		Column(
			Modifier.fillMaxWidth().padding(top = 20.dp)
		) {
			Row(Modifier.fillMaxWidth()) {
				Text(
					color = colorResource(R.color.title_color),
					text = "Добавленные теги",
					fontFamily = getInterFont(),
					fontSize = 18.sp,
					fontWeight = FontWeight.Bold
				)
			}
			addedTags.forEachIndexed {index, tag ->
				Row(
					modifier = Modifier.fillMaxWidth()
						.padding(vertical = 15.dp),
					verticalAlignment = Alignment.CenterVertically,
					horizontalArrangement = Arrangement.SpaceBetween) {

					Text(
						color = colorResource(R.color.text_color),
						text = tag.title,
						fontFamily = getInterFont(),
						fontSize = 16.sp,
					)

					IconButton(onClick = {}) {
						Icon(
							modifier = Modifier.size(20.dp),
							painter = painterResource(
								R.drawable.ic_trash), contentDescription = "",
							tint = colorResource(R.color.danger_color))
					}

				}

				if (index != tags.count() -1){
					HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
				}
			}
		}

		//Save button
		Row(
			modifier = Modifier.fillMaxWidth().padding(top = 40.dp)
		) {
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp),
				shape = RoundedCornerShape(10.dp),
				colors = ButtonDefaults.buttonColors(containerColor = colorResource(R.color.primary_color)),
				onClick = {	}
			) {
				Text(
					text = "Сохранить",
					fontFamily = getInterFont(),
					fontSize = 16.sp,
					fontWeight = FontWeight.SemiBold
				)
			}
		}
	}
}

fun getNoteTypes(): List<NoteType>{
	val noteTypes = mutableListOf<NoteType>()

	noteTypes.add(NoteType(id = "", title = "Событие"))
	noteTypes.add(NoteType(id = "", title = "Заметка"))

	return noteTypes
}

fun getArchives(): List<Archive>{
	val archives = mutableListOf<Archive>()

	for (i in 1..5){
		archives.add(Archive(id = "", title = "Архив ${i}"))
	}

	return archives
}

fun getTags(): MutableList<Tag>{
	val tags = mutableListOf<Tag>()

	for (i in 1..10){
		tags.add(Tag(id = "", title = "Тег ${i}"))
	}

	return tags
}

fun getAddedTags(): MutableList<Tag>{
	val tags = mutableListOf<Tag>()

	for (i in 1..5){
		tags.add(Tag(id = "", title = "Добавленный тег ${i}"))
	}

	return tags
}
