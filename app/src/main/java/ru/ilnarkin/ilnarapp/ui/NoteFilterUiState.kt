package ru.ilnarkin.ilnarapp.ui

import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.NoteFilter
import ru.ilnarkin.ilnarapp.models.NoteType


data class NoteFilterUiState(
    var noteFilter: NoteFilter? = null,
    val selectableNoteTypes: List<NoteType> = listOf(),
    val selectableArchives: List<Archive> = listOf(),
    val selectableTags: List<Tag> = listOf(),
    val selectedNoteTypeId: String = "",
    val selectedNoteTypeTitle: String = "",
    var startYear: Int = 2025,
    val years: List<Int> = listOf(),
    val unSelectedYearTitle: String = "Год не выбран",
    var selectedYearTitle: String = unSelectedYearTitle,
    var selectedYear: Int? = null,
    var yearSelected: Boolean = false,
    val months: List<String> = listOf("Январь", "Февраль", "Март", "Апрель", "Май", "Июнь", "Июль", "Август", "Сентябрь", "Октябрь", "Ноябрь", "Декабрь"),
    val unSelectedMonthTitle: String = "Месяц не выбран",
    var monthSelected: Boolean = false,
    var selectedMonthTitle: String = unSelectedMonthTitle,
    var selectedMonthNumber: Int? = null,
    val unSelectedArchiveTitle: String = "Архив не выбран",
    var selectedArchiveTitle: String = unSelectedArchiveTitle,
    var archiveIsSelected: Boolean = false,
    var selectedArchiveId: String? = null,
    val selectedTagIds: List<String> = listOf(),
    val hasNextTags: Boolean = false,
    val filterApplied: Boolean = false
)
