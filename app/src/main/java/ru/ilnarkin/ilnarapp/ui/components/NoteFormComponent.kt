package ru.ilnarkin.ilnarapp.ui.components

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.ArchiveViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteTypeViewModel
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFormComponent(
	note: Note? = null,
	noteTypeViewModel: NoteTypeViewModel = koinViewModel(),
	archiveViewModel: ArchiveViewModel = koinViewModel(),
	tagViewModel: TagViewModel = koinViewModel(),
	action: suspend (note: Note) -> Unit,
	close: () -> Unit)
{
	val tagsLimit = 10
	val scope = rememberCoroutineScope()
	var saving by rememberSaveable { mutableStateOf(false) }
	var loading by rememberSaveable { mutableStateOf(true) }
	val selectableTags = remember { mutableStateListOf<Tag>() }
	val noteTypeViewModelState by noteTypeViewModel.uiState.collectAsState()
	val archiveViewModelState by archiveViewModel.uiState.collectAsState()
	val tagViewModelState by tagViewModel.uiState.collectAsState()
	var noteTypeMenuExpanded by rememberSaveable { mutableStateOf(false) }
	var selectedNoteType by remember { mutableStateOf(note?.noteType) }
	val selectedNoteTypeTitle = rememberSaveable { mutableStateOf(note?.noteType?.title ?: "") }
	val noteTitle = rememberSaveable { mutableStateOf(note?.title ?: "") }
	val noteText = rememberSaveable { mutableStateOf(note?.text ?: "") }
	var isNoteTextError by rememberSaveable { mutableStateOf(false) }

	var noteDate by remember {mutableStateOf(if (note != null) LocalDate.parse(note.date) else LocalDate.now())}
	val formattedDate = remember {
		derivedStateOf {
			DateTimeFormatter
				.ofPattern("dd.MM.yyyy")
				.format(noteDate)
		}
	}

	val visibleDate = rememberSaveable { mutableStateOf(formattedDate.value) }

	var datePickerVisible by rememberSaveable { mutableStateOf(false) }
	val datePickerState = rememberDatePickerState()
	val unSelectedArchiveTitle = "Архив не выбран"
	var selectedArchiveTitle by rememberSaveable { mutableStateOf(note?.archive?.title ?: unSelectedArchiveTitle) }
	var archiveMenuExpanded by rememberSaveable { mutableStateOf(false) }
	var archiveIsSelected by remember { mutableStateOf(note?.archive != null) }
	var selectedArchive: Archive? by remember { mutableStateOf(note?.archive) }
	var tagsLoading by rememberSaveable { mutableStateOf(false) }
	val addedTags = remember { note?.tags?.toMutableStateList() ?: mutableStateListOf()}
	val selectedTags = remember { mutableStateListOf<Tag>() }
	val uploadableTags = mutableListOf<Tag>()

	val inputColors = OutlinedTextFieldDefaults.colors(
		unfocusedBorderColor = AppTheme.colors.inputsBorderColor,
		focusedBorderColor = AppTheme.colors.primaryColor,
		unfocusedLabelColor = AppTheme.colors.inputsPlaceholderColor,
		focusedLabelColor = AppTheme.colors.primaryColor,
		focusedTrailingIconColor = AppTheme.colors.primaryColor,
		unfocusedTrailingIconColor = AppTheme.colors.primaryColor,
		focusedTextColor = AppTheme.colors.textColor,
		unfocusedTextColor = AppTheme.colors.textColor
	)


	LaunchedEffect(Unit) {

		try {
			if (noteTypeViewModelState.list.isEmpty()){
				noteTypeViewModel.getNoteTypesList(0, 10)
			}

			if (archiveViewModelState.list.isEmpty()){
				archiveViewModel.getArchivesList(0, 100)
			}

			if (tagViewModelState.list.isEmpty()){
				tagViewModel.getTagsList(0, tagsLimit)
			}

			if (note == null && noteTypeViewModelState.list.isNotEmpty()){
				selectedNoteType = noteTypeViewModelState.list[0]
			}

			if (selectedNoteTypeTitle.value.isEmpty()){
				selectedNoteTypeTitle.value = noteTypeViewModelState.list[0].title
			}

			loading = false
		}

		catch (_: Exception){}
	}


	if (loading){
		Box(
			modifier = Modifier
				.background(AppTheme.colors.appBgColor)
				.fillMaxSize(),
			contentAlignment = Alignment.Center)
		{
			ProgressIndicatorComponent(60, AppTheme.colors.primaryColor)
		}
	}


	else{
		Column(
			modifier = Modifier
				.verticalScroll(rememberScrollState())
				.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding)
				.background(AppTheme.colors.appBgColor)
				.fillMaxSize())
		{
			BackHandler {
				if (!saving){
					close()
					tagViewModel.clearSelectedTags()
				}
			}

			//Note type dropdown menu
			ExposedDropdownMenuBox(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 35.dp, bottom = 10.dp),
				expanded = noteTypeMenuExpanded,
				onExpandedChange = { noteTypeMenuExpanded = !noteTypeMenuExpanded })
			{
				selectedNoteType?.let { it ->
					OutlinedTextField(
						modifier = Modifier
							.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
							.fillMaxWidth(),
						textStyle = AppTheme.typography.formInputText,
						value = selectedNoteTypeTitle.value,
						onValueChange = { selectedNoteTypeTitle.value = it },
						readOnly = true,
						colors = inputColors,
						shape = RoundedCornerShape(10.dp),
						trailingIcon = {
							Icon(
								painter = if (noteTypeMenuExpanded) painterResource(R.drawable.ic_arrow_down)
								else painterResource(R.drawable.ic_arrow_up),
								contentDescription = "")
						}
					)
				}
				ExposedDropdownMenu(
					modifier = Modifier.background(Color.White),
					expanded = noteTypeMenuExpanded,
					onDismissRequest = { noteTypeMenuExpanded = false})
				{
					noteTypeViewModelState.list.forEach {noteType ->
						DropdownMenuItem(
							modifier = Modifier.background(Color.White),
							colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
							text = {
								Text(
									text = noteType.title,
									style = AppTheme.typography.formInputText
								)},
							onClick = {
								selectedNoteType = noteType
								selectedNoteTypeTitle.value = noteType.title
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
				textStyle = AppTheme.typography.formInputText,
				value = noteTitle.value,
				singleLine = true,
				label = { Text("Заголовок") },
				onValueChange = {text -> noteTitle.value = text	},
				colors = inputColors,
				shape = RoundedCornerShape(10.dp))

			//Note text field
			OutlinedTextField(
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 5.dp)
					.height(250.dp),
				textStyle = AppTheme.typography.formInputText,
				value = noteText.value,
				minLines = 10,
				label = { Text("Текст") },
				isError = isNoteTextError,
				onValueChange = {text ->
					noteText.value = text
					isNoteTextError = noteText.value.isEmpty()
				},
				colors = inputColors,
				shape = RoundedCornerShape(10.dp))

			if (isNoteTextError){
				Text(
					modifier = Modifier.padding(bottom = 10.dp),
					text = "Обязательное поле",
					color = AppTheme.colors.dangerColor,
					style = AppTheme.typography.errorText
				)
			}

			// Date field
			OutlinedTextField(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 15.dp, bottom = 20.dp),
				readOnly = true,
				enabled = true,
				value = visibleDate.value,
				onValueChange = {},
				colors = OutlinedTextFieldDefaults.colors(
					unfocusedBorderColor = AppTheme.colors.borderColor,
					focusedBorderColor = AppTheme.colors.primaryColor,
					focusedTextColor = AppTheme.colors.textColor,
					unfocusedTextColor = AppTheme.colors.textColor,
				),
				trailingIcon = {
					IconButton(onClick = { datePickerVisible = true })
					{
						Icon(
							modifier = Modifier.size(30.dp),
							painter = painterResource(R.drawable.ic_calendar),
							contentDescription = "Выбрать дату",
							tint = AppTheme.colors.primaryColor
						)
					}
				},
				shape = RoundedCornerShape(10.dp)
			)

			// Archive dropdown
			ExposedDropdownMenuBox(
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 20.dp),
				expanded = archiveMenuExpanded,
				onExpandedChange = { archiveMenuExpanded = !archiveMenuExpanded })
			{
				OutlinedTextField(
					modifier = Modifier
						.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
						.fillMaxWidth(),
					value = selectedArchiveTitle,
					onValueChange = {},
					readOnly = true,
					textStyle = AppTheme.typography.formInputText,
					colors = inputColors,
					shape = RoundedCornerShape(10.dp),
					trailingIcon = {
						Icon(
							painter = if (archiveMenuExpanded) painterResource(R.drawable.ic_arrow_down)
							else painterResource(R.drawable.ic_arrow_up),
							contentDescription = "")
					})
				ExposedDropdownMenu(
					modifier = Modifier.background(Color.White),
					expanded = archiveMenuExpanded,
					onDismissRequest = { archiveMenuExpanded = false})
				{
					DropdownMenuItem(
						modifier = Modifier.background(Color.White),
						colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
						text = {
							Text(
								text = unSelectedArchiveTitle,
								style = AppTheme.typography.formInputText)
						},
						onClick = {
							selectedArchiveTitle = unSelectedArchiveTitle
							archiveIsSelected = false
							archiveMenuExpanded = false
						}
					)

					archiveViewModelState.list.forEach {archive ->
						DropdownMenuItem(
							modifier = Modifier.background(Color.White),
							colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
							text = {
								Text(
									text = archive.title,
									style = AppTheme.typography.formInputText)
							},
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
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 20.dp))
			{
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 20.dp))
				{
					Text(
						text = "Выбрать теги (${selectedTags.count()})",
						color = AppTheme.colors.colorGrey,
						style = AppTheme.typography.formInputText.copy(fontWeight = FontWeight.Bold))
				}

				tagViewModelState.list.forEachIndexed { index, tag ->
					Row(Modifier.fillMaxWidth())
					{
						TagCheckboxComponent(
							tag,
							isChecked = tagViewModel.getSelectedTags().find { it.id == tag.id } != null,
							onChecked = {tag ->
								tagViewModel.selectTag(tag)
							})
					}

					if (index != selectableTags.count() -1){
						HorizontalDivider(
							thickness = 1.dp,
							color = AppTheme.colors.borderColor)
					}
				}
			}

			tagViewModelState.pagination?.let {
				if (it.hasNextPage){
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 20.dp, bottom = 40.dp))
					{
						if (tagsLoading){
							ProgressIndicatorComponent(25, AppTheme.colors.primaryColor)
						}

						else{
							Text(
								modifier = Modifier.clickable(
									interactionSource = remember { MutableInteractionSource() },
									indication = null,
									onClick = {
										scope.launch {
											tagsLoading = true
											try {
												val tags = tagViewModel.getTagsList(tagViewModelState.offset + tagsLimit, tagsLimit)

												selectableTags.addAll(tags)
											} finally {
												tagsLoading = false
											}
										}
									}),
								text = "Загрузить еще",
								color = AppTheme.colors.primaryColor,
								style = AppTheme.typography.textButton
							)
						}
					}
				}
			}

			//Added tags
			if (!addedTags.isEmpty()){
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 20.dp))
				{
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(bottom = 20.dp))
					{
						Text(
							text = "Добавленные теги",
							color = AppTheme.colors.colorGrey,
							style = AppTheme.typography.formInputText.copy(fontWeight = FontWeight.Bold))
					}

					addedTags.forEachIndexed {index, tag ->
						Row(
							modifier = Modifier.fillMaxWidth(),
							verticalAlignment = Alignment.CenterVertically,
							horizontalArrangement = Arrangement.SpaceBetween)
						{

							Row(
								modifier = Modifier
									.fillMaxWidth()
									.weight(0.9f))
							{
								Text(
									text = tag.title,
									color = AppTheme.colors.textColor,
									style = AppTheme.typography.formInputText)
							}

							Row(
								modifier = Modifier
									.fillMaxWidth()
									.weight(0.1f))
							{
								IconButton(onClick = { addedTags.removeAt(index) })
								{
									Icon(
										modifier = Modifier.size(20.dp),
										painter = painterResource(R.drawable.ic_trash),
										contentDescription = "",
										tint = AppTheme.colors.dangerColor)
								}
							}
						}

						if (index != addedTags.count() -1){
							HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
						}
					}
				}
			}


			//Save button
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 60.dp))
			{
				Button(
					modifier = Modifier
						.fillMaxWidth()
						.height(60.dp),
					enabled = !saving,
					shape = RoundedCornerShape(10.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = AppTheme.colors.primaryColor,
						disabledContainerColor = AppTheme.colors.primaryColor.copy(alpha = 0.8f)),
					onClick = {
						isNoteTextError = noteText.value.isEmpty()

						if(!isNoteTextError){
							saving = true

							uploadableTags.addAll(tagViewModel.getSelectedTags())
							uploadableTags.addAll(addedTags)

							val updatableNote = Note(
								title = noteTitle.value,
								text = noteText.value,
								noteType = selectedNoteType!!,
								date = DateTimeFormatter.ofPattern("yyyy-MM-dd").format(noteDate),
								archive = selectedArchive,
								tags =  uploadableTags
							)

							if(note != null){
								updatableNote.id = note.id
							}

							scope.launch {
								saving = true
								try {
									action(updatableNote)
								} finally {
									tagViewModel.clearSelectedTags()
									saving = false
								}
							}
						}
					})
				{
					if (saving){
						CircularProgressIndicator(
							modifier = Modifier.size(20.dp),
							strokeWidth = 2.dp,
							color = Color.White,
							trackColor = Color.Transparent)
					}
					else{
						Text(
							text = "Сохранить",
							style = AppTheme.typography.inputButtonText
						)
					}
				}
			}


			if (!saving){
				Row(
					modifier = Modifier
						.fillMaxWidth()
						.padding(top = 15.dp),
					horizontalArrangement = Arrangement.Center)
				{
					Text(
						modifier = Modifier.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = {
								tagViewModel.clearSelectedTags()
								close()
							}),
						text = "Закрыть",
						color = AppTheme.colors.colorGrey,
						style = AppTheme.typography.textButton)
				}
			}

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 80.dp)) {  }
		}
	}


	AlertComponent(
		success = noteTypeViewModelState.success,
		message = noteTypeViewModelState.message,
		visible = noteTypeViewModelState.showAlert,
		action = {
			noteTypeViewModel.dismissAlert()
			close()
		}
	)


	// Date picker
	if (datePickerVisible) {
		DatePickerDialog(
			onDismissRequest = { datePickerVisible = false },
			colors = DatePickerDefaults.colors(
				containerColor = AppTheme.colors.primaryColor
			),
			confirmButton = {
				TextButton(
					onClick = {
						datePickerState.selectedDateMillis?.let { millis ->
							val date = Instant.ofEpochMilli(millis)
								.atZone(ZoneId.systemDefault())
								.toLocalDate()
							noteDate = date
						}

						visibleDate.value = formattedDate.value

						datePickerVisible = false
					})
				{
					Text(text = "Ок", color = Color.White)
				}
			},
			dismissButton = {
				TextButton(onClick = { datePickerVisible = false })
				{
					Text(text = "Закрыть", color = Color.White)
				}
			})
		{
			DatePicker(
				state = datePickerState,
				title = {
					Text(
						modifier = Modifier.padding(start = 24.dp, top = 16.dp),
						text = "Выбрать дату")
				},
				colors = DatePickerDefaults.colors(
					containerColor = Color.White,
					selectedDayContainerColor = AppTheme.colors.primaryColor,
					todayContentColor = AppTheme.colors.primaryColor,
					selectedYearContainerColor = AppTheme.colors.primaryColor,
					todayDateBorderColor = AppTheme.colors.primaryColor,
					titleContentColor = AppTheme.colors.primaryColor,
					headlineContentColor = AppTheme.colors.titleColor
				)
			)
		}
	}
}