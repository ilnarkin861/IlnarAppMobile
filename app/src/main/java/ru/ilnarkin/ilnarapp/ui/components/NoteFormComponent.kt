package ru.ilnarkin.ilnarapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.DatePickerDefaults
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.models.Tag
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormComponent(
	note: Note? = null,
	noteTypes: List<NoteType>,
	archives: List<Archive>,
	tags: MutableList<Tag>,
	hasNextTags: Boolean = true,
	loadTags: suspend (count: Int) -> MutableList<Tag>,
	action: suspend (note: Note) -> Unit) {

	val selectableTags = tags

	var saving by remember { mutableStateOf(false) }
	var tagsLoading by remember { mutableStateOf(false) }
	val scope = rememberCoroutineScope()
	val interactionSource = remember { MutableInteractionSource() }

	var noteTypeMenuExpanded by remember { mutableStateOf(false) }

	val selectedNoteType = remember { mutableStateOf(note?.noteType ?: noteTypes[0]) }

	val noteTitle = remember { mutableStateOf(note?.title ?: "") }

	val noteText = remember { mutableStateOf(note?.text ?: "") }

	var isNoteTextError by remember { mutableStateOf(false) }

	val dateDialogState = rememberMaterialDialogState()
	var noteDate by remember {mutableStateOf(if (note != null) LocalDate.parse(note.date) else LocalDate.now())}
	val formattedDate = remember {
		derivedStateOf {
			DateTimeFormatter
				.ofPattern("dd.MM.yyyy")
				.format(noteDate)
		}
	}

	val unSelectedArchiveTitle = "Архив не выбран"
	var selectedArchiveTitle by remember { mutableStateOf(note?.archive?.title ?: unSelectedArchiveTitle) }
	var archiveMenuExpanded by remember { mutableStateOf(false) }
	var archiveIsSelected by remember { mutableStateOf(note?.archive ?: false) }
	var selectedArchive: Archive? by remember { mutableStateOf(note?.archive) }


	val addedTags = remember { note?.tags?.toMutableStateList() ?: mutableStateListOf()}

	val selectedTags = remember { mutableStateListOf<Tag>() }
	val selectedTagsCount = remember { mutableIntStateOf(0) }
	val uploadableTags = mutableListOf<Tag>()


	Column(Modifier
		.fillMaxSize()
		.padding(top = 30.dp)
		.verticalScroll(rememberScrollState())) {

		//Note type dropdown menu
		ExposedDropdownMenuBox(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 10.dp),
			expanded = noteTypeMenuExpanded,
			onExpandedChange = { noteTypeMenuExpanded = !noteTypeMenuExpanded }
		) {
			OutlinedTextField(
				modifier = Modifier
					.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				textStyle = TextStyle(
					fontFamily = getInterFont(),
					fontSize = 15.sp,
					),
				value = selectedNoteType.value.title,
				onValueChange = {selectedNoteType.value.title = it},
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
						painter = if (noteTypeMenuExpanded) painterResource(R.drawable.ic_arrow_down)
						else painterResource(R.drawable.ic_arrow_up),
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
							selectedNoteType.value = noteType
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
				.padding(bottom = 10.dp),
			textStyle = TextStyle(
				fontFamily = getInterFont(),
				fontSize = 15.sp,
			),
			value = noteTitle.value,
			singleLine = true,
			label = { Text("Заголовок") },
			onValueChange = {text -> noteTitle.value = text	},
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
				modifier = Modifier.padding(top = 5.dp, bottom = 10.dp),
				text = "Обязательное поле",
				color = colorResource(R.color.danger_color),
				fontFamily = getInterFont(),
				fontSize = 13.sp
			)
		}


		//Date field
		OutlinedTextField(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 10.dp, bottom = 20.dp),
			readOnly = true,
			enabled = false,
			value = formattedDate.value,
			onValueChange = {},
			colors = OutlinedTextFieldDefaults.colors(
				unfocusedBorderColor = colorResource(R.color.inputs_border_color),
				focusedBorderColor = colorResource(R.color.primary_color),
				focusedTextColor = colorResource(R.color.text_color),
				unfocusedTextColor = colorResource(R.color.text_color),
			),
			trailingIcon = {
				IconButton(onClick = { dateDialogState.show() }) {
					Icon(
						painter = painterResource(R.drawable.ic_calendar),
						contentDescription = "Выбрать дату",
						tint = colorResource(R.color.primary_color)
					)
				}
			},
			shape = RoundedCornerShape(10.dp)
		)

		MaterialDialog(
			dialogState = dateDialogState,
			buttons = {
				positiveButton(
					text = "Ок",
					textStyle = TextStyle(
						color = colorResource(R.color.primary_color),
						fontFamily = getInterFont(),
						fontWeight = FontWeight.Bold),
					onClick = { dateDialogState.hide()},
				)
				negativeButton(
					text = "Закрыть",
					textStyle = TextStyle(
						color = colorResource(R.color.primary_color),
						fontFamily = getInterFont(),
						fontWeight = FontWeight.Bold)
				)
			}
		) {
			datepicker(
				title = "Выбрать дату",
				colors = DatePickerDefaults.colors(
					headerBackgroundColor = colorResource(R.color.primary_color),
					dateActiveBackgroundColor = colorResource(R.color.primary_color)
				)
			) { noteDate = it }
		}


		// Archive dropdown
		ExposedDropdownMenuBox(
			modifier = Modifier
				.fillMaxWidth()
				.padding(bottom = 20.dp),
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
						painter = if (archiveMenuExpanded) painterResource(R.drawable.ic_arrow_down)
						else painterResource(R.drawable.ic_arrow_up),
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
						archiveIsSelected = false
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
							archiveIsSelected = true
							archiveMenuExpanded = false
						}
					)
				}
			}
		}


		//Selectable tags
		Column(
			Modifier
				.fillMaxWidth()
				.padding(top = 20.dp)
		) {
			Row(Modifier
				.fillMaxWidth()
				.padding(bottom = 20.dp)) {
				Text(
					color = Color.Gray,
					text = "Выбрать теги (${selectedTagsCount.intValue})",
					fontFamily = getInterFont(),
					fontSize = 15.sp,
					fontWeight = FontWeight.Bold
				)
			}
			selectableTags.forEachIndexed { index, tag ->
				Row(Modifier
					.fillMaxWidth()
					.padding(vertical = 15.dp)) {
					TagCheckboxComponent(tag, onChecked = {tag ->
						if (selectedTags.count() == 0){
							selectedTags.add(tag)
						}

						else{
							if (selectedTags.any{it.title == tag.title}){
								selectedTags.remove(tag)
							}

							else selectedTags.add(tag)
						}

						selectedTagsCount.intValue = selectedTags.count()
					})
				}

				if (index != selectableTags.count() -1){
					HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
				}
			}
		}

		if (hasNextTags){
			Row(Modifier
				.fillMaxWidth()
				.padding(top = 20.dp, bottom = 40.dp)) {

				if (tagsLoading){
					ProgressIndicatorComponent(25)
				}

				else{
					Text(
						modifier = Modifier.clickable(
							interactionSource = interactionSource,
							indication = null,
							onClick = {
								tagsLoading = true

								scope.launch {
									scope.async {
										val tags = loadTags(selectableTags.count() + 10)
										selectableTags.clear()
										selectableTags.addAll(tags)
									}.await()
								}.invokeOnCompletion { tagsLoading = false }
							}

						),
						color = colorResource(R.color.primary_color),
						text = "Загрузить еще",
						fontFamily = getInterFont(),
						fontSize = 15.sp,
						fontWeight = FontWeight.Bold
					)
				}
			}
		}

		if (!addedTags.isEmpty()){
			//Added tags
			Column(
				Modifier
					.fillMaxWidth()
					.padding(top = 20.dp)
			) {
				Row(Modifier
					.fillMaxWidth()
					.padding(bottom = 20.dp)) {
					Text(
						color = Color.Gray,
						text = "Добавленные теги",
						fontFamily = getInterFont(),
						fontSize = 15.sp,
						fontWeight = FontWeight.Bold
					)
				}
				addedTags.forEachIndexed {index, tag ->
					Row(
						modifier = Modifier.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically,
						horizontalArrangement = Arrangement.SpaceBetween) {

						Text(
							color = colorResource(R.color.text_color),
							text = tag.title,
							fontFamily = getInterFont(),
							fontSize = 16.sp,
						)

						IconButton(onClick = { addedTags.removeAt(index) }) {
							Icon(
								modifier = Modifier.size(20.dp),
								painter = painterResource(
									R.drawable.ic_trash), contentDescription = "",
								tint = colorResource(R.color.danger_color))
						}
					}

					if (index != addedTags.count() -1){
						HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
					}
				}
			}

		}


		//Save button
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 60.dp, bottom = 80.dp)
		) {
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp),
				enabled = !saving,
				shape = RoundedCornerShape(10.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = colorResource(R.color.primary_color),
					disabledContainerColor = colorResource(R.color.primary_color).copy(alpha = 0.8f)),
				onClick = {
					isNoteTextError = noteText.value.isEmpty()



					if(!isNoteTextError){
						saving = true

						uploadableTags.addAll(selectedTags)
						uploadableTags.addAll(addedTags)

						val note = Note(
							title = noteTitle.value,
							text = noteText.value,
							noteType = selectedNoteType.value,
							date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(noteDate),
							archive = selectedArchive,
							tags =  uploadableTags
						)

						scope.launch {
							scope.async {
								action(note) }.await()
						}.invokeOnCompletion { saving = false }
					}
				}
			) {
				if (saving){
					CircularProgressIndicator(
						modifier = Modifier.size(20.dp),
						strokeWidth = 2.dp,
						color = Color.White,
						trackColor = Color.Transparent,
					)
				}
				else{
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
}
