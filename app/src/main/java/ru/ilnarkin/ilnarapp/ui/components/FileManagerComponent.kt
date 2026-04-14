package ru.ilnarkin.ilnarapp.ui.components

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.models.FileInfo
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.FileViewModel
import ru.ilnarkin.ilnarapp.viewModels.TopBarViewModel


@Composable
fun FileManagerComponent(
	fileViewModel: FileViewModel = koinViewModel(),
	topBarViewModel: TopBarViewModel = koinViewModel(),
	filesChanged: (files: List<FileInfo>) -> Unit,
	close: () -> Unit)
{
	val selectedFiles by fileViewModel.selectedFiles.collectAsState()
	val listState = rememberLazyGridState()
	val lazyPagingItems = fileViewModel.filesFlow.collectAsLazyPagingItems()
	val loadState = lazyPagingItems.loadState
	val isInitialLoading = loadState.refresh is LoadState.Loading
	val isPaginationLoading = loadState.append is LoadState.Loading
	val isEmptyFiles = loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0


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


	Box(
		modifier = Modifier
			.fillMaxSize()
			.background(color = AppTheme.colors.appBgColor)
			.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding))
	{
		BackHandler {
			// если только не идет загрузка
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
					state = listState,
					columns = GridCells.Fixed(3),
					modifier = Modifier.weight(1f),
					contentPadding = PaddingValues(top = 20.dp))
				{
					items(
						count = lazyPagingItems.itemCount,
						key = lazyPagingItems.itemKey { it.id }) { index ->

						val url = lazyPagingItems[index]?.url ?: return@items
						val isSelected = remember(selectedFiles) { selectedFiles.contains(lazyPagingItems[index]) }

						FileItemComponent(
							url,
							isSelected,
							onClick = {
								if (!selectedFiles.isEmpty()){
									lazyPagingItems[index]?.let { fileViewModel.toggleSelection(it) }
								}
							},
							onLongClick = {
								if (selectedFiles.isEmpty()){
									lazyPagingItems[index]?.let { fileViewModel.toggleSelection(it) }
								}
							}
						)
					}
				}
			}
		}


		FloatingActionButton(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.absolutePadding(bottom = 20.dp, right = 20.dp)
				.alpha(0.8f),
			containerColor = AppTheme.colors.primaryColor,
			contentColor = Color.White,
			shape = CircleShape,
			elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),

			onClick = {

			})
		{
			Icon(
				modifier = Modifier.size(40.dp),
				painter = painterResource(R.drawable.ic_choice_image),
				contentDescription = "Добавить")
		}
	}
}