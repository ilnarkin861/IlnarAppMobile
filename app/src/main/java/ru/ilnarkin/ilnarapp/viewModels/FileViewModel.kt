package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.FileInfo
import ru.ilnarkin.ilnarapp.repositories.FileRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class FileViewModel(private val fileRepository: FileRepository): ViewModel() {
	private val _uiState = MutableStateFlow(AppUiState<FileInfo>())
	val uiState: StateFlow<AppUiState<FileInfo>> = _uiState.asStateFlow()


	suspend fun getFilesList(offset: Int, limit: Int): List<FileInfo>{

		try {

			val filesOffset = if (offset <= 0) 0 else offset

			val result = fileRepository.getFilesList<AppPagination<FileInfo>>(filesOffset, limit)

			_uiState.update { currentState ->

				val updatedList = currentState.list + result.data

				currentState.copy(
					loading = false,
					success = true,
					list = updatedList,
					offset = filesOffset,
					pagination = result.pagination
				)
			}

			return result.data

		}

		catch (_: Exception){
			_uiState.update { it.copy(
				loading = false,
				success = false,
				list = emptyList(),
				showAlert = true,
				message = DEFAULT_ERROR_MESSAGE
			) }

			return emptyList()
		}


	}
}