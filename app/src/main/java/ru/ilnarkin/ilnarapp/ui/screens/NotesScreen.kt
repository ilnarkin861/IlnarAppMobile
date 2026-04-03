package ru.ilnarkin.ilnarapp.ui.screens

import android.os.Build
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.navigation.NavController
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.services.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.MessageComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteDetailsComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteFilterFormComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteFormComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteItemComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.ArchiveViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteFilterViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteTypeViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteViewModel
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel
import ru.ilnarkin.ilnarapp.viewModels.TopBarViewModel


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen(
	navController: NavController,
	noteViewModel: NoteViewModel = koinViewModel(),
	noteTypeViewModel: NoteTypeViewModel = koinViewModel(),
	tagViewModel: TagViewModel = koinViewModel(),
	archiveViewModel: ArchiveViewModel = koinViewModel(),
	topBarViewModel: TopBarViewModel = koinViewModel(),
	noteFilterViewModel: NoteFilterViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject())
{
	val title = stringResource(R.string.notes_title)
	val noteCreateTitle = stringResource(R.string.note_create_title)
	val noteUpdateTitle = stringResource(R.string.note_update_title)
	val tagsLimit = 10
	val scope = rememberCoroutineScope()
	val listState = rememberLazyListState()
	val snackBarHostState = remember { SnackbarHostState() }
	val noteViewModelState by noteViewModel.uiState.collectAsState()
	val noteTypeViewModelState by noteTypeViewModel.uiState.collectAsState()
	val tagViewModelState by tagViewModel.uiState.collectAsState()
	val noteFilterViewModelState by noteFilterViewModel.uiState.collectAsState()
	var noteFormVisible by rememberSaveable { mutableStateOf(false) }
	var noteFormLoading by rememberSaveable { mutableStateOf(false) }
	var showNoteDetailsSheet by rememberSaveable { mutableStateOf(false) }
	val noteDetailsSheetState = rememberModalBottomSheetState()
	var noteDetailsLoading by rememberSaveable { mutableStateOf(false) }
	var noteFilterFormVisible by rememberSaveable { mutableStateOf(false) }
	var noteFilterFormLoading by rememberSaveable { mutableStateOf(false) }
	val lazyPagingItems = noteViewModel.notesFlow.collectAsLazyPagingItems()
	val loadState = lazyPagingItems.loadState
	val isInitialLoading = loadState.refresh is LoadState.Loading
	val isPaginationLoading = loadState.append is LoadState.Loading
	val isEmptyNotes = loadState.refresh is LoadState.NotLoading && lazyPagingItems.itemCount == 0
	var pendingScrollToId by remember { mutableStateOf<String?>(null) }
	var saving by rememberSaveable { mutableStateOf(false) }



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
				NetworkErrorType.NO_INTERNET, NetworkErrorType.SERVER_ERROR -> {
					showNoteDetailsSheet = false
					noteDetailsLoading = false
					noteFormLoading = false
					noteFormVisible = false
					saving = false
					val message = if (error == NetworkErrorType.NO_INTERNET) NO_INTERNET_ERROR_MESSAGE else SERVER_ERROR_MESSAGE
					snackBarHostState.showSnackbar(message)
				}

				NetworkErrorType.UNAUTHORIZED -> {
					noteTypeViewModel.dismissAlert()

					noteViewModel.dismissAlert()

					navController.navigate(NavRoutes.LoginScreen.route) {
						popUpTo(0) { inclusive = true }

						launchSingleTop = true
					}

					return@collect
				}
			}
		}
	}


	LaunchedEffect(noteFormVisible) {
		if (!noteFormVisible){
			topBarViewModel.update(
				title = title,
				showBack = false,
				onBack = {}
			)
		}
	}


	Box(
		modifier = Modifier
			.fillMaxSize()
			.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding))
	{

		if (isEmptyNotes){
			Box(
				modifier = Modifier
					.background(AppTheme.colors.appBgColor)
					.fillMaxSize(),
				contentAlignment = Alignment.Center)
			{
				MessageComponent(text = "Записей нет")
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
				contentPadding = PaddingValues(top = 30.dp))
			{
				items(count = lazyPagingItems.itemCount,
					key = lazyPagingItems.itemKey { it.id }) {index ->
					lazyPagingItems[index]?.let {
						NoteItemComponent(
							it,
							viewAction = {
								showNoteDetailsSheet = true
								noteDetailsLoading = true

								lazyPagingItems[index]?.let { it1 -> noteViewModel.getNoteById(it1.id) }

								noteDetailsLoading = false
							},

							editAction = {
								val result = lazyPagingItems[index]?.let { it1 -> noteViewModel.getNoteById(it1.id) }

								if (result != null){
									noteViewModel.setActionType(ActionType.UPDATE)

									topBarViewModel.update(
										title = noteUpdateTitle,
										showBack = true,
										onBack = {
											if (!saving){
												noteFormVisible = false
												topBarViewModel.reset()
											}
										}
									)

									noteFormVisible = true
								}
							},

							deleteAction = {
								val isDeleted = lazyPagingItems[index]?.let { it1 -> noteViewModel.deleteNote(it1.id) }

								if (isDeleted == true){
									noteViewModel.refreshData()
								}
							})
					}
				}

				if (isPaginationLoading){
					item {
						Row(
							modifier = Modifier
								.padding(vertical = 15.dp)
								.fillMaxWidth()
								.height(25.dp),
							horizontalArrangement = Arrangement.Center)
						{
							ProgressIndicatorComponent(25, AppTheme.colors.colorGrey)
						}
					}
				}
			}
		}


		Column(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(20.dp),
			horizontalAlignment = Alignment.End,
			verticalArrangement = Arrangement.spacedBy(10.dp))
		{
			BadgedBox(
				modifier = Modifier.align(Alignment.CenterHorizontally),
				badge = {
					if (noteFilterViewModelState.filterApplied){
						Badge(
							modifier = Modifier
								.size(12.dp)
								.offset(x = (-1).dp, y = 1.dp),
							containerColor = AppTheme.colors.dangerColor
						)
					}
				}
			) {
				SmallFloatingActionButton(
					shape = CircleShape,
					containerColor = Color.White,
					contentColor = AppTheme.colors.primaryColor,
					onClick = {

						scope.launch {
							noteFilterFormVisible = true

							noteFilterFormLoading = true

							val noteTypes = noteTypeViewModel.getNoteTypesList(0, 10)

							if (!noteTypes.isEmpty()){
								if (noteFilterViewModelState.selectableNoteTypes.isEmpty()){
									noteFilterViewModel.addNoteTypes(noteTypes)
								}

								if (noteFilterViewModelState.selectableArchives.isEmpty()){
									val archives = archiveViewModel.getArchivesList(0, 100)
									noteFilterViewModel.addArchives(archives)
								}

								if (noteFilterViewModelState.selectableTags.isEmpty()){
									val tags = tagViewModel.getTagsList(0, tagsLimit)
									val hasNextTags = tagViewModel.uiState.value.pagination?.hasNextPage ?: false
									noteFilterViewModel.addTags(tags, hasNextTags)
								}
							}

							else{
								noteFilterFormVisible = false
							}

							noteFilterFormLoading = false
						}
					})
				{
					Icon(painter = painterResource(R.drawable.ic_filter), contentDescription = "Filter")
				}
			}

			FloatingActionButton(
				modifier = Modifier.alpha(0.6f),
				containerColor = AppTheme.colors.primaryColor,
				contentColor = Color.White,
				shape = CircleShape,
				elevation = FloatingActionButtonDefaults.elevation(0.dp, 0.dp, 0.dp, 0.dp),

				onClick = {

					topBarViewModel.update(
						title = noteCreateTitle,
						showBack = true,
						onBack = {
							if (!saving){
								noteFormVisible = false
								topBarViewModel.reset()
							}
						})

					noteViewModel.setActionType(ActionType.CREATE)

					noteViewModel.clearNote()

					noteFormVisible = true
				}) {
				Icon(
					modifier = Modifier.size(25.dp),
					painter = painterResource(R.drawable.ic_plus),
					contentDescription = "Добавить")
			}
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
	}

	AlertComponent(
		success = noteViewModelState.success,
		message = if (noteViewModelState.showAlert) noteViewModelState.message else noteTypeViewModelState.message,
		visible = noteViewModelState.showAlert || noteTypeViewModelState.showAlert,
		action = {
			noteTypeViewModel.dismissAlert()
			noteViewModel.dismissAlert()
			saving = false
		}
	)


	// Note form
	if (noteFormVisible){
		NoteFormComponent(
			note = noteViewModelState.data,
			action = {note ->

				saving = true

				if (noteViewModelState.actionType == ActionType.CREATE){

					val createdNote = noteViewModel.createNote(note)

					if (createdNote != null){

						if (noteFilterViewModelState.filterApplied){
							noteFilterViewModel.resetFilter()
						}

						noteViewModel.updateFilter(null)

						noteViewModel.refreshData()

						snapshotFlow { lazyPagingItems.loadState.refresh }
							.filter { it is LoadState.Loading }
							.first()

						snapshotFlow { lazyPagingItems.loadState.refresh }
							.filter { it is LoadState.NotLoading }
							.first()

						listState.animateScrollToItem(0)
					}
				}

				if (noteViewModelState.actionType == ActionType.UPDATE){

					val updatedNote = noteViewModel.updateNote(note)

					if (updatedNote != null){

						noteViewModel.refreshData()

						pendingScrollToId = updatedNote.id
					}
				}

				saving = false

				noteFormVisible = false
			},

			close = {
				noteFormVisible = false
				topBarViewModel.reset()
			}
		)
	}


	// Note filter form
	if (noteFilterFormVisible){
		Box(
			modifier = Modifier
				.fillMaxSize()
				.background(AppTheme.colors.appBgColor))
		{

			BackHandler { noteFilterFormVisible = false }

			if (noteFilterFormLoading){
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center)
				{
					ProgressIndicatorComponent(50, AppTheme.colors.primaryColor)
				}
			}

			else{
				Column(
					modifier = Modifier
						.verticalScroll(rememberScrollState())
						.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding)
						.fillMaxSize())
				{

					Row(
						modifier = Modifier
							.padding(top = 15.dp, bottom = 20.dp)
							.fillMaxWidth(),
						horizontalArrangement = Arrangement.Center)
					{
						Text(
							text = "Фильтр",
							color = AppTheme.colors.colorGrey,
							style = AppTheme.typography.formTitleText
						)
					}
					Row(modifier = Modifier.fillMaxWidth())
					{
						HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
					}

					NoteFilterFormComponent(
						viewModel = noteFilterViewModel,

						loadTags = {
							val tags = tagViewModel.getTagsList(tagViewModelState.offset + tagsLimit, tagsLimit)

							val hasNextTags = tagViewModel.uiState.value.pagination?.hasNextPage ?: false

							noteFilterViewModel.addTags(tags, hasNextTags)

							tags.toMutableList()
						},

						action = {
							noteFilterFormVisible = false

							noteViewModel.updateFilter(noteFilterViewModel.uiState.value.noteFilter)

							lazyPagingItems.refresh()
						},

						resetFilter = {
							noteFilterFormVisible = false

							noteViewModel.updateFilter(null)

							lazyPagingItems.refresh()
						}
					)
				}
			}
		}
	}


	// Details sheet
	if (showNoteDetailsSheet){
		ModalBottomSheet(
			onDismissRequest = { showNoteDetailsSheet = false },
			containerColor = Color.White,
			sheetState = noteDetailsSheetState)
		{
			if (noteDetailsLoading){
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(200.dp),
					contentAlignment = Alignment.Center)
				{
					ProgressIndicatorComponent(50, AppTheme.colors.primaryColor)
				}
			}

			if(!noteDetailsLoading && noteViewModelState.data != null){
				NoteDetailsComponent(noteViewModelState.data)
			}
		}
	}
}