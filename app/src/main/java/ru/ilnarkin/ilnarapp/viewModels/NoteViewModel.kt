package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ilnarkin.ilnarapp.helpers.buildString
import ru.ilnarkin.ilnarapp.models.Info
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.repositories.NoteRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class NoteViewModel(private val noteRepository: NoteRepository) : ViewModel(){

	private val _uiState = MutableStateFlow(AppUiState<Note>())
	val uiState: StateFlow<AppUiState<Note>> = _uiState.asStateFlow()


	suspend fun getNotesList(offset: Int, limit: Int, showLoading: Boolean = true){

		try {
			val tagsOffset = if (offset <= 0) 0 else offset

			if (showLoading){
				_uiState.value = _uiState.value.copy(loading = true)
			}

			_uiState.value = _uiState.value.copy(list = _uiState.value.list)

			val result = noteRepository.getList(tagsOffset, limit, null)

			_uiState.value.list.clear()

			if (result.isSuccessful && result.body() != null){

				val notes = result.body()?.data

				notes?.count()?.let {

					if ( it > 0){
						for (note in notes){
							_uiState.value.list.add(Note(
								id = note.id,
								title = note.title,
								text = note.text,
								noteType = note.noteType,
								date = note.date,
								archive = note.archive,
								tags = note.tags
								))
						}
					}
				}

				_uiState.value.pagination = result.body()?.pagination


				_uiState.value = _uiState.value.copy(
					loading = false,
					list = _uiState.value.list,
					offset = tagsOffset,
					success = true,
					pagination = _uiState.value.pagination)
			}
		}
		catch (_: Exception){}
	}


	suspend fun createNote(note: Note){

		try {
			_uiState.value = _uiState.value.copy(message = "", success = false, data = null)

			val result = noteRepository.create(note)

			if (result.isSuccessful){
				_uiState.value = _uiState.value.copy(
					success = true,
					message = "Запись успешно добавлена",
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
				message = "Ошибка при добавлении записи")
		}
	}

}