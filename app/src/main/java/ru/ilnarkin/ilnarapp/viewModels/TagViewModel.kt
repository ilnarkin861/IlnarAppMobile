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
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.pagingSources.TagPagingSource
import ru.ilnarkin.ilnarapp.repositories.TagRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState
import kotlin.collections.any
import kotlin.collections.count


class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<Tag>())
	val uiState: StateFlow<AppUiState<Tag>> = _uiState.asStateFlow()
	var currentPagingSource: TagPagingSource? = null
	private val selectableTags = mutableListOf<Tag>()
	private val selectedTags = MutableStateFlow(mutableListOf<Tag>())



	@OptIn(ExperimentalCoroutinesApi::class)
	val tagsFlow = Pager(
		config = PagingConfig(
			pageSize = 15,
			enablePlaceholders = false,
			initialLoadSize = 15,
			prefetchDistance = 1
		),
		pagingSourceFactory = {
			TagPagingSource(tagRepository).also { currentPagingSource = it }
		}

	).flow.cachedIn(viewModelScope)


	suspend fun getTagsList(offset: Int, limit: Int, showLoading: Boolean = true): List<Tag>{

		try {
			_uiState.value = _uiState.value.copy(success = false)

			val tagsOffset = if (offset <= 0) 0 else offset

			if (showLoading){
				_uiState.update { it.copy(
					loading = true
				)}
			}

			val result = tagRepository.getList<AppPagination<Tag>>(tagsOffset, limit, null)

			_uiState.update { currentState ->

				val updatedList = currentState.list + result.data

				currentState.copy(
					loading = false,
					success = true,
					list = updatedList,
					offset = tagsOffset,
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


	suspend fun getTagById(id: String): Tag?{

		try {
			return tagRepository.getById<Tag>(id)
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
				message = "Ошибка при получении тега"
			)}

			return null
		}
	}


	suspend fun createTag(tag: Tag): Tag?{

		try {
			return tagRepository.create<Tag>(tag)
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
				message = "Ошибка при добавлении тега"
			)}

			return null
		}
	}


	suspend fun updateTag(tag: Tag): Tag?{

		try {
			return tagRepository.update<Tag>(tag.id, tag)
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
				message = "Ошибка при обновлении тега"
			) }

			return null
		}
	}


	suspend fun deleteTag(id: String): Boolean{

		try {
			return tagRepository.delete(id)
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
				message = "Ошибка при удалении тега"
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


	fun getSelectedTags(): MutableList<Tag>{
		return selectedTags.value
	}


	fun clearSelectedTags(){
		selectedTags.value.clear()
	}


	fun selectTag(tag: Tag){
		if (selectedTags.value.count() == 0){
			selectedTags.value.add(tag)
		}

		else{
			if (selectedTags.value.any{it.id == tag.id}){
				selectedTags.value.remove(tag)
			}

			else selectedTags.value.add(tag)
		}
	}


	fun addSelectableTags(tags: List<Tag>){
		selectableTags.addAll(tags)
	}


	fun getSelectableTags(): MutableList<Tag>{
		return selectableTags
	}
}