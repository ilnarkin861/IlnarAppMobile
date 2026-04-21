package ru.ilnarkin.ilnarapp.ui.components

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.models.FileInfo
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.FileViewModel
import ru.ilnarkin.ilnarapp.viewModels.TopBarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FileManagerComponent(
	fileViewModel: FileViewModel = koinViewModel(),
	topBarViewModel: TopBarViewModel = koinViewModel(),
	filesChanged: (files: List<FileInfo>) -> Unit,
	close: () -> Unit)
{
	val state by fileViewModel.uiState.collectAsState()
	val selectedFilesState by fileViewModel.selectedFiles.collectAsState()
	val listState = rememberLazyGridState()
	val lazyPagingItems = fileViewModel.filesFlow.collectAsLazyPagingItems()
	val loadState = lazyPagingItems.loadState
	val isInitialLoading = loadState.refresh is LoadState.Loading
	val isPaginationLoading = loadState.append is LoadState.Loading
	val isEmptyFiles = loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0
	var uploadDialogVisible by rememberSaveable { mutableStateOf(false) }


	LaunchedEffect(Unit) {
		topBarViewModel.update(
			title = "Изображения",
			showBack = true,
			onBack = {
				fileViewModel.clearSelection()
				close()
			}
		)
	}


	LaunchedEffect(selectedFilesState.size) {
		if (selectedFilesState.isNotEmpty()) {
			topBarViewModel.update(
				title = "Выбрано: ${selectedFilesState.size}",
				showBack = true,
				isSelectionMode = true,
				onBack = { fileViewModel.clearSelection() },
				onChange = {
					Log.d("selectedFiles", selectedFilesState.size.toString())
					filesChanged(selectedFilesState.toList())
					fileViewModel.clearSelection()
					close()
				}
			)
		}

		else {
			topBarViewModel.update(
				title = "Изображения",
				showBack = true,
				isSelectionMode = false,
				onBack = {
					fileViewModel.clearSelection()
					close()
				}
			)
		}
	}


	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(color = AppTheme.colors.appBgColor)
			.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding))
	{
		BackHandler {
			// добавить проверку, закрывать если только не идет загрузка
			fileViewModel.clearSelection()
			close()
		}

		if (isEmptyFiles){
			Box(
				modifier = Modifier
					.background(AppTheme.colors.appBgColor)
					.fillMaxSize(),
				contentAlignment = Alignment.Center)
			{
				MessageComponent(text = "Файлов нет")
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
			Column(
				modifier = Modifier.fillMaxSize())
			{
				LazyVerticalGrid(
					modifier = Modifier.weight(1f),
					state = listState,
					columns = GridCells.Fixed(3),
					contentPadding = PaddingValues(top = 20.dp))
				{
					items(
						count = lazyPagingItems.itemCount,
						key = lazyPagingItems.itemKey { it.id })
					{ index ->

						val url = lazyPagingItems[index]?.url ?: return@items
						val isSelected = remember(selectedFilesState) { selectedFilesState.contains(lazyPagingItems[index]) }

						Row(modifier = Modifier.fillMaxWidth())
						{
							FileItemComponent(
								url,
								isSelected,
								onClick = {
									if (!selectedFilesState.isEmpty()){
										lazyPagingItems[index]?.let { fileViewModel.toggleSelection(it) }
									}
								},
								onLongClick = {
									if (selectedFilesState.isEmpty()){
										lazyPagingItems[index]?.let { fileViewModel.toggleSelection(it) }
									}
								}
							)
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
							modifier = Modifier.padding(bottom = 80.dp)
						) {  }
					}
				}
			}
		}


		FloatingActionButton(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.absolutePadding(bottom = 20.dp, right = 20.dp)
				.alpha(0.7f),
			containerColor = AppTheme.colors.primaryColor,
			contentColor = Color.White,
			shape = CircleShape,
			elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),

			onClick = {
				uploadDialogVisible = true
			})
		{
			Icon(
				modifier = Modifier.size(25.dp),
				painter = painterResource(R.drawable.ic_plus),
				contentDescription = "Добавить")
		}
	}


	if (uploadDialogVisible){
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
				FileUploadComponent(
					onUpload = {

						Log.d("onUpload", "onUpload")

						fileViewModel.refreshData()

						snapshotFlow { lazyPagingItems.loadState.refresh }
							.filter { it is LoadState.Loading }
							.first()

						snapshotFlow { lazyPagingItems.loadState.refresh }
							.filter { it is LoadState.NotLoading }
							.first()

						listState.animateScrollToItem(0)

						uploadDialogVisible = false
					},
					close = {
						uploadDialogVisible = false
					}
				)
			}
		}
	}


	AlertComponent(
		success = state.success,
		message = state.message,
		visible = state.showAlert,
		action = { fileViewModel.dismissAlert()	}
	)
}