package ru.ilnarkin.ilnarapp.viewModels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import ru.ilnarkin.ilnarapp.ui.TopBarState


class TopBarViewModel : ViewModel() {
	var uiState by mutableStateOf(TopBarState())
		private set


	fun update(title: String,
			   showBack: Boolean = false,
			   isSelectionMode: Boolean = false,
			   onBack: () -> Unit = {},
			   onChange: () -> Unit = {},
			   onDeleteSelected: () -> Unit = {}) {

		uiState = uiState.copy(
			title = title,
			showBackButton = showBack,
			onBackClick = onBack,
			onChangeClick = onChange,
			isSelectionMode = isSelectionMode,
			onDeleteSelectedClick = onDeleteSelected
		)
	}


	fun reset() {
		uiState = TopBarState()
	}
}