package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ilnarkin.ilnarapp.helpers.buildString
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.models.Info
import ru.ilnarkin.ilnarapp.repositories.ArchiveRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class ArchiveViewModel(private val archiveRepository: ArchiveRepository) : ViewModel(){

	private val _uiState = MutableStateFlow(AppUiState<Archive>())
	val uiState: StateFlow<AppUiState<Archive>> = _uiState.asStateFlow()


	suspend fun getArchivesList(offset: Int, limit: Int, showLoading: Boolean = true){

		try {
			val tagsOffset = if (offset < 0) 0 else offset

			if (showLoading){
				_uiState.value = _uiState.value.copy(loading = true)
			}

			_uiState.value = _uiState.value.copy(list = _uiState.value.list)

			val result = archiveRepository.getList(tagsOffset, limit, null)

			_uiState.value.list.clear()

			if (result.isSuccessful && result.body() != null){

				val archives = result.body()?.data

				archives?.count()?.let {

					if ( it > 0){
						for (archive in archives){
							_uiState.value.list.add(Archive(id = archive.id, title = archive.title))
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


	suspend fun getArchiveById(id: String){

		try {
			_uiState.value = _uiState.value.copy(success = false, data = null)

			val result = archiveRepository.getById(id)

			if (result.isSuccessful && result.body() != null){
				_uiState.value = _uiState.value.copy(
					success = true,
					data =  result.body())
			}

			else{
				if (result.code() == 404){
					val errorBody = result.errorBody()?.string()
					val errorResponse = Gson().fromJson(errorBody, Info::class.java)

					val message = buildString(errorResponse.messages)

					_uiState.value = _uiState.value.copy(
						success = false,
						message = message)
				}
			}
		}

		catch (_: Exception){
			_uiState.value = _uiState.value.copy(
				success = false,
				message = "Ошибка при получении архива")
		}
	}


	suspend fun createArchive(archive: Archive){

		try {
			_uiState.value = _uiState.value.copy(message = "", success = false, data = null)

			val result = archiveRepository.create(archive)

			if (result.isSuccessful){
				_uiState.value = _uiState.value.copy(
					success = true,
					message = "Архив успешно добавлен",
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
				message = "Ошибка при добавлении архива")
		}
	}


	suspend fun updateArchive(archive: Archive){

		try {
			_uiState.value = _uiState.value.copy(success = false, data = null)

			val result = archiveRepository.update(archive)

			if (result.isSuccessful){
				_uiState.value = _uiState.value.copy(
					success = true,
					message = "Архив успешно обновлен"
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
				message = "Ошибка при обновлении архива")
		}
	}


	suspend fun deleteArchive(id: String){

		try {
			_uiState.value = _uiState.value.copy(success = false)

			val result = archiveRepository.delete(id)

			if (result.isSuccessful){
				_uiState.value = _uiState.value.copy(
					success = true,
					message = "Архив успешно удален"
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
				message = "Ошибка при удалении архива")
		}
	}


}