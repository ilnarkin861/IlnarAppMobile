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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
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
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
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


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen(
	navController: NavController,
	noteViewModel: NoteViewModel = koinViewModel(),
	noteTypeViewModel: NoteTypeViewModel = koinViewModel(),
	tagViewModel: TagViewModel = koinViewModel(),
	archiveViewModel: ArchiveViewModel = koinViewModel(),
	noteFilterViewModel: NoteFilterViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject()
) {

	val notesLimit = 10
	val tagsLimit = 10

	var showNoteFilterForm by rememberSaveable { mutableStateOf(false) }
	var noteFilterFormLoading by rememberSaveable { mutableStateOf(false) }

	val scope = rememberCoroutineScope()

	val listState = rememberLazyListState()

	var showNoteForm by rememberSaveable { mutableStateOf(false) }
	var noteFormLoading by rememberSaveable { mutableStateOf(false) }

	var showNoteDetailsSheet by rememberSaveable { mutableStateOf(false) }
	val noteDetailsSheetState = rememberModalBottomSheetState()
	var noteDetailsLoading by rememberSaveable { mutableStateOf(false) }

	val sheetTitle = rememberSaveable { mutableStateOf("") }

	val snackBarHostState = remember { SnackbarHostState() }

	val noteViewModelState by noteViewModel.uiState.collectAsState()

	val noteTypeViewModelState by noteTypeViewModel.uiState.collectAsState()

	val tagViewModelState by tagViewModel.uiState.collectAsState()

	val archiveViewModelState by archiveViewModel.uiState.collectAsState()

	val noteFilterViewModelState by noteFilterViewModel.uiState.collectAsState()


	LaunchedEffect(Unit) {
		errorManager.errorEvent.collect { error ->
			when(error) {
				NetworkErrorType.NO_INTERNET, NetworkErrorType.SERVER_ERROR -> {
					showNoteDetailsSheet = false
					noteDetailsLoading = false
					noteFormLoading = false
					showNoteForm = false
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


	LaunchedEffect(Unit) {
		if (noteViewModelState.list.isEmpty()){

			noteViewModel.getNotesList(0, notesLimit)
		}
	}


	Box(Modifier.fillMaxSize().padding(horizontal = AppTheme.dimensions.containerHorizontalPadding)) {
		if (noteViewModelState.loading){
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center){
				ProgressIndicatorComponent(60, AppTheme.colors.primaryColor)
			}
		}

		if (!noteViewModelState.loading && !noteViewModelState.list.isEmpty()){
			LazyColumn(
				state = listState,
				contentPadding = PaddingValues(top = 30.dp)) {

				noteViewModelState.pagination?.let {
					if (it.hasPreviousPage){
						item {
							Row(Modifier.padding(bottom = 25.dp)) {
								LoadButtonComponent(nextButton = false, action = {
									noteViewModel.setActionType(ActionType.READ)

									noteViewModel.getNotesList(
										noteViewModelState.offset - notesLimit, notesLimit,
										showLoading = false,
										filter = noteFilterViewModel.uiState.value.noteFilter)
								})
							}
						}
					}
				}

				items(noteViewModelState.list) {value ->
					NoteItemComponent(
						value,
						viewAction = {
							showNoteDetailsSheet = true

							noteDetailsLoading = true

							noteViewModel.getNoteById(value.id)

							noteDetailsLoading = false
						},

						editAction = {

							val result = noteViewModel.getNoteById(value.id)

							if (result != null){

								val noteTypes = noteTypeViewModel.getNoteTypesList(0, 10)

								if (!noteTypes.isEmpty()){
									tagViewModel.getTagsList(0, tagsLimit)
									archiveViewModel.getArchivesList(0, 100)

									noteViewModel.setActionType(ActionType.UPDATE)

									sheetTitle.value = "Изменить запись"
									showNoteForm = true
								}
							}
						},

						deleteAction = {
							val isDeleted = noteViewModel.deleteNote(value.id)

							if (isDeleted){
								val offset = if (noteViewModelState.list.size == 1) noteViewModelState.offset - notesLimit else noteViewModelState.offset

								noteViewModel.getNotesList(offset,
									notesLimit,
									showLoading = false,
									filter = noteFilterViewModel.uiState.value.noteFilter)
							}
						})
				}

				noteViewModelState.pagination?.let {
					if (it.hasNextPage){
						item {
							Row(Modifier.padding(top = 25.dp, bottom = 30.dp)) {
								LoadButtonComponent(action = {
									noteViewModel.setActionType(ActionType.READ)

									noteViewModel.getNotesList(noteViewModelState.offset + notesLimit,
										notesLimit,
										showLoading = false,
										filter = noteFilterViewModel.uiState.value.noteFilter)
								})
							}
						}
					}
				}
			}
		}

		if (!noteViewModelState.loading && noteViewModelState.list.isEmpty()){
			Box(modifier = Modifier.background(AppTheme.colors.appBgColor).fillMaxSize(),
				contentAlignment = Alignment.Center){
				MessageComponent("Записей нет")
			}
		}


		Column(
			modifier = Modifier.align(Alignment.BottomEnd).padding(20.dp),
			horizontalAlignment = Alignment.End,
			verticalArrangement = Arrangement.spacedBy(10.dp)
		) {
			BadgedBox(
				modifier = Modifier.align(Alignment.CenterHorizontally),
				badge = {
					if (noteFilterViewModelState.filterApplied){
						Badge(
							Modifier.size(12.dp).offset(x = (-1).dp, y = 1.dp),
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
							showNoteFilterForm = true

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
								showNoteFilterForm = false
							}

							noteFilterFormLoading = false
						}
					}) {

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
					scope.launch {

						showNoteForm = true

						noteFormLoading = true

						val noteTypes = noteTypeViewModel.getNoteTypesList(0, 10)

						if (!noteTypes.isEmpty()){
							tagViewModel.getTagsList(0, tagsLimit)
							archiveViewModel.getArchivesList(0, 100)

							noteViewModel.setActionType(ActionType.CREATE)

							noteViewModel.clearNote()

							sheetTitle.value = "Добавить запись"
						}

						else{
							showNoteForm = false
						}

						noteFormLoading = false
					}
				}) {
				Icon(modifier = Modifier.size(25.dp),
					painter = painterResource(R.drawable.ic_plus),
					contentDescription = "Добавить")
			}
		}


		SnackbarHost(
			modifier = Modifier.padding(16.dp).align(Alignment.BottomCenter),
			hostState = snackBarHostState
		){data ->
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
		showed = noteViewModelState.showAlert || noteTypeViewModelState.showAlert,
		action = {
			noteTypeViewModel.dismissAlert()
			noteViewModel.dismissAlert()
		}
	)


	// Note form
	if (showNoteForm){
		Column(modifier = Modifier.fillMaxSize()
			.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding)
			.background(AppTheme.colors.appBgColor)){

			BackHandler {
				showNoteForm = false
			}

			if (noteFormLoading){
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					ProgressIndicatorComponent(50, AppTheme.colors.primaryColor)
				}
			}

			else{
				Row(Modifier.padding(top = 15.dp, bottom = 20.dp).fillMaxWidth()) {
					Text(
						text = sheetTitle.value,
						color = AppTheme.colors.titleColor,
						style = AppTheme.typography.modalTitleText
					)
				}

				Row(Modifier.fillMaxWidth()) {
					HorizontalDivider(thickness = 1.dp, color = AppTheme.colors.borderColor)
				}

				NoteFormComponent(
					noteViewModelState.data,
					noteTypes = noteTypeViewModelState.list,
					archives = archiveViewModelState.list,
					tags = tagViewModelState.list,
					hasNextTags = tagViewModelState.pagination?.hasNextPage ?: false,

					loadTags = {
						val tags = tagViewModel.getTagsList(tagViewModelState.offset + tagsLimit, tagsLimit)
						tags.toMutableList()
					},

					action = { note ->

						if (noteViewModelState.actionType == ActionType.CREATE){

							val createdNote = noteViewModel.createNote(note)

							if (createdNote != null){

								if (noteFilterViewModelState.filterApplied){
									noteFilterViewModel.resetFilter()
								}

								noteViewModel.getNotesList(0, notesLimit)
							}
						}

						if (noteViewModelState.actionType == ActionType.UPDATE){

							val updatedNote = noteViewModel.updateNote(note)

							if (updatedNote != null){
								noteViewModel.getNotesList(noteViewModelState.offset, notesLimit, noteFilterViewModel.uiState.value.noteFilter)
							}
						}

						showNoteForm = false
					},

					close = { showNoteForm = false }
				)
			}
		}
	}


	// Note filter form
	if (showNoteFilterForm){
		Column(modifier = Modifier.fillMaxSize()
			.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding)
			.background(AppTheme.colors.appBgColor)){
			BackHandler { showNoteFilterForm = false }

			if (noteFilterFormLoading){
				Box(
					modifier = Modifier.fillMaxSize(),
					contentAlignment = Alignment.Center
				) {
					ProgressIndicatorComponent(50, AppTheme.colors.primaryColor)
				}
			}

			else{
				Row(Modifier.padding(top = 15.dp, bottom = 20.dp).fillMaxWidth()) {
					Text(
						text = "Фильтр",
						color = AppTheme.colors.titleColor,
						style = AppTheme.typography.modalTitleText
					)
				}

				Row(Modifier.fillMaxWidth()) {
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

						showNoteFilterForm = false

						scope.launch {
							noteViewModel.getNotesList(0, notesLimit, noteFilterViewModel.uiState.value.noteFilter)
						}
					},

					resetFilter = {
						showNoteFilterForm = false

						scope.launch {
							noteViewModel.getNotesList(0, notesLimit, noteFilterViewModel.uiState.value.noteFilter)
						}
					}
				)
			}
		}
	}


	// Details sheet
	if (showNoteDetailsSheet){
		ModalBottomSheet(
			onDismissRequest = { showNoteDetailsSheet = false },
			containerColor = Color.White,
			sheetState = noteDetailsSheetState,
		) {
			if (noteDetailsLoading){
				Box(
					modifier = Modifier.fillMaxWidth().height(200.dp),
					contentAlignment = Alignment.Center
				) {
					ProgressIndicatorComponent(50, AppTheme.colors.primaryColor)
				}
			}

			if(!noteDetailsLoading && noteViewModelState.data != null){
				NoteDetailsComponent(noteViewModelState.data)
			}
		}
	}
}