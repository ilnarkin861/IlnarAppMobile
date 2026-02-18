package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.exceptions.ApiException
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.AppPagination
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.repositories.TagRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<Tag>())
	val uiState: StateFlow<AppUiState<Tag>> = _uiState.asStateFlow()


	suspend fun getTagsList(offset: Int, limit: Int, showLoading: Boolean = true){

		try {
			_uiState.value = _uiState.value.copy(success = false)

			val tagsOffset = if (offset <= 0) 0 else offset

			if (showLoading){
				_uiState.value = _uiState.value.copy(loading = true)
			}

			val result = tagRepository.getList<AppPagination<Tag>>(tagsOffset, limit, null)

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


	suspend fun getTagById(id: String): Tag?{

		return try {
			_uiState.value = _uiState.value.copy(showAlert = false, data = null)

			tagRepository.getById<Tag>(id) as Tag
		}

		catch (e: ApiException){
			_uiState.value = _uiState.value.copy(
				success = false,
				data = null,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)

			null
		}

		catch (_: Exception){
			_uiState.value = _uiState.value.copy(
				success = false,
				data = null,
				showAlert = true,
				message = DEFAULT_ERROR_MESSAGE)

			null
		}
	}


	suspend fun createTag(tag: Tag): Tag?{

		try {
			_uiState.value = _uiState.value.copy(success = false)

			return tagRepository.create<Tag>(tag)
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
				message = "Ошибка при добавлении тега")

			return null
		}
	}


	suspend fun updateTag(tag: Tag): Tag?{

		try {
			_uiState.value = _uiState.value.copy(success = false)

			return tagRepository.update<Tag>(tag.id, tag)
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
				message = "Ошибка при обновлении тега")
			return null
		}
	}


	suspend fun deleteTag(id: String): Boolean{

		return try {
			_uiState.value = _uiState.value.copy(success = false)

			tagRepository.delete(id)
			
			_uiState.value = _uiState.value.copy(
				success = true,
				message = "Тег успешно удален"
			)

			true
		}

		catch (e: ApiException){
			_uiState.value = _uiState.value.copy(
				success = false,
				showAlert = true,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)

			false
		}

		catch (_: Exception){
			_uiState.value = _uiState.value.copy(
				success = false,
				showAlert = true,
				message = "Ошибка при удалении тега")

			false
		}
	}

	fun dismissAlert() {
		_uiState.update { it.copy(showAlert = false) }
	}
}