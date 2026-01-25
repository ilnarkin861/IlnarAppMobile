package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ilnarkin.ilnarapp.helpers.buildString
import ru.ilnarkin.ilnarapp.models.Info
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.repositories.TagRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class TagViewModel(private val tagRepository: TagRepository) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<Tag>())
	val uiState: StateFlow<AppUiState<Tag>> = _uiState.asStateFlow()


	suspend fun getTagsList(offset: Int, limit: Int){


		try {
			val tagsOffset = if (offset < 0) 0 else offset

			_uiState.value = _uiState.value.copy(loading = true)

			_uiState.value.list.clear()

			_uiState.value = _uiState.value.copy(list = _uiState.value.list)

			val result = tagRepository.getList(tagsOffset, limit, null)

			if (result.isSuccessful && result.body() != null){

				val tags = result.body()?.data

				tags?.count()?.let {

					if ( it > 0){
						for (tag in tags){
							_uiState.value.list.add(Tag(id = tag.id, title = tag.title))
						}
					}
				}

				_uiState.value.pagination = result.body()?.pagination

				_uiState.value.offset = tagsOffset

				_uiState.value = _uiState.value.copy(
					loading = false,
					list = _uiState.value.list,
					offset = _uiState.value.offset,
					pagination = _uiState.value.pagination)
			}
		}
		catch (_: Exception){}
	}


	suspend fun createTag(tag: Tag){

		try {

			_uiState.value = _uiState.value.copy(message = "")

			val result = tagRepository.create(tag)

			if (result.isSuccessful){
				_uiState.value = _uiState.value.copy(
					success = true,
					message = "Тег успешно добавлен",
					offset = 0
				)
			}

			else{
				val errorBody = result.errorBody()?.string()
				val errorResponse = Gson().fromJson(errorBody, Info::class.java)

				val message = buildString(errorResponse.messages)

				_uiState.value = _uiState.value.copy(
					success = false,
					message = message)
			}
		}

		catch (_: Exception){
			_uiState.value = _uiState.value.copy(
				success = false,
				message = "Ошибка при добавлении тега")
		}
	}
}