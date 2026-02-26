package ru.ilnarkin.ilnarapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.FilterModel
import ru.ilnarkin.ilnarapp.models.NoteFilter
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.models.Tag
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchFormComponent(
	noteTypes: List<NoteType>,
	archives: List<Archive>,
	tags: MutableList<Tag>,
	hasNextTags: Boolean = true,
	loadTags: suspend () -> MutableList<Tag>,
	action: suspend (filter: FilterModel) -> Unit
) {

	val selectableTags = remember { mutableStateListOf<Tag>().apply { addAll(tags) } }

	val font = getInterFont()

	var tagsLoading by remember { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	var startYear = 2026

	val unSelectedYearTitle = "Год не выбран"
	var yearsMenuExpanded by remember { mutableStateOf(false) }
	var selectedYearTitle by remember { mutableStateOf(unSelectedYearTitle) }
	var selectedYear: Int? by remember { mutableStateOf(null) }
	var yearSelected by remember { mutableStateOf(false) }

	val years = mutableListOf<Int>()

	while (startYear <= LocalDate.now().year){
		years.add(startYear)
		startYear++
	}

	val months = arrayOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль",
		"Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь",)
	val unSelectedMonthTitle = "Месяц не выбран"
	var monthSelected by remember { mutableStateOf(false) }
	var monthMenuExpanded by remember { mutableStateOf(false) }
	var selectedMonthTitle by remember { mutableStateOf(unSelectedMonthTitle) }
	var selectedMonthNumber: Int? by remember { mutableStateOf(null) }

	var noteTypeMenuExpanded by remember { mutableStateOf(false) }
	val selectedNoteType = remember { mutableStateOf(noteTypes[0]) }

	val unSelectedArchiveTitle = "Архив не выбран"
	var selectedArchiveTitle by remember { mutableStateOf(unSelectedArchiveTitle) }
	var archiveMenuExpanded by remember { mutableStateOf(false) }
	var archiveIsSelected by remember { mutableStateOf(false) }
	var selectedArchive: Archive? by remember { mutableStateOf(null) }

	val selectedTags = remember { mutableStateListOf<Tag>() }
	val selectedTagsCount = remember { mutableIntStateOf(0) }


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
								fontFamily = font,
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
				value = selectedYearTitle,
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
							text = unSelectedYearTitle,
							fontFamily = font,
							fontSize = 15.sp
						)},
					onClick = {
						selectedYearTitle = unSelectedYearTitle
						yearSelected = false
						yearsMenuExpanded = false
						monthSelected = false
						selectedYear = null
						selectedMonthNumber = null
					}
				)
				years.forEach {year ->
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
							selectedYear = year
							selectedYearTitle = year.toString()
							yearSelected = true
							yearsMenuExpanded = false
						}
					)
				}
			}
		}

		// Month dropdown
		if (yearSelected){
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
					value = selectedMonthTitle,
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
								text = unSelectedYearTitle,
								fontFamily = font,
								fontSize = 15.sp
							)},
						onClick = {
							selectedMonthTitle = unSelectedMonthTitle
							monthSelected = false
							monthMenuExpanded = false
						}
					)
					months.forEachIndexed {index, month ->
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
								selectedMonthTitle = month
								selectedMonthNumber = index + 1
								monthSelected = true
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
				value = selectedArchiveTitle,
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
							text = unSelectedArchiveTitle,
							fontFamily = font,
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
								fontFamily = font,
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
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),bottom = 20.dp)) {
				Text(
					color = Color.Gray,
					text = "Выбрать теги (${selectedTagsCount.intValue})",
					fontFamily = font,
					fontSize = 15.sp,
					fontWeight = FontWeight.Bold
				)
			}
			selectableTags.forEachIndexed { index, tag ->
				Row(Modifier
					.fillMaxWidth()) {
					TagCheckboxComponent(tag, onChecked = {tag ->
						if (selectedTags.count() == 0){
							selectedTags.add(tag)
						}

						else{
							if (selectedTags.any{it.id == tag.id}){
								selectedTags.remove(tag)
							}

							else selectedTags.add(tag)
						}

						selectedTagsCount.intValue = selectedTags.count()
					})
				}

				if (index != selectableTags.count() -1){
					HorizontalDivider(
						modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding)),
						thickness = 1.dp,
						color = colorResource(R.color.border_color))
				}
			}
		}

		if (hasNextTags){
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
										val tags = loadTags()

										selectableTags.addAll(tags)
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

		// Search button
		Row(
			modifier = Modifier
				.fillMaxWidth()
				.padding(start = dimensionResource(R.dimen.container_horizontal_padding),
					end = dimensionResource(R.dimen.container_horizontal_padding),
					top = 60.dp, bottom = 80.dp)
		) {
			Button(
				modifier = Modifier
					.fillMaxWidth()
					.height(60.dp),
				shape = RoundedCornerShape(10.dp),
				colors = ButtonDefaults.buttonColors(
					containerColor = colorResource(R.color.primary_color),
					disabledContainerColor = colorResource(R.color.primary_color).copy(alpha = 0.8f)),
				onClick = {

					val tagIds = mutableListOf<String>()

					for (tag in selectedTags){
						tagIds.add(tag.id)
					}

					val filter = NoteFilter(
						noteTypeId = selectedNoteType.value.id,
						archiveId = selectedArchive?.id,
						year = selectedYear,
						month = selectedMonthNumber,
						day = null,
						tagIds = tagIds
					)

					scope.launch {
						action(filter)
					}
				}
			) {
				Text(
					text = "Искать",
					fontFamily = font,
					fontSize = 16.sp,
					fontWeight = FontWeight.SemiBold
				)
			}
		}
	}
}