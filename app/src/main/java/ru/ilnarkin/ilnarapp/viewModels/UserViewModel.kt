package ru.ilnarkin.ilnarapp.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import ru.ilnarkin.ilnarapp.exceptions.ApiException
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.UserInfo
import ru.ilnarkin.ilnarapp.models.UserLoginData
import ru.ilnarkin.ilnarapp.network.UserManager
import ru.ilnarkin.ilnarapp.repositories.UserRepository
import ru.ilnarkin.ilnarapp.ui.AppUiState


class UserViewModel(
	private val userManager: UserManager,
	private val userRepository: UserRepository
) : ViewModel() {

	private val _uiState = MutableStateFlow(AppUiState<UserInfo>())
	val uiState: StateFlow<AppUiState<UserInfo>> = _uiState.asStateFlow()


	suspend fun checkAuth(){

		try {

			val result = userRepository.checkAuth()

			if (result){
				_uiState.update { it.copy(
					isAuth = true
				)}
			}

		}catch (e: ApiException){
			_uiState.update { it.copy(
				success = false,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)}
		}

		catch (_: Exception){
			_uiState.update { it.copy(
				success = false,
				message = DEFAULT_ERROR_MESSAGE
			)}
		}
	}


	suspend fun login(userAuthData: UserLoginData){

		try {
			_uiState.value = _uiState.value.copy(message = "")

			val result = userRepository.login(userAuthData)

			userManager.saveAuthToken(result.token)

			_uiState.update { it.copy(
				success = true
			)}
		}

		catch (e: ApiException){
			_uiState.update { it.copy(
				success = false,
				message = e.message ?: DEFAULT_ERROR_MESSAGE
			)}
		}

		catch (_: Exception){
			_uiState.update { it.copy(
				success = false,
				message = DEFAULT_ERROR_MESSAGE
			)}
		}
	}


	fun setPinCode(pinCode: String){
		userManager.setPinCode(pinCode)
	}


	fun getPinCode(): String?{
		return userManager.getPinCode()
	}


	fun clearPinCode(){
		userManager.clearPinCode()
	}


	fun clearToken(){
		userManager.clearAuthToken()
	}
}