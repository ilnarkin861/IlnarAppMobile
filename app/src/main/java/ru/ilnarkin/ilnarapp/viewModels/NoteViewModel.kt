package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.exceptions.ApiException
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteFilter
import ru.ilnarkin.ilnarapp.pagingSources.NotePagingSource
import ru.ilnarkin.ilnarapp.repositories.NoteRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel(){

	private val _uiState = MutableStateFlow(AppUiState<Note>())
	val uiState: StateFlow<AppUiState<Note>> = _uiState.asStateFlow()
	var currentPagingSource: NotePagingSource? = null
	private val _filter = MutableStateFlow<NoteFilter?>(null)


	@OptIn(ExperimentalCoroutinesApi::class)
	val notesFlow = _filter.flatMapLatest { currentFilter ->
		Pager(
			config = PagingConfig(
				pageSize = 10,
				enablePlaceholders = false,
				initialLoadSize = 10,
				prefetchDistance = 1
			),
			pagingSourceFactory = {
				NotePagingSource(noteRepository, currentFilter).also {
					currentPagingSource = it
				}
			}
		).flow
	}.cachedIn(viewModelScope)


	suspend fun getNoteById(id: String): Note?{

		try {
			val note = noteRepository.getById<Note>(id)

			_uiState.update { it.copy(
				success = true,
				data = note,
			)}

			return  note
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
			_uiState.update { it.copy( data = null )}

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
			_uiState.update { it.copy( data = null )}

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


	fun clearNote(){
		_uiState.update { it.copy( data = null )}
	}


	fun setActionType(actionType: ActionType){
		_uiState.update { it.copy(
			actionType = actionType
		) }
	}


	fun dismissAlert() {
		_uiState.update { it.copy(showAlert = false) }
	}


	fun updateFilter(newFilter: NoteFilter?) {
		_filter.value = newFilter
	}


	fun refreshData() {
		currentPagingSource?.invalidate()
	}
}