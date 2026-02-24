package ru.ilnarkin.ilnarapp.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.WelcomeActivity
import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.ItemFormComponent
import ru.ilnarkin.ilnarapp.ui.components.ListItemComponent
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.MessageComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.viewModels.ArchiveViewModel


@Composable
fun ArchiveScreen(
	archiveViewModel: ArchiveViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject()
) {

	val limit = 100

	val context = LocalContext.current

	val listState = rememberLazyListState()

	var modalFormLabel by remember { mutableStateOf("") }

	val dialogState = rememberMaterialDialogState()

	var actionType by remember { mutableStateOf(ActionType.READ) }

	val state by archiveViewModel.uiState.collectAsState()

	val itemId = remember { mutableStateOf("") }

	val itemText = remember { mutableStateOf("") }

	val snackBarHostState = remember { SnackbarHostState() }


	LaunchedEffect(Unit) {
		errorManager.errorEvent.collect { error ->
			when(error) {
				NetworkErrorType.NO_INTERNET ->
					snackBarHostState.showSnackbar(NO_INTERNET_ERROR_MESSAGE)

				NetworkErrorType.SERVER_ERROR ->
					snackBarHostState.showSnackbar(SERVER_ERROR_MESSAGE)

				NetworkErrorType.UNAUTHORIZED -> {
					context.startActivity(Intent(context, WelcomeActivity::class.java).apply {
						flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
					})
					return@collect
				}
			}
		}
	}


	LaunchedEffect(Unit) {
		archiveViewModel.getArchivesList(state.offset, limit)
	}


	Box(Modifier.fillMaxSize().padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))) {

		if (!state.loading && !state.list.isEmpty()){
			LazyColumn(
				state = listState,
				contentPadding = PaddingValues(top = 30.dp, bottom = 80.dp)
			) {

				state.pagination?.let {
					if (it.hasPreviousPage){
						item {
							Row(Modifier.padding(bottom = 25.dp)) {
								LoadButtonComponent(nextButton = false, action = {
									actionType = ActionType.READ

									archiveViewModel.getArchivesList(state.offset - limit, limit, false)
								})
							}
						}
					}
				}

				itemsIndexed(state.list){index, item ->

					Row(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
						ListItemComponent(
							item.title,

							editAction = {
								val archive = archiveViewModel.getArchiveById(item.id)

								if (archive != null) {
									actionType = ActionType.UPDATE
									itemText.value = archive.title
									itemId.value = archive.id
									modalFormLabel = "Изменить архив"
									dialogState.show()
								}
							},

							deleteAction = {
								val isDeleted = archiveViewModel.deleteArchive(item.id)

								if (isDeleted){
									val offset = if (state.list.size == 1) state.offset - limit else state.offset

									archiveViewModel.getArchivesList(offset, limit, false)
								}
							}
						)
					}

					if (index != state.list.count() -1){
						HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
					}
				}

				state.pagination?.let {
					if (it.hasNextPage){
						item {
							Row(Modifier.padding(top = 25.dp, bottom = 30.dp)) {
								LoadButtonComponent(action = {
									actionType = ActionType.READ

									archiveViewModel.getArchivesList(state.offset + limit, limit, false)
								})
							}
						}
					}
				}
			}
		}

		if (!state.loading && state.list.isEmpty()){
			Box(modifier = Modifier.background(colorResource(R.color.app_bg_color)).fillMaxSize(),
				contentAlignment = Alignment.Center){
				MessageComponent("Архивов нет")
			}
		}

		if (state.loading){
			Box(
				modifier = Modifier.background(colorResource(R.color.app_bg_color)).fillMaxSize(),
				contentAlignment = Alignment.Center){
				ProgressIndicatorComponent(60, colorResource(R.color.primary_color))
			}
		}


		FloatingActionButton(
			containerColor = colorResource(R.color.primary_color),
			contentColor = Color.White,
			shape = CircleShape,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.absolutePadding(bottom = 20.dp, right = 20.dp)
				.background(Color.Transparent),
			onClick = {
				actionType = ActionType.CREATE
				modalFormLabel = "Добавить архив"
				itemText.value = ""
				dialogState.show()
			}) {
			Icon(
				modifier = Modifier.size(25.dp),
				painter = painterResource(R.drawable.ic_plus),
				contentDescription = "Добавить") }


		SnackbarHost(
			hostState = snackBarHostState,
			modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter)
		){data ->
			Snackbar(
				snackbarData = data,
				containerColor = colorResource(R.color.primary_color),
				contentColor = Color.White
			)
		}
	}// Box


	AlertComponent(
		success = state.success,
		message = state.message,
		showed = state.showAlert,
		action = { archiveViewModel.dismissAlert()	}
	)


	MaterialDialog(
		dialogState = dialogState,
		shape = MaterialTheme.shapes.small,
		onCloseRequest = { MaterialDialogState.Saver() },
	){
		ItemFormComponent(
			itemText.value,
			modalFormLabel,
			action = {text->

				if (actionType == ActionType.CREATE){

					val createdArchive = archiveViewModel.createArchive(Archive(title = text))

					if (createdArchive != null){
						archiveViewModel.getArchivesList(0, limit)
					}
				}

				if (actionType == ActionType.UPDATE){

					val updatedArchive = archiveViewModel.updateArchive(Archive(id = itemId.value, title = text))

					if (updatedArchive != null){
						archiveViewModel.getArchivesList(state.offset, limit)
					}
				}

				dialogState.hide()

			},
			close = { dialogState.hide() }
		)
	}
}