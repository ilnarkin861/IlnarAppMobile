package ru.ilnarkin.ilnarapp.ui.screens

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
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.Archive
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.services.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.ItemFormComponent
import ru.ilnarkin.ilnarapp.ui.components.ListItemComponent
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.MessageComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.ArchiveViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArchiveScreen(
	navController: NavController,
	archiveViewModel: ArchiveViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject())
{

	val limit = 100
	val state by archiveViewModel.uiState.collectAsState()
	val snackBarHostState = remember { SnackbarHostState() }
	val listState = rememberLazyListState()
	var modalFormLabel by rememberSaveable { mutableStateOf("") }
	var formDialogVisible by rememberSaveable { mutableStateOf(false) }
	val itemId = rememberSaveable { mutableStateOf("") }
	val itemText = rememberSaveable { mutableStateOf("") }


	LaunchedEffect(Unit) {
		errorManager.errorEvent.collect { error ->
			when(error) {
				NetworkErrorType.NO_INTERNET,  NetworkErrorType.SERVER_ERROR ->{
					val message = if (error == NetworkErrorType.NO_INTERNET) NO_INTERNET_ERROR_MESSAGE else  SERVER_ERROR_MESSAGE

					snackBarHostState.showSnackbar(message)
				}

				NetworkErrorType.UNAUTHORIZED -> {
					archiveViewModel.dismissAlert()

					navController.navigate(NavRoutes.LoginScreen.route) {
						popUpTo(0) { inclusive = true }

						launchSingleTop = true
					}

					return@collect
				}
			}
		}
	}


	LaunchedEffect(Unit) {
		if (state.list.isEmpty()){
			archiveViewModel.getArchivesList(state.offset, limit)
		}
	}


	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding)
			.background(AppTheme.colors.appBgColor))
	{

		if (!state.loading && !state.list.isEmpty()){
			LazyColumn(
				state = listState,
				contentPadding = PaddingValues(top = 30.dp, bottom = 30.dp))
			{

				state.pagination?.let {
					if (it.hasPreviousPage){
						item {
							Row(Modifier.padding(bottom = 25.dp)
							) {
								LoadButtonComponent(nextButton = false, action = {
									archiveViewModel.setActionType(ActionType.READ)

									archiveViewModel.getArchivesList(state.offset - limit, limit, false)
								})
							}
						}
					}
				}

				itemsIndexed(state.list){index, item ->
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(vertical = 10.dp))
					{
						ListItemComponent(
							item.title,

							editAction = {
								val archive = archiveViewModel.getArchiveById(item.id)

								if (archive != null) {
									archiveViewModel.setActionType(ActionType.UPDATE)
									itemText.value = archive.title
									itemId.value = archive.id
									modalFormLabel = "Изменить архив"
									formDialogVisible = true
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
						HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
					}
				}

				state.pagination?.let {
					if (it.hasNextPage){
						item {
							Row(modifier = Modifier.padding(top = 25.dp, bottom = 30.dp))
							{
								LoadButtonComponent(action = {
									archiveViewModel.setActionType(ActionType.READ)

									archiveViewModel.getArchivesList(state.offset + limit, limit, false)
								})
							}
						}
					}
				}
			}
		}

		if (!state.loading && state.list.isEmpty()){
			Box(
				modifier = Modifier
					.background(AppTheme.colors.appBgColor)
					.fillMaxSize(),
				contentAlignment = Alignment.Center)
			{
				MessageComponent("Архивов нет")
			}
		}

		if (state.loading){
			Box(
				modifier = Modifier
					.background(AppTheme.colors.appBgColor)
					.fillMaxSize(),
				contentAlignment = Alignment.Center)
			{
				ProgressIndicatorComponent(60, AppTheme.colors.primaryColor)
			}
		}

		FloatingActionButton(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.absolutePadding(bottom = 20.dp, right = 20.dp)
				.alpha(0.6f),
			containerColor = AppTheme.colors.primaryColor,
			contentColor = Color.White,
			shape = CircleShape,
			elevation = FloatingActionButtonDefaults
				.elevation(0.dp, 0.dp, 0.dp, 0.dp),

			onClick = {
				archiveViewModel.setActionType(ActionType.CREATE)
				modalFormLabel = "Добавить архив"
				itemText.value = ""
				formDialogVisible = true
			})
		{
			Icon(
				modifier = Modifier.size(25.dp),
				painter = painterResource(R.drawable.ic_plus),
				contentDescription = "Добавить")
		}

		SnackbarHost(
			modifier = Modifier
				.padding(16.dp)
				.align(Alignment.BottomCenter),
			hostState = snackBarHostState)
		{data ->
			Snackbar(
				snackbarData = data,
				containerColor = AppTheme.colors.primaryColor,
				contentColor = Color.White
			)
		}
	}// Box


	AlertComponent(
		success = state.success,
		message = state.message,
		visible = state.showAlert,
		action = { archiveViewModel.dismissAlert()	}
	)


	if (formDialogVisible){
		BasicAlertDialog(
			onDismissRequest = {},
			properties = DialogProperties(
				dismissOnBackPress = false,
				dismissOnClickOutside = false
			))
		{
			Surface(
				shape = MaterialTheme.shapes.small,
				tonalElevation = AlertDialogDefaults.TonalElevation)
			{
				ItemFormComponent(
					itemText.value,
					modalFormLabel,
					action = {text->

						if (state.actionType == ActionType.CREATE){

							val createdArchive = archiveViewModel.createArchive(Archive(title = text))

							if (createdArchive != null){
								archiveViewModel.getArchivesList(0, limit)
							}
						}

						if (state.actionType == ActionType.UPDATE){

							val updatedArchive = archiveViewModel.updateArchive(Archive(id = itemId.value, title = text))

							if (updatedArchive != null){
								archiveViewModel.getArchivesList(state.offset, limit)
							}
						}

						formDialogVisible = false

					},
					close = { formDialogVisible = false }
				)
			}
		}
	}
}