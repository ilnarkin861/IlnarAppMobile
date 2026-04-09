@file:OptIn(ExperimentalMaterial3Api::class)

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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.ArchiveViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteFilterViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteTypeViewModel
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFilterFormComponent(
	noteTypeViewModel: NoteTypeViewModel = koinViewModel(),
	archiveViewModel: ArchiveViewModel = koinViewModel(),
	tagViewModel: TagViewModel = koinViewModel(),
	noteFilterViewModel: NoteFilterViewModel,
	action: () -> Unit,
	reset: () -> Unit,
	close: () -> Unit)
{
	val tagsLimit = 10
	var loading by rememberSaveable { mutableStateOf(true) }
	val scope = rememberCoroutineScope()
	val noteTypeViewModelState by noteTypeViewModel.uiState.collectAsState()
	val tagViewModelState by tagViewModel.uiState.collectAsState()
	val filterViewModelState by noteFilterViewModel.uiState.collectAsState()
	var tagsLoading by remember { mutableStateOf(false) }
	var yearsMenuExpanded by remember { mutableStateOf(false) }
	var monthMenuExpanded by remember { mutableStateOf(false) }
	var noteTypeMenuExpanded by remember { mutableStateOf(false) }
	var archiveMenuExpanded by remember { mutableStateOf(false) }

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
		if (filterViewModelState.selectableNoteTypes.isEmpty()){
			val noteTypes = noteTypeViewModel.getNoteTypesList(0, 100)
			noteFilterViewModel.addNoteTypes(noteTypes)
		}

		if (filterViewModelState.selectableArchives.isEmpty()){
			val archives = archiveViewModel.getArchivesList(0, 100)
			noteFilterViewModel.addArchives(archives)
		}

		if (filterViewModelState.selectableTags.isEmpty()){
			val tags = tagViewModel.getTagsList(0, tagsLimit)
			val hasNextTags = tagViewModel.uiState.value.pagination?.hasNextPage ?: false
			noteFilterViewModel.addTags(tags, hasNextTags)
		}

		loading = false
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
				close()
			}

			//Note type dropdown menu
			ExposedDropdownMenuBox(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 25.dp, bottom = 20.dp),
				expanded = noteTypeMenuExpanded,
				onExpandedChange = { noteTypeMenuExpanded = !noteTypeMenuExpanded })
			{
				OutlinedTextField(
					modifier = Modifier
						.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
						.fillMaxWidth(),
					textStyle = AppTheme.typography.formInputText,
					value = filterViewModelState.selectedNoteTypeTitle,
					onValueChange = {},
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
				ExposedDropdownMenu(
					modifier = Modifier.background(Color.White),
					expanded = noteTypeMenuExpanded,
					onDismissRequest = { noteTypeMenuExpanded = false })
				{
					filterViewModelState.selectableNoteTypes.forEach {noteType ->
						DropdownMenuItem(
							modifier = Modifier.background(Color.White),
							colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
							text = {
								Text(
									text = noteType.title,
									style = AppTheme.typography.formInputText
								)},
							onClick = {
								noteFilterViewModel.selectNoteType(noteType.id, noteType.title)
								noteTypeMenuExpanded = false
							}
						)
					}
				}
			}


			// Year dropdown
			ExposedDropdownMenuBox(
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 20.dp),
				expanded = yearsMenuExpanded,
				onExpandedChange = { yearsMenuExpanded = !yearsMenuExpanded })
			{
				OutlinedTextField(
					modifier = Modifier
						.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
						.fillMaxWidth(),
					value = filterViewModelState.selectedYearTitle,
					onValueChange = {},
					readOnly = true,
					textStyle = AppTheme.typography.formInputText,
					colors = inputColors,
					shape = RoundedCornerShape(10.dp),
					trailingIcon = {
						Icon(
							painter = if (yearsMenuExpanded) painterResource(R.drawable.ic_arrow_down)
							else painterResource(R.drawable.ic_arrow_up),
							contentDescription = "")
					}
				)
				ExposedDropdownMenu(
					modifier = Modifier.background(Color.White),
					expanded = yearsMenuExpanded,
					onDismissRequest = { yearsMenuExpanded = false })
				{
					DropdownMenuItem(
						modifier = Modifier.background(Color.White),
						colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
						text = {
							Text(
								text = filterViewModelState.unSelectedYearTitle,
								style = AppTheme.typography.formInputText
							)},
						onClick = {
							noteFilterViewModel.selectYear(null, filterViewModelState.unSelectedYearTitle)
							yearsMenuExpanded = false
						}
					)
					filterViewModelState.years.forEach {year ->
						DropdownMenuItem(
							modifier = Modifier.background(Color.White),
							colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
							text = {
								Text(
									text = year.toString(),
									style = AppTheme.typography.formInputText
								)},
							onClick = {
								noteFilterViewModel.selectYear(year, year.toString())
								yearsMenuExpanded = false
							}
						)
					}
				}
			}



			// Month dropdown
			if (filterViewModelState.yearSelected){
				ExposedDropdownMenuBox(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 20.dp),
					expanded = monthMenuExpanded,
					onExpandedChange = { monthMenuExpanded = !monthMenuExpanded })
				{
					OutlinedTextField(
						modifier = Modifier
							.menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable)
							.fillMaxWidth(),
						value = filterViewModelState.selectedMonthTitle,
						onValueChange = {},
						readOnly = true,
						textStyle = AppTheme.typography.formInputText,
						colors = inputColors,
						shape = RoundedCornerShape(10.dp),
						trailingIcon = {
							Icon(
								painter = if (monthMenuExpanded) painterResource(R.drawable.ic_arrow_down)
								else painterResource(R.drawable.ic_arrow_up),
								contentDescription = "")
						}
					)
					ExposedDropdownMenu(
						modifier = Modifier.background(Color.White),
						expanded = monthMenuExpanded,
						onDismissRequest = { monthMenuExpanded = false })
					{
						DropdownMenuItem(
							modifier = Modifier.background(Color.White),
							colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
							text = {
								Text(
									text = filterViewModelState.unSelectedMonthTitle,
									style = AppTheme.typography.formInputText)
							},
							onClick = {
								noteFilterViewModel.selectMonth(null, filterViewModelState.unSelectedMonthTitle)
								monthMenuExpanded = false
							}
						)

						filterViewModelState.months.forEachIndexed {index, month ->
							DropdownMenuItem(
								modifier = Modifier.background(Color.White),
								colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
								text = {
									Text(
										text = month,
										style = AppTheme.typography.formInputText)
								},
								onClick = {
									noteFilterViewModel.selectMonth(index + 1, month)
									monthMenuExpanded = false
								}
							)
						}
					}
				}
			}


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
					value = filterViewModelState.selectedArchiveTitle,
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
					}
				)
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
								text = filterViewModelState.unSelectedArchiveTitle,
								style = AppTheme.typography.formInputText)
						},
						onClick = {
							noteFilterViewModel.selectArchive(null, filterViewModelState.unSelectedArchiveTitle)
							archiveMenuExpanded = false
						}
					)

					filterViewModelState.selectableArchives.forEach {archive ->
						DropdownMenuItem(
							modifier = Modifier.background(Color.White),
							colors = MenuDefaults.itemColors(textColor = AppTheme.colors.textColor),
							text = {
								Text(
									text = archive.title,
									style = AppTheme.typography.formInputText)
							},
							onClick = {
								noteFilterViewModel.selectArchive(archive.id, archive.title)
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
				filterViewModelState.selectableTags.forEachIndexed { index, tag ->
					Row(modifier = Modifier.fillMaxWidth())
					{
						TagCheckboxComponent(
							tag,
							isChecked = filterViewModelState.selectedTagIds.find { it == tag.id } != null,
							onChecked = {tag ->

								val existingTag = filterViewModelState.selectedTagIds.find { it == tag.id }

								if (existingTag == null){
									noteFilterViewModel.addTag(tag.id)
								}

								else{
									noteFilterViewModel.removeTag(tag.id)
								}
							})
					}

					if (index != filterViewModelState.selectableTags.count() -1){
						HorizontalDivider(
							thickness = 1.dp,
							color = AppTheme.colors.borderColor)
					}
				}
			}


			if (filterViewModelState.hasNextTags){
				Row(
					modifier =  Modifier
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

											val hasNextTags = tagViewModel.uiState.value.pagination?.hasNextPage ?: false

											noteFilterViewModel.addTags(tags, hasNextTags)

										} finally {
											tagsLoading = false
										}
									}
								}

							),
							color = AppTheme.colors.primaryColor,
							text = "Загрузить еще",
							style = AppTheme.typography.textButton
						)
					}
				}
			}


			// Filter button
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 60.dp))
			{
				Button(
					modifier = Modifier
						.fillMaxWidth()
						.height(60.dp),
					shape = RoundedCornerShape(10.dp),
					colors = ButtonDefaults.buttonColors(
						containerColor = AppTheme.colors.primaryColor,
						disabledContainerColor = AppTheme.colors.primaryColor.copy(alpha = 0.8f)),
					onClick = {
						noteFilterViewModel.applyFilter()

						action()
					})
				{
					Text(
						text = "Применить",
						style = AppTheme.typography.inputButtonText
					)
				}
			}


			// Close button
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 30.dp, bottom = 20.dp),
				horizontalArrangement = Arrangement.Center)
			{
				Text(
					modifier = Modifier.clickable(
						interactionSource = remember { MutableInteractionSource() },
						indication = null,
						onClick = {
							close()
						}
					),
					text = "Закрыть",
					color = AppTheme.colors.colorGrey,
					style = AppTheme.typography.textButton
				)
			}


			// Reset button
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 80.dp),
				horizontalArrangement = Arrangement.Center)
			{
				if (filterViewModelState.filterApplied){
					Text(
						modifier = Modifier.clickable(
							interactionSource = remember { MutableInteractionSource() },
							indication = null,
							onClick = {
								noteFilterViewModel.resetFilter()
								reset()
							}
						),
						text = "Сбросить фильтр",
						color = AppTheme.colors.primaryColor,
						style = AppTheme.typography.textButton
					)
				}
			}
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
}