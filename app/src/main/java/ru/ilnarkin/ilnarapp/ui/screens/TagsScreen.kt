package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.yield
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.services.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.ItemFormComponent
import ru.ilnarkin.ilnarapp.ui.components.ListItemComponent
import ru.ilnarkin.ilnarapp.ui.components.MessageComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel
import ru.ilnarkin.ilnarapp.viewModels.TopBarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagsScreen(
	navController: NavController,
	tagViewModel: TagViewModel = koinViewModel(),
	topBarViewModel: TopBarViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject())
{
	val title = stringResource(R.string.tags_title)
	val state by tagViewModel.uiState.collectAsState()
	val snackBarHostState = remember { SnackbarHostState() }
	val listState = rememberLazyListState()
	var formDialogVisible by rememberSaveable { mutableStateOf(false) }
	val itemId = rememberSaveable { mutableStateOf("") }
	val itemText = rememberSaveable { mutableStateOf("") }
	var modalFormLabel by rememberSaveable { mutableStateOf("") }
	val lazyPagingItems = tagViewModel.tagsFlow.collectAsLazyPagingItems()
	val loadState = lazyPagingItems.loadState
	val isInitialLoading = loadState.refresh is LoadState.Loading
	val isPaginationLoading = loadState.append is LoadState.Loading
	val isEmptyTags = loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0
	var pendingScrollToId by remember { mutableStateOf<String?>(null) }



	LaunchedEffect(Unit) {
		topBarViewModel.update(
			title = title,
			showBack = false,
			onBack = {}
		)
	}


	LaunchedEffect(pendingScrollToId) {
		val idToFind = pendingScrollToId ?: return@LaunchedEffect

		snapshotFlow { lazyPagingItems.loadState.refresh }
			.filter { it is LoadState.NotLoading }
			.first()

		snapshotFlow {
			lazyPagingItems.itemSnapshotList.items.any { it.id == idToFind }
		}
			.filter { it }
			.first()

		yield()

		val index = lazyPagingItems.itemSnapshotList.items.indexOfFirst { it.id == idToFind }

		if (index != -1) {
			listState.animateScrollToItem(index)
		}

		pendingScrollToId = null
	}


	LaunchedEffect(Unit) {
		errorManager.errorEvent.collect { error ->
			when(error) {
				NetworkErrorType.NO_INTERNET,  NetworkErrorType.SERVER_ERROR ->{
					val message = if (error == NetworkErrorType.NO_INTERNET) NO_INTERNET_ERROR_MESSAGE else  SERVER_ERROR_MESSAGE

					snackBarHostState.showSnackbar(message)
				}

				NetworkErrorType.UNAUTHORIZED -> {
					tagViewModel.dismissAlert()

					navController.navigate(NavRoutes.LoginScreen.route) {
						popUpTo(0) { inclusive = true }

						launchSingleTop = true
					}

					return@collect
				}
			}
		}
	}


	Box(
		modifier = Modifier.fillMaxSize()
			.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding)
			.background(AppTheme.colors.appBgColor))
	{
		if (isEmptyTags){
			Box(
				modifier = Modifier
					.background(AppTheme.colors.appBgColor)
					.fillMaxSize(),
				contentAlignment = Alignment.Center)
			{
				MessageComponent(text = "Тегов нет")
			}
		}

		if (isInitialLoading){
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center)
			{
				ProgressIndicatorComponent(60, AppTheme.colors.primaryColor)
			}
		}

		else{
			LazyColumn(
				state = listState,
				contentPadding = PaddingValues(top = 30.dp, bottom = 30.dp))
			{
				items(
					count = lazyPagingItems.itemCount,
					key = lazyPagingItems.itemKey { it.id }){index ->

					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(vertical = 10.dp))
					{

						lazyPagingItems[index]?.let {
							ListItemComponent(
								it.title,

								editAction = {
									val tag = tagViewModel.getTagById(it.id)

									if (tag != null) {
										tagViewModel.setActionType(ActionType.UPDATE)
										itemText.value = tag.title
										itemId.value = tag.id
										modalFormLabel = "Изменить тег"
										formDialogVisible = true
									}
								},

								deleteAction = {
									val isDeleted = tagViewModel.deleteTag(it.id)

									if (isDeleted){
										tagViewModel.refreshData()
									}
								}
							)
						}
					}

					if (index != lazyPagingItems.itemCount-1){
						HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
					}
				}

				if (isPaginationLoading) {
					item {
						Row(
							modifier = Modifier
								.padding(vertical = 20.dp)
								.fillMaxWidth()
								.height(25.dp),
							horizontalArrangement = Arrangement.Center)
						{
							ProgressIndicatorComponent(25, AppTheme.colors.colorGrey)
						}
					}
				}

				item {
					Row(
						modifier = Modifier.padding(bottom = 50.dp)
					) {  }
				}
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
			elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),

			onClick = {
				tagViewModel.setActionType(ActionType.CREATE)
				modalFormLabel = "Добавить тег"
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
		action = { tagViewModel.dismissAlert()	}
	)


	if (formDialogVisible){
		BasicAlertDialog(
			onDismissRequest = {},
			properties = DialogProperties(
				dismissOnBackPress = false,
				dismissOnClickOutside = false))
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

							val createdTag = tagViewModel.createTag(Tag(title = text))

							if (createdTag != null){

								tagViewModel.refreshData()

								snapshotFlow { lazyPagingItems.loadState.refresh }
									.filter { it is LoadState.Loading }
									.first()

								snapshotFlow { lazyPagingItems.loadState.refresh }
									.filter { it is LoadState.NotLoading }
									.first()

								listState.animateScrollToItem(0)
							}
						}

						if (state.actionType == ActionType.UPDATE){

							val updatedTag = tagViewModel.updateTag(Tag(id = itemId.value, title = text))

							if (updatedTag != null){
								tagViewModel.refreshData()

								pendingScrollToId = updatedTag.id
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

