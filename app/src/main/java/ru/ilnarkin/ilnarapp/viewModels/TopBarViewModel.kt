package ru.ilnarkin.ilnarapp.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.ilnarkin.ilnarapp.ui.TopBarState


class TopBarViewModel : ViewModel() {
	var uiState by mutableStateOf(TopBarState())
		private set


	fun update(title: String, showBack: Boolean = false, onBack: () -> Unit = {}) {
		uiState = uiState.copy(
			title = title,
			showBackButton = showBack,
			onBackClick = onBack
		)
	}


	fun reset() {
		uiState = TopBarState()
	}

}