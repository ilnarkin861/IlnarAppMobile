package ru.ilnarkin.ilnarapp.services

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType


class NetworkErrorManager {
	private val _errorEvent = MutableSharedFlow<NetworkErrorType>(extraBufferCapacity = 1)
	val errorEvent = _errorEvent.asSharedFlow()

	suspend fun notifyError(errorType: NetworkErrorType) {
		_errorEvent.emit(errorType)
	}
}