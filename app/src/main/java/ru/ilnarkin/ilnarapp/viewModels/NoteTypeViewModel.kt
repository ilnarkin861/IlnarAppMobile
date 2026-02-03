package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ilnarkin.ilnarapp.models.NoteType
import ru.ilnarkin.ilnarapp.repositories.NoteTypeRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class NoteTypeViewModel(private val noteTypeRepository: NoteTypeRepository) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<NoteType>())
	val uiState: StateFlow<AppUiState<NoteType>> = _uiState.asStateFlow()


	suspend fun getNoteTypesList(offset: Int, limit: Int, showLoading: Boolean = true){

		try {
			val tagsOffset = if (offset < 0) 0 else offset

			if (showLoading){
				_uiState.value = _uiState.value.copy(loading = true)
			}

			_uiState.value = _uiState.value.copy(list = _uiState.value.list)

			val result = noteTypeRepository.getList(tagsOffset, limit, null)

			_uiState.value.list.clear()

			if (result.isSuccessful && result.body() != null){

				val tags = result.body()?.data

				tags?.count()?.let {

					if ( it > 0){
						for (tag in tags){
							_uiState.value.list.add(NoteType(id = tag.id, title = tag.title))
						}
					}
				}

				_uiState.value.pagination = result.body()?.pagination


				_uiState.value = _uiState.value.copy(
					loading = false,
					list = _uiState.value.list,
					offset = tagsOffset,
					pagination = _uiState.value.pagination)
			}
		}
		catch (_: Exception){}
	}
}