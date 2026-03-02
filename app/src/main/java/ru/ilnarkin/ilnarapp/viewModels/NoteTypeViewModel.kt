package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.exceptions.ApiException
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.repositories.NoteTypeRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class NoteTypeViewModel(private val noteTypeRepository: NoteTypeRepository) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<NoteType>())
	val uiState: StateFlow<AppUiState<NoteType>> = _uiState.asStateFlow()


	suspend fun getNoteTypesList(offset: Int, limit: Int, showLoading: Boolean = true): List<NoteType>{

		try {
			_uiState.value = _uiState.value.copy(success = false, showAlert = false)

			val tagsOffset = if (offset <= 0) 0 else offset

			if (showLoading){
				_uiState.update { it.copy(
					loading = true
				)}
			}

			val result = noteTypeRepository.getList<AppPagination<NoteType>>(tagsOffset, limit, null)

			if (result.data.isEmpty()){
				throw ApiException("Нет спика типов записи")
			}

			_uiState.update { it.copy(
				loading = false,
				success = true,
				list = result.data,
				offset = tagsOffset,
				pagination = result.pagination
			)}

			return result.data
		}

		catch (e: ApiException){

			_uiState.update { it.copy(
				loading = false,
				success = false,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)}

			return emptyList()
		}

		catch (_: Exception){

			_uiState.update { it.copy(
				loading = false,
				success = false,
				showAlert = true,
				message = "Ошибка при получении типов записи"
			)}

			return emptyList()
		}
	}


	fun dismissAlert() {
		_uiState.update { it.copy(showAlert = false) }
	}
}