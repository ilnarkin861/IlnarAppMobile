package ru.ilnarkin.ilnarapp.network

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType


class NetworkErrorManager {
	private val _errorEvent = MutableSharedFlow<NetworkErrorType>()
	val errorEvent = _errorEvent.asSharedFlow()

	suspend fun notifyError(errorType: NetworkErrorType) {
		_errorEvent.emit(errorType)
	}
}