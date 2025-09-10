package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteItemComponent


@Composable
fun NotesScreen() {

	val notes = getNotesList()

	Column {
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