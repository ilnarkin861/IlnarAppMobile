package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.exceptions.ApiException
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteFilter
import ru.ilnarkin.ilnarapp.repositories.NoteRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel(){

	private val _uiState = MutableStateFlow(AppUiState<Note>())
	val uiState: StateFlow<AppUiState<Note>> = _uiState.asStateFlow()


	suspend fun getNotesList(offset: Int, limit: Int, filter: NoteFilter? = null, showLoading: Boolean = true): List<Note>{

		return try {

			val notesOffset = if (offset <= 0) 0 else offset

			if (showLoading){
				_uiState.update { it.copy(
					loading = true
				)}
			}

			val filterParams = mutableListOf<Pair<String, String>>().apply {
				filter?.noteTypeId?.let { add("noteTypeId" to it) }
				filter?.archiveId?.let { add("archiveId" to it) }
				filter?.year?.let { add("year" to it.toString()) }
				filter?.month?.let { add("month" to it.toString()) }
				filter?.tagIds?.forEach { id -> add("tagIds" to id) }
			}

			val result = noteRepository.getList<AppPagination<Note>>(notesOffset, limit, filterParams)

			_uiState.update { it.copy(
				loading = false,
				offset = notesOffset,
				pagination = result.pagination,
				list = result.data)
			}

			result.data

		}

		catch (_: Exception){
			_uiState.update { it.copy(
				loading = false,
				success = false,
				list = emptyList(),
				showAlert = true,
				message = DEFAULT_ERROR_MESSAGE
			)}

			emptyList()
		}
	}


	suspend fun getNoteById(id: String): Note?{

		try {
			return noteRepository.getById<Note>(id)

		}

		catch (e: ApiException){
			_uiState.update { it.copy(
				success = false,
				data = null,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)}

			return null
		}

		catch (_: Exception){
			_uiState.update { it.copy(
				success = false,
				data = null,
				showAlert = true,
				message = "Ошибка при получении записи"
			)}

			return null
		}
	}


	suspend fun createNote(note: Note): Note?{

		try {
			return noteRepository.create<Note>(note)
		}

		catch (e: ApiException){

			_uiState.update { it.copy(
				success = false,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)}

			return null
		}

		catch (_: Exception){
			_uiState.update { it.copy(
				success = false,
				showAlert = true,
				message = "Ошибка при добавлении записи"
			)}

			return null
		}
	}


	suspend fun updateNote(note: Note): Note?{

		try {
			return noteRepository.update<Note>(note.id, note)
		}

		catch (e: ApiException){
			_uiState.update { it.copy(
				success = false,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)}

			return null
		}

		catch (_: Exception){
			_uiState.update { it.copy(
				success = false,
				showAlert = true,
				message = "Ошибка при изменении записи"
			)}

			return null
		}
	}


	suspend fun deleteNote(id: String): Boolean{

		try {
			return noteRepository.delete(id)
		}

		catch (e: ApiException){
			_uiState.update { it.copy(
				success = false,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)}

			return false
		}

		catch (_: Exception){
			_uiState.update { it.copy(
				success = false,
				showAlert = true,
				message = "Ошибка при удалении записи"
			)}

			return false
		}
	}


	fun dismissAlert() {
		_uiState.update { it.copy(showAlert = false) }
	}
}