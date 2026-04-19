package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.withContext
import ru.ilnarkin.ilnarapp.exceptions.ApiException
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.FileInfo
import ru.ilnarkin.ilnarapp.models.SelectedFileInfo
import ru.ilnarkin.ilnarapp.pagingSources.FilePagingSource
import ru.ilnarkin.ilnarapp.repositories.FileRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class FileViewModel(private val fileRepository: FileRepository): ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<FileInfo>())
	val uiState: StateFlow<AppUiState<FileInfo>> = _uiState.asStateFlow()
	var currentPagingSource: FilePagingSource? = null
	private val _selectedFiles = MutableStateFlow<Set<FileInfo>>(emptySet())
	val selectedFiles = _selectedFiles.asStateFlow()

	private val _localSelectedFiles = MutableStateFlow<List<SelectedFileInfo>>(emptyList())

	val localSelectedFiles = _localSelectedFiles.asStateFlow()


	@OptIn(ExperimentalCoroutinesApi::class)
	val filesFlow = Pager(
		config = PagingConfig(
			pageSize = 15,
			enablePlaceholders = false,
			initialLoadSize = 15,
			prefetchDistance = 1
		),
		pagingSourceFactory = {
			FilePagingSource(fileRepository).also { currentPagingSource = it }
		}

	).flow.cachedIn(viewModelScope)


	fun toggleSelection(file: FileInfo) {
		_selectedFiles.update { currentSet ->
			if (currentSet.contains(file)) {
				currentSet - file
			} else {
				currentSet + file
			}
		}
	}


	suspend fun upload(): Boolean = withContext(Dispatchers.IO) {

		return@withContext try {

			_uiState.update { it.copy(
				loading = true
			)}

			val files = getLocalSelectedFiles()

			fileRepository.uploadFiles(files)

			clearLocalSelectedFiles()

			true
		}
		catch (e: ApiException){
			_uiState.update { it.copy(
				success = false,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)}

			false
		}

		catch (e: Exception){
			_uiState.update { it.copy(
				success = false,
				showAlert = true,
				message = e.toString()
			)}

			false
		}

		finally {
			_uiState.update { it.copy(
				loading = false
			)}
		}
	}


	fun refreshData() {
		currentPagingSource?.invalidate()
	}


	fun clearSelection(){
		_selectedFiles.value = emptySet()
	}


	fun addLocalSelectedFile(file: SelectedFileInfo) {
		_localSelectedFiles.value = _localSelectedFiles.value + file
	}


	private fun getLocalSelectedFiles(): List<SelectedFileInfo>{
		return _localSelectedFiles.value
	}


	fun removeLocalSelectedFile(file: SelectedFileInfo) {
		_localSelectedFiles.value = _localSelectedFiles.value - file
	}


	fun clearLocalSelectedFiles(){
		_localSelectedFiles.value = emptyList()
	}


	fun dismissAlert() {
		_uiState.update { it.copy(showAlert = false) }
	}
}