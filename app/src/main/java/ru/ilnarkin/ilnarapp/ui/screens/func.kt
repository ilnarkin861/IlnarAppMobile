package ru.ilnarkin.ilnarapp.ui.screens

import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.models.Tag

fun getNotesList() : MutableList<Note>{
    val notes = mutableListOf<Note>()
    val noteTitle = "Событие: Фестиваль дружбы"
    val noteText = """
		В маленьком городке, расположенном у подножия гор, ежегодно проходит фестиваль дружбы. Это событие собирает людей из разных уголков региона, и каждый год его темы отличаются.
	""".trimIndent()
    val noteDate = "2023-10-20"
    val noteType = NoteType(id = "", title = "Заметка")

    val tags = getTags(5)

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

fun getArchives(count: Int): List<Archive>{
    val archives = mutableListOf<Archive>()

    for (i in 1..count){
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