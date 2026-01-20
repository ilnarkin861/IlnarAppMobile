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
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.enums.ActionType
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

	var loading by remember { mutableStateOf(false) }

	val listState = rememberLazyListState()

	val itemText = remember { mutableStateOf("") }

	val alertTitle = remember { mutableStateOf("") }

	var showAlert by remember { mutableStateOf(false) }

	var modalFormLabel by remember { mutableStateOf("") }

	var success by remember { mutableStateOf(true) }

	val dialogState = rememberMaterialDialogState()

	var actionType by remember { mutableStateOf(ActionType.CREATE) }

	val state by tagViewModel.uiState.collectAsState()


	LaunchedEffect(Unit) {
		async {
			tagViewModel.getTagsList(state.offset, limit)
		}.await()
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
								LoadButtonComponent(nextButton = false, action = { delay(1500) })
							}
						}
					}
				}

				itemsIndexed(state.list){index, tag ->

					Row(Modifier.fillMaxWidth().padding(vertical = 10.dp)) {
						ListItemComponent(
							tag.id,
							tag.title,

							editAction = { tag ->

								delay(1500)

								actionType = ActionType.UPDATE
								modalFormLabel = "Изменить тег"
								itemText.value = tag
								dialogState.show()
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
									delay(1500)
									listState.scrollToItem(0)
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
	}// Box


	AlertComponent(
		success = success,
		message = alertTitle.value,
		showed = showAlert,
		action = {
			showAlert = false

			// еще что-то делаем, может
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
			action = {

				delay(1000)

				if (actionType == ActionType.CREATE){

					// Save to db

					alertTitle.value = "Тег успешно добавлен"
				}

				else{

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

