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
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.viewModels.NoteFilterViewModel


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NoteFilterFormComponent(
	viewModel: NoteFilterViewModel,
	loadTags: suspend () -> MutableList<Tag>,
	action: suspend () -> Unit,
	resetFilter: () -> Unit
) {
	val font = getInterFont()

	val scope = rememberCoroutineScope()

	val viewModelState by viewModel.uiState.collectAsState()

	var tagsLoading by remember { mutableStateOf(false) }

	var filtering by remember { mutableStateOf(false) }

	var yearsMenuExpanded by remember { mutableStateOf(false) }

	var monthMenuExpanded by remember { mutableStateOf(false) }

	var noteTypeMenuExpanded by remember { mutableStateOf(false) }

	var archiveMenuExpanded by remember { mutableStateOf(false) }



	Column(Modifier
		.fillMaxSize()
		.padding(top = 30.dp)
		.verticalScroll(rememberScrollState())) {


		//Note type dropdown menu
		ExposedDropdownMenuBox(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),
					bottom = 20.dp),
			expanded = noteTypeMenuExpanded,
			onExpandedChange = { noteTypeMenuExpanded = !noteTypeMenuExpanded }
		) {
			OutlinedTextField(
				modifier = Modifier
					.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				textStyle = TextStyle(
					fontFamily = font,
					fontSize = 15.sp,
				),
				value = viewModelState.selectedNoteTypeTitle,
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
				viewModelState.selectableNoteTypes.forEach {noteType ->
					DropdownMenuItem(
						modifier = Modifier.background(Color.White),
						colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
						text = {
							Text(
								text = noteType.title,
								fontFamily = font,
								fontSize = 15.sp
							)},
						onClick = {
							viewModel.selectNoteType(noteType.id, noteType.title)
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
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),
					bottom = 20.dp),
			expanded = yearsMenuExpanded,
			onExpandedChange = { yearsMenuExpanded = !yearsMenuExpanded }
		){
			OutlinedTextField(
				modifier = Modifier
					.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				value = viewModelState.selectedYearTitle,
				onValueChange = {},
				readOnly = true,
				textStyle = TextStyle(
					fontFamily = font,
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
						painter = if (yearsMenuExpanded) painterResource(R.drawable.ic_arrow_down)
						else painterResource(R.drawable.ic_arrow_up),
						contentDescription = "")
				}
			)

			ExposedDropdownMenu(
				modifier = Modifier.background(Color.White),
				expanded = yearsMenuExpanded,
				onDismissRequest = { yearsMenuExpanded = false}
			) {
				DropdownMenuItem(
					modifier = Modifier.background(Color.White),
					colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
					text = {
						Text(
							text = viewModelState.unSelectedYearTitle,
							fontFamily = font,
							fontSize = 15.sp
						)},
					onClick = {
						viewModel.selectYear(null, viewModelState.unSelectedYearTitle)
						yearsMenuExpanded = false
					}
				)
				viewModelState.years.forEach {year ->
					DropdownMenuItem(
						modifier = Modifier.background(Color.White),
						colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
						text = {
							Text(
								text = year.toString(),
								fontFamily = font,
								fontSize = 15.sp
							)},
						onClick = {
							viewModel.selectYear(year, year.toString())
							yearsMenuExpanded = false
						}
					)
				}
			}
		}


		// Month dropdown
		if (viewModelState.yearSelected){
			ExposedDropdownMenuBox(
				modifier = Modifier
					.fillMaxWidth()
					.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
						end = dimensionResource(R.dimen.container_horizontal_padding),bottom = 20.dp),
				expanded = monthMenuExpanded,
				onExpandedChange = { monthMenuExpanded = !monthMenuExpanded }
			){
				OutlinedTextField(
					modifier = Modifier
						.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
						.fillMaxWidth(),
					value = viewModelState.selectedMonthTitle,
					onValueChange = {},
					readOnly = true,
					textStyle = TextStyle(
						fontFamily = font,
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
							painter = if (monthMenuExpanded) painterResource(R.drawable.ic_arrow_down)
							else painterResource(R.drawable.ic_arrow_up),
							contentDescription = "")
					}
				)

				ExposedDropdownMenu(
					modifier = Modifier.background(Color.White),
					expanded = monthMenuExpanded,
					onDismissRequest = { monthMenuExpanded = false}
				) {
					DropdownMenuItem(
						modifier = Modifier.background(Color.White),
						colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
						text = {
							Text(
								text = viewModelState.unSelectedMonthTitle,
								fontFamily = font,
								fontSize = 15.sp
							)},
						onClick = {
							viewModel.selectMonth(null, viewModelState.unSelectedMonthTitle)
							monthMenuExpanded = false
						}
					)
					viewModelState.months.forEachIndexed {index, month ->
						DropdownMenuItem(
							modifier = Modifier.background(Color.White),
							colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
							text = {
								Text(
									text = month,
									fontFamily = font,
									fontSize = 15.sp
								)},
							onClick = {
								viewModel.selectMonth(index + 1, month)
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
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),bottom = 20.dp),
			expanded = archiveMenuExpanded,
			onExpandedChange = { archiveMenuExpanded = !archiveMenuExpanded }
		){
			OutlinedTextField(
				modifier = Modifier
					.menuAnchor(type = MenuAnchorType.PrimaryNotEditable)
					.fillMaxWidth(),
				value = viewModelState.selectedArchiveTitle,
				onValueChange = {},
				readOnly = true,
				textStyle = TextStyle(
					fontFamily = font,
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
							text = viewModelState.unSelectedArchiveTitle,
							fontFamily = font,
							fontSize = 15.sp
						)},
					onClick = {
						viewModel.selectArchive(null, viewModelState.unSelectedArchiveTitle)
						archiveMenuExpanded = false
					}
				)

				viewModelState.selectableArchives.forEach {archive ->
					DropdownMenuItem(
						modifier = Modifier.background(Color.White),
						colors = MenuDefaults.itemColors(textColor = colorResource(R.color.text_color)),
						text = {
							Text(
								text = archive.title,
								fontFamily = font,
								fontSize = 15.sp
							)},
						onClick = {
							viewModel.selectArchive(archive.id, archive.title)
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
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),bottom = 20.dp)) {
				Text(
					color = Color.Gray,
					text = "Выбрать теги (${viewModelState.selectedTagIds.size})",
					fontFamily = font,
					fontSize = 15.sp,
					fontWeight = FontWeight.Bold
				)
			}
			viewModelState.selectableTags.forEachIndexed { index, tag ->
				Row(Modifier
					.fillMaxWidth()) {
					TagCheckboxComponent(
						tag,
						isChecked = viewModelState.selectedTagIds.find { it == tag.id } != null,
						onChecked = {tag ->

							val existingTag = viewModelState.selectedTagIds.find { it == tag.id }

							if (existingTag == null){
								viewModel.addTag(tag.id)
							}

							else{
								viewModel.removeTag(tag.id)
							}
					})
				}

				if (index != viewModelState.selectableTags.count() -1){
					HorizontalDivider(
						modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding)),
						thickness = 1.dp,
						color = colorResource(R.color.border_color))
				}
			}
		}

		if (viewModelState.hasNextTags){
			Row(Modifier
				.fillMaxWidth()
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),
					top = 20.dp,
					bottom = 40.dp)) {

				if (tagsLoading){
					ProgressIndicatorComponent(25, colorResource(R.color.primary_color))
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

										loadTags()

									} finally {
										tagsLoading = false
									}
								}
							}

						),
						color = colorResource(R.color.primary_color),
						text = "Загрузить еще",
						fontFamily = font,
						fontSize = 15.sp,
						fontWeight = FontWeight.Bold
					)
				}
			}
		}


		// Filter button
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),
					top = 60.dp)
		) {
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp),
				enabled = !filtering,
				shape = RoundedCornerShape(10.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = colorResource(R.color.primary_color),
					disabledContainerColor = colorResource(R.color.primary_color).copy(alpha = 0.8f)),
				onClick = {

					scope.launch {
						filtering = true
						try {
							viewModel.applyFilter()

							action()

						} finally {
							filtering = false
						}
					}
				}
			) {

				if (filtering){
					CircularProgressIndicator(
						modifier = Modifier.size(20.dp),
						strokeWidth = 2.dp,
						color = Color.White,
						trackColor = Color.Transparent,
					)
				}

				else{
					Text(
						text = "Применить",
						fontFamily = font,
						fontSize = 16.sp,
						fontWeight = FontWeight.SemiBold
					)
				}
			}
		}


		// Reset button
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),
					top = 30.dp, bottom = 80.dp),
			horizontalArrangement = Arrangement.Center
		){
			Text(
				modifier = Modifier.clickable(
					interactionSource = remember { MutableInteractionSource() },
					indication = null,
					onClick = {

						viewModel.resetFilter()

						resetFilter()
					}
				),
				text = "Сбросить фильтр",
				fontWeight = FontWeight.SemiBold,
				fontSize = 15.sp,
				color = Color.Gray,
				fontFamily = font,
			)
		}
	}
}