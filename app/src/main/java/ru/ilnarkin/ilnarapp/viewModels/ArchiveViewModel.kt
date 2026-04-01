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
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.exceptions.ApiException
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.pagingSources.ArchivePagingSource
import ru.ilnarkin.ilnarapp.repositories.ArchiveRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class ArchiveViewModel(private val archiveRepository: ArchiveRepository) : ViewModel(){

	private val _uiState = MutableStateFlow(AppUiState<Archive>())
	val uiState: StateFlow<AppUiState<Archive>> = _uiState.asStateFlow()
	var currentPagingSource: ArchivePagingSource? = null


	@OptIn(ExperimentalCoroutinesApi::class)
	val archivesFlow = Pager(
		config = PagingConfig(
			pageSize = 50,
			enablePlaceholders = false,
			initialLoadSize = 50,
			prefetchDistance = 1
		),
		pagingSourceFactory = {
			ArchivePagingSource(archiveRepository).also { currentPagingSource = it }
		}

	).flow.cachedIn(viewModelScope)

	suspend fun getArchivesList(offset: Int, limit: Int, showLoading: Boolean = true): List<Archive>{

		try {
			_uiState.value = _uiState.value.copy(success = false)

			val tagsOffset = if (offset <= 0) 0 else offset

			if (showLoading){
				_uiState.update { it.copy(
					loading = true
				)}
			}

			val result = archiveRepository.getList<AppPagination<Archive>>(tagsOffset, limit, null)

			_uiState.update { it.copy(
				loading = false,
				success = true,
				list = result.data,
				offset = tagsOffset,
				pagination = result.pagination
			)}

			return result.data
		}

		catch (_: Exception){
			_uiState.update { it.copy(
				loading = false,
				success = false,
				showAlert = true,
				list = emptyList(),
				message = DEFAULT_ERROR_MESSAGE
			)}

			return emptyList()
		}
	}


	suspend fun getArchiveById(id: String): Archive?{

		try {
			_uiState.value = _uiState.value.copy(showAlert = false, data = null)

			return archiveRepository.getById<Archive>(id)
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
				message = DEFAULT_ERROR_MESSAGE
			)}

			return null
		}
	}


	suspend fun createArchive(archive: Archive): Archive?{

		try {
			_uiState.value = _uiState.value.copy(success = false)

			return archiveRepository.create<Archive>(archive)
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
				message = "Ошибка при добавлении архива"
			)}

			return null
		}
	}


	suspend fun updateArchive(archive: Archive): Archive?{

		try {
			_uiState.value = _uiState.value.copy(success = false)

			return archiveRepository.update<Archive>(archive.id, archive)
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
				message = "Ошибка при обновлении архива"
			)}

			return null
		}
	}


	suspend fun deleteArchive(id: String): Boolean{

		try {
			_uiState.value = _uiState.value.copy(success = false)

			archiveRepository.delete(id)

			_uiState.update { it.copy(
				success = true,
				message = "Архив успешно удален"
			)}

			return true
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
				message = "Ошибка при удалении архива"
			)}

			return false
		}
	}


	fun setActionType(actionType: ActionType){
		_uiState.update { it.copy(
			actionType = actionType
		) }
	}


	fun dismissAlert() {
		_uiState.update { it.copy(showAlert = false) }
	}


	fun refreshData() {
		currentPagingSource?.invalidate()
	}
}