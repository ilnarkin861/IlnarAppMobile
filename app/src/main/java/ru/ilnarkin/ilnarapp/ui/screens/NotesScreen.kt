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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteDetailsComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteFormComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteItemComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen() {

	var currentNote by remember { mutableStateOf<Note?>(null) }
	var notes = getNotesList()
	var showNoteFormSheet by remember { mutableStateOf(false) }
	var showNoteDetailsSheet by remember { mutableStateOf(false) }
	val listState = rememberLazyListState()
	val noteFormSheetState = rememberModalBottomSheetState()
	val noteDetailsSheetState = rememberModalBottomSheetState()
	var sheetTitle = remember { mutableStateOf("") }
	var alertTitle = remember { mutableStateOf("") }
	var loading by remember { mutableStateOf(false) }
	var noteDetailsLoading by remember { mutableStateOf(false) }
	var showAlert by remember { mutableStateOf(false) }
	var success by remember { mutableStateOf(true) }
	val scope = rememberCoroutineScope()
	val noteFullText = """
		В маленьком городке, расположенном у подножия гор, ежегодно проходит фестиваль дружбы. Это событие собирает людей из разных уголков региона, и каждый год его темы отличаются.

		В этом году открыл его известный местный музыкант, который исполнил песни о дружбе и единстве. На главной площади горько улыбалась выступление детей из местной школы. Их танец, который они подготовили специально для этого дня, зацепил сердца всех зрителей.
			
		Фестиваль дружбы становится не только местом встречи старых друзей, но и возможностью завести новые знакомства. Люди разных возрастов и национальностей объединяются под общим девизом: "Вместе мы сильнее!" По завершении праздника жители обещали встречаться чаще и продолжать развивать дружеские связи, возникающие в течение этого неповторимого дня.
		""".trimIndent()


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
				items(notes) {value ->
					NoteItemComponent(
						value,
						viewAction = {note ->
							currentNote = null

							showNoteDetailsSheet = true

							noteDetailsLoading = true

							scope.launch {
								delay(2500)
							}.invokeOnCompletion {
								note.text = noteFullText
								currentNote = note
								noteDetailsLoading = false
							}
						},

						editAction = {note ->
							currentNote = note
							showNoteFormSheet = true

						},
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

				// еще что-то делаем
			}
		)


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
				showNoteFormSheet = true
			}) {
			Icon(painter = painterResource(R.drawable.ic_plus), contentDescription = "Добавить")
		}


		if (showNoteFormSheet){
			ModalBottomSheet(
				onDismissRequest = {
					currentNote = null
					showNoteFormSheet = false
				},
				containerColor = Color.White,
				sheetState = noteFormSheetState,
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
						currentNote,
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

							noteFormSheetState.hide()

							showNoteFormSheet = false
						}
					)
				}
			}
		}


		if (showNoteDetailsSheet){
			ModalBottomSheet(
				onDismissRequest = { showNoteDetailsSheet = false },
				containerColor = Color.White,
				sheetState = noteDetailsSheetState,
			) {
				if (noteDetailsLoading){
					Box(
						modifier = Modifier.fillMaxWidth().height(200.dp),
						contentAlignment = Alignment.Center
					) {
						ProgressIndicatorComponent(50)
					}
				}

				else { NoteDetailsComponent(currentNote) }
			}
		}
	}
}


fun getNotesList() : MutableList<Note>{
	val notes = mutableListOf<Note>()
	val noteTitle = "Событие: Фестиваль дружбы"
	val noteText = """
		В маленьком городке, расположенном у подножия гор, ежегодно проходит фестиваль дружбы. Это событие собирает людей из разных уголков региона, и каждый год его темы отличаются.
	""".trimIndent()
	val noteDate = "2023-10-20"
	val noteType = NoteType(id = "", title = "Заметка")

	var tags = getTags(5)

	for (i in 1..10){
		notes.add(Note(
			title = "$noteTitle - $i",
			text = noteText,
			date = noteDate,
			noteType =  noteType,
			tags = tags,
			archive = Archive(id = "", title = "Архив 1")
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