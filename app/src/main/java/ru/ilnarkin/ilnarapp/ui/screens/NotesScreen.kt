package ru.ilnarkin.ilnarapp.ui.screens

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteFormComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteItemComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen() {

	var notes = getNotesList()
	var showBottomSheet by remember { mutableStateOf(false) }
	val listState = rememberLazyListState()
	val sheetState = rememberModalBottomSheetState()
	var sheetTitle = remember { mutableStateOf("") }
	var alertTitle = remember { mutableStateOf("") }
	var loading by remember { mutableStateOf(false) }
	var showAlert by remember { mutableStateOf(false) }
	var success by remember { mutableStateOf(true) }



	LaunchedEffect(Unit) {
		loading = true

		delay(1500)

		loading = false
	}

	Box(Modifier.fillMaxSize()) {

		if (loading){
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center){
				ProgressIndicatorComponent(60)
			}
		}


		if (!loading && !notes.isEmpty()){
			LazyColumn(
				state = listState,
				contentPadding = PaddingValues(top = 30.dp, bottom = 60.dp)) {
				item {
					Row(Modifier.padding(bottom = 25.dp)) {
						LoadButtonComponent(nextButton = false, action = { delay(1500) })
					}
				}
				items(notes) {note ->
					NoteItemComponent(
						note,
						viewAction = {},
						editAction = {},
						deleteAction = {})
				}
				item {
					Row(Modifier.padding(top = 25.dp, bottom = 30.dp)) {
						LoadButtonComponent(action = {
							delay(1500)
							listState.scrollToItem(0)
						})
					}
				}
			}
		}


		AlertComponent(
			success = success,
			message = alertTitle.value,
			showed = showAlert,
			action = {
				showAlert = false
			}
		)


		if (showBottomSheet){
			ModalBottomSheet(
				onDismissRequest = { showBottomSheet = false },
				containerColor = Color.White,
				sheetState = sheetState,
			) {
				Column(Modifier
					.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))) {
					Row {
						Text(
							color = colorResource(R.color.title_color),
							text = sheetTitle.value,
							fontFamily = getInterFont(),
							fontSize = 18.sp,
							fontWeight = FontWeight.Bold
						)
					}

					NoteFormComponent(
						noteTypes = getNoteTypes(),
						archives = getArchives(),
						tags = getTags(10),

						loadTags = {count ->
							delay(2000)

							getTags(count)
						},

						action = {
							delay(3000)

							alertTitle.value = "Запись успешно добавлена"

							showAlert = true

							sheetState.hide()

							showBottomSheet = false
						}
					)
				}
			}
		}


		FloatingActionButton(
			containerColor = colorResource(R.color.primary_color),
			contentColor = Color.White,
			shape = CircleShape,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.absolutePadding(bottom = 30.dp, right = 30.dp)
				.background(Color.Transparent),
			onClick = {
				sheetTitle.value = "Добавить запись"
				showBottomSheet = true
			}) {
			Icon(painter = painterResource(R.drawable.ic_plus), contentDescription = "Добавить")
		}
	}
}


fun getNotesList() : MutableList<Note>{
	val notes = mutableListOf<Note>()
	val noteTitle = "Событие: Фестиваль дружбы"
	val noteText = """
		В маленьком городке, расположенном у подножия гор, ежегодно проходит фестиваль дружбы. Это событие собирает людей из разных уголков региона, и каждый год его темы отличаются.
	""".trimIndent()
	val noteDate = "12.09.2025"
	val noteType = NoteType(id = "", title = "Событие")

	for (i in 1..10){
		notes.add(Note(
			title = "$noteTitle - $i",
			text = noteText,
			date = noteDate,
			noteType =  noteType
		))
	}

	return notes
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

fun getTags(count: Int): MutableList<Tag>{
	val tags = mutableListOf<Tag>()

	for (i in 1..count){
		tags.add(Tag(id = "", title = "Тег ${i}"))
	}

	return tags
}