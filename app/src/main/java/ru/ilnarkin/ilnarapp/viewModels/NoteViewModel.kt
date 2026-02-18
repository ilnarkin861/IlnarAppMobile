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
import ru.ilnarkin.ilnarapp.repositories.NoteRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel(){

	private val _uiState = MutableStateFlow(AppUiState<Note>())
	val uiState: StateFlow<AppUiState<Note>> = _uiState.asStateFlow()


	suspend fun getNotesList(offset: Int, limit: Int, showLoading: Boolean = true){

		try {
			_uiState.value = _uiState.value.copy(success = false)

			val tagsOffset = if (offset <= 0) 0 else offset

			if (showLoading){
				_uiState.value = _uiState.value.copy(loading = true)
			}

			val result = noteRepository.getList<AppPagination<Note>>(tagsOffset, limit, null)

			_uiState.value = _uiState.value.copy(
				loading = false,
				list = result.data,
				offset = tagsOffset,
				pagination = result.pagination)

			_uiState.update { it.copy(success = false) }

		}
		catch (_: Exception){
			_uiState.value = _uiState.value.copy(
				loading = false,
				success = false,
				message = DEFAULT_ERROR_MESSAGE
			)
		}
	}


	suspend fun createNote(note: Note): Note?{

		try {
			_uiState.value = _uiState.value.copy(success = false)

			return noteRepository.create<Note>(note)
		}

		catch (e: ApiException){
			_uiState.value = _uiState.value.copy(
				success = false,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)

			return null
		}

		catch (_: Exception){
			_uiState.value = _uiState.value.copy(
				success = false,
				showAlert = true,
				message = "Ошибка при добавлении записи")

			return null
		}
	}


	fun dismissAlert() {
		_uiState.update { it.copy(showAlert = false) }
	}
}