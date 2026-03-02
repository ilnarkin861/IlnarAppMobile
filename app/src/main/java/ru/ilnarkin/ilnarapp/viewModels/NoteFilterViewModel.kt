package ru.ilnarkin.ilnarapp.viewModels

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.NoteFilter
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.ui.NoteFilterUiState
import java.time.LocalDate


@RequiresApi(Build.VERSION_CODES.O)
class NoteFilterViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(NoteFilterUiState())
    val uiState: StateFlow<NoteFilterUiState> = _uiState.asStateFlow()


    init {
        val years = mutableListOf<Int>()
        val currentYear = LocalDate.now().year
        var startYear = _uiState.value.startYear

        while (startYear <= currentYear) {
            years.add(startYear)
            startYear++
        }

        _uiState.update { it.copy(years = years) }
    }


    fun addNoteTypes(noteTypes: List<NoteType>) {

        if (!noteTypes.isEmpty()){
            _uiState.update { it.copy(
                selectableNoteTypes = noteTypes,
                selectedNoteTypeId = noteTypes[0].id,
                selectedNoteTypeTitle = noteTypes[0].title
            )}

            updateFilter()
        }
    }


    fun addArchives(archives: List<Archive>){
        _uiState.update { it.copy(
            selectableArchives = archives
        )}
    }


    fun addTags(tags: List<Tag>, hasNextTags: Boolean){
        _uiState.update { currentState ->
            val updatedList = currentState.selectableTags + tags

            currentState.copy(
                selectableTags = updatedList,
                hasNextTags = hasNextTags
            )
        }
    }


    fun selectNoteType(id: String, title: String){
        _uiState.update { it.copy(
                selectedNoteTypeId = id,
                selectedNoteTypeTitle = title
            )
        }

        updateFilter()
    }


    fun selectYear(year: Int?, yearTitle: String){
        _uiState.update { currentState ->
            currentState.copy(
                selectedYear = year,
                selectedYearTitle = yearTitle,
                yearSelected = year != null,
                monthSelected = year != null,
                selectedMonthNumber = null
            )
        }

        if (year == null){
            selectMonth(null, _uiState.value.unSelectedMonthTitle)
        }

        updateFilter()
    }


    fun selectMonth(monthNumber: Int?, monthTitle: String){
        _uiState.update { it.copy(
            selectedMonthNumber = monthNumber,
            selectedMonthTitle = monthTitle,
            monthSelected = monthNumber != null
        )}

        updateFilter()
    }


    fun selectArchive(id: String?, title: String){
        _uiState.update { it.copy(
            selectedArchiveId = id,
            selectedArchiveTitle = title,
            archiveIsSelected = id != null
        )}

        updateFilter()
    }


    fun addTag(id: String){
        _uiState.update { currentState ->
            val updatedList = currentState.selectedTagIds.toMutableList()

            val existingTag = updatedList.find { it == id }

            if (existingTag == null) updatedList.add(id)

            currentState.copy(
                selectedTagIds = updatedList
            )
        }

        updateFilter()
    }


    fun removeTag(id: String){
        _uiState.update { currentState ->
            val updatedList = currentState.selectedTagIds.toMutableList()

            val existingTag = updatedList.find { it == id }

            if (existingTag != null) updatedList.remove(id)

            currentState.copy(
                selectedTagIds = updatedList
            )
        }

        updateFilter()
    }


    fun updateFilter(){

        _uiState.update { currentState ->
            val filter = NoteFilter(
                noteTypeId = currentState.selectedNoteTypeId,
                year = currentState.selectedYear,
                month = currentState.selectedMonthNumber,
                archiveId = currentState.selectedArchiveId,
                tagIds = currentState.selectedTagIds,
                day = null
            )

            currentState.copy(
                noteFilter = filter
            )
        }
    }


    fun applyFilter(){
        updateFilter()

        _uiState.update { it.copy(
            filterApplied = true
        )}
    }


    fun resetFilter(){

        selectNoteType(_uiState.value.selectableNoteTypes[0].id, _uiState.value.selectableNoteTypes[0].title)

        selectYear(null, _uiState.value.unSelectedYearTitle)

        selectMonth(null, _uiState.value.unSelectedMonthTitle)

        selectArchive(null, _uiState.value.unSelectedArchiveTitle)

        _uiState.update { currentState ->

            val updatedList = currentState.selectedTagIds.toMutableList()

            updatedList.clear()

            currentState.copy(
                selectedTagIds = updatedList,
                noteFilter = null,
                filterApplied = false
            )
        }
    }
}