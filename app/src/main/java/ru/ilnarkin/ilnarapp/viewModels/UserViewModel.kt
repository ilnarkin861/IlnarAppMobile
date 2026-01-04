package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import retrofit2.Response
import ru.ilnarkin.ilnarapp.models.UserInfo
import ru.ilnarkin.ilnarapp.network.UserHttpService
import ru.ilnarkin.ilnarapp.ui.AppUiState


class UserViewModel(private val userHttpService: UserHttpService) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<UserInfo>())
	val uiState: StateFlow<AppUiState<UserInfo>> = _uiState.asStateFlow()


	suspend fun checkAuth(){

		try {
			val deferredResult : Deferred<Response<Any>> = viewModelScope.async {
				userHttpService.checkAuth()
			}

			val result = deferredResult.await()

			if (result.isSuccessful){
				_uiState.value = _uiState.value.copy(isAuth = true)
			}
		}catch (e: Exception){
			_uiState.value = _uiState.value.copy(isAuth = false)
		}
	}
}