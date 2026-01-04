package ru.ilnarkin.ilnarapp.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType


class NetworkErrorManager {
	private val _errorEvent = MutableStateFlow(NetworkErrorType.SERVER_ERROR)
	val errorEvent = _errorEvent.asStateFlow()

	suspend fun notifyError(errorType: NetworkErrorType) {
		_errorEvent.emit(errorType)
	}
}