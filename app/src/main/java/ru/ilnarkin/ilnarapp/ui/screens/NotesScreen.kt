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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
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
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteFormComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteItemComponent


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen() {

	val notes = getNotesList()
	var showBottomSheet by remember { mutableStateOf(false) }
	val sheetState = rememberModalBottomSheetState()
	var sheetTitle = remember { mutableStateOf("") }


	Box(Modifier.fillMaxSize()) {
		LazyColumn(contentPadding = PaddingValues(top = 30.dp, bottom = 60.dp)) {
			item {
				Row(Modifier.padding(bottom = 15.dp)) {
					LoadButtonComponent(nextButton = false, action = {})
				}
			}
			items(notes) {value ->
				NoteItemComponent(value)
			}
			item {
				Row(Modifier.padding(top = 15.dp, bottom = 30.dp)) {
					LoadButtonComponent(action = {})
				}
			}
		}

		if (showBottomSheet){
			ModalBottomSheet(
				onDismissRequest = { showBottomSheet = false },
				containerColor = Color.White,
				sheetState = sheetState
			) {
				Column(Modifier.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))) {
					Row {
						Text(
							color = colorResource(R.color.title_color),
							text = sheetTitle.value,
							fontFamily = getInterFont(),
							fontSize = 18.sp,
							fontWeight = FontWeight.Bold
						)
					}

					NoteFormComponent()
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
	val noteType = NoteType(id = "", title = "Note type")

	for (i in 1..10){
		notes.add(Note(
			title = noteTitle,
			text = noteText,
			date = noteDate,
			noteType =  noteType
		))
	}

	return notes
}