package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.repositories.NoteTypeRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class NoteTypeViewModel(private val noteTypeRepository: NoteTypeRepository) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<NoteType>())
	val uiState: StateFlow<AppUiState<NoteType>> = _uiState.asStateFlow()


	suspend fun getNoteTypesList(offset: Int, limit: Int, showLoading: Boolean = true){

		try {
			_uiState.value = _uiState.value.copy(success = false)

			val tagsOffset = if (offset <= 0) 0 else offset

			if (showLoading){
				_uiState.value = _uiState.value.copy(loading = true)
			}

			val result = noteTypeRepository.getList<AppPagination<NoteType>>(tagsOffset, limit, null)

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
}