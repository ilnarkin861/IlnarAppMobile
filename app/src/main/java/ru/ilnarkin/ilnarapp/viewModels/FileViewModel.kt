package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.models.FileInfo
import ru.ilnarkin.ilnarapp.pagingSources.FilePagingSource
import ru.ilnarkin.ilnarapp.repositories.FileRepository


class FileViewModel(private val fileRepository: FileRepository): ViewModel() {
	var currentPagingSource: FilePagingSource? = null

	private val _selectedFiles = MutableStateFlow<Set<FileInfo>>(emptySet())
	val selectedFiles = _selectedFiles.asStateFlow()


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

	fun clearSelection(){
		_selectedFiles.value = emptySet()
	}
}