package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.buildString
import ru.ilnarkin.ilnarapp.models.Info
import ru.ilnarkin.ilnarapp.models.UserLoginData
import ru.ilnarkin.ilnarapp.models.UserInfo
import ru.ilnarkin.ilnarapp.network.TokenManager
import ru.ilnarkin.ilnarapp.network.UserHttpService
import ru.ilnarkin.ilnarapp.ui.AppUiState


class UserViewModel(
	private val tokenManager: TokenManager,
	private val userHttpService: UserHttpService
) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<UserInfo>())
	val uiState: StateFlow<AppUiState<UserInfo>> = _uiState.asStateFlow()


	suspend fun checkAuth(){

		try {

			val result = userHttpService.checkAuth()

			if (result.isSuccessful){
				_uiState.value = _uiState.value.copy(isAuth = true)
			}

		}catch (_: Exception){
			_uiState.value = _uiState.value.copy(
				success = false,
				message = DEFAULT_ERROR_MESSAGE
			)
		}
	}


	suspend fun login(userAuthData: UserLoginData){

		try {
			_uiState.value = _uiState.value.copy(message = "")

			val result = userHttpService.login(userAuthData)

			if (result.isSuccessful){
				tokenManager.saveAuthToken(result.body()?.token)
				_uiState.value = _uiState.value.copy(success = true)
			}

			else {
				val errorBody = result.errorBody()?.string()
				val errorResponse = Gson().fromJson(errorBody, Info::class.java)

				val message = buildString(errorResponse.messages)

				_uiState.value = _uiState.value.copy(
					success = false,
					message = message
				)
			}
		}catch (_: Exception){
			_uiState.value = _uiState.value.copy(success = false)
		}
	}


	fun clearToken(){
		tokenManager.clearAuthToken()
	}
}