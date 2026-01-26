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
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.WelcomeActivity
import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.ItemFormComponent
import ru.ilnarkin.ilnarapp.ui.components.ListItemComponent
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.MessageComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel


@Composable
fun TagsScreen(
	tagViewModel: TagViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject()
	) {

	val limit = 10

	val context = LocalContext.current

	var loading by remember { mutableStateOf(false) }

	val listState = rememberLazyListState()

	val alertTitle = remember { mutableStateOf("") }
	var showAlert by remember { mutableStateOf(false) }

	var modalFormLabel by remember { mutableStateOf("") }

	val dialogState = rememberMaterialDialogState()

	var actionType by remember { mutableStateOf(ActionType.CREATE) }

	val state by tagViewModel.uiState.collectAsState()

	val itemText = remember { mutableStateOf("") }

	val scope = rememberCoroutineScope()

	val snackBarHostState = remember { SnackbarHostState() }


	LaunchedEffect(Unit) {

		lateinit var errorJob: Job

		errorJob = launch {
			errorManager.errorEvent.collect { error ->

				when(error){
					NetworkErrorType.NO_INTERNET ->
						snackBarHostState.showSnackbar(NO_INTERNET_ERROR_MESSAGE, duration = SnackbarDuration.Long)

					NetworkErrorType.SERVER_ERROR ->
						snackBarHostState.showSnackbar(SERVER_ERROR_MESSAGE, duration = SnackbarDuration.Long)

					NetworkErrorType.UNAUTHORIZED -> {
						val intent = Intent(context, WelcomeActivity::class.java).apply {
							flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
						}

						context.startActivity(intent)

						errorJob.cancel()
					}
				}
			}
		}


		tagViewModel.getTagsList(state.offset, limit)
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
									tagViewModel.getTagsList(state.offset - limit, limit, false)
								})
							}
						}
					}
				}

				itemsIndexed(state.list){index, tag ->

					Row(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
						ListItemComponent(
							tag.id,
							tag.title,

							editAction = { text ->

								tagViewModel.getTagById(tag.id)

								if (tagViewModel.uiState.value.success){
									actionType = ActionType.UPDATE
									modalFormLabel = "Изменить тег"
									tagViewModel.uiState.value.data?.let { itemText.value = it.title }
									dialogState.show()
								}

								else{
									showAlert = true
								}
							},

							deleteAction = {
								delay(1500)
								alertTitle.value = "Тег успешно удален"
								showAlert = true
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
									tagViewModel.getTagsList(state.offset + limit, limit, false)
								})
							}
						}
					}
				}
			}
		}

		if (!loading && state.list.isEmpty()){
			Box(modifier = Modifier.background(colorResource(R.color.app_bg_color)).fillMaxSize(),
				contentAlignment = Alignment.Center){
				MessageComponent("Тегов нет")
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
				.absolutePadding(bottom = 30.dp, right = 30.dp)
				.background(Color.Transparent),
			onClick = {
				actionType = ActionType.CREATE
				modalFormLabel = "Добавить тег"
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
		showed = showAlert,
		action = {
			showAlert = false

			if (state.success){
				scope.launch {
					tagViewModel.getTagsList(state.offset, limit)
				}
			}
		}
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

					tagViewModel.createTag(Tag(title = text))
				}

				if (actionType == ActionType.UPDATE){

					// Save to db

					alertTitle.value = "Тег успешно изменен"
				}

				showAlert = true

				dialogState.hide()

			},
			close = { dialogState.hide() }
		)
	}
}

