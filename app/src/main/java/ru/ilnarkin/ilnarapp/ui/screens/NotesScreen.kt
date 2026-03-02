package ru.ilnarkin.ilnarapp.ui.screens

import android.content.Intent
import android.os.Build
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import org.koin.compose.koinInject
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.WelcomeActivity
import ru.ilnarkin.ilnarapp.enums.ActionType
import ru.ilnarkin.ilnarapp.enums.NetworkErrorType
import ru.ilnarkin.ilnarapp.helpers.NO_INTERNET_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.SERVER_ERROR_MESSAGE
import ru.ilnarkin.ilnarapp.helpers.getInterFont
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.models.NoteFilter
import ru.ilnarkin.ilnarapp.network.NetworkErrorManager
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.MessageComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteDetailsComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteFormComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteItemComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.ui.components.NoteFilterFormComponent
import ru.ilnarkin.ilnarapp.viewModels.ArchiveViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteFilterViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteTypeViewModel
import ru.ilnarkin.ilnarapp.viewModels.NoteViewModel
import ru.ilnarkin.ilnarapp.viewModels.TagViewModel


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NotesScreen(
	noteViewModel: NoteViewModel = koinViewModel(),
	noteTypeViewModel: NoteTypeViewModel = koinViewModel(),
	tagViewModel: TagViewModel = koinViewModel(),
	archiveViewModel: ArchiveViewModel = koinViewModel(),
	noteFilterViewModel: NoteFilterViewModel = koinViewModel(),
	errorManager: NetworkErrorManager = koinInject()
) {

	val notesLimit = 10
	val tagsLimit = 10

	val context = LocalContext.current

	var actionType by remember { mutableStateOf(ActionType.READ) }

	var currentNote by remember { mutableStateOf<Note?>(null) }

	var showNoteFilterFormSheet by remember { mutableStateOf(false) }
	val noteSearchFormSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

	val scope = rememberCoroutineScope()

	val listState = rememberLazyListState()

	var showNoteFormSheet by remember { mutableStateOf(false) }
	val noteFormSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

	var showNoteDetailsSheet by remember { mutableStateOf(false) }
	val noteDetailsSheetState = rememberModalBottomSheetState()
	var noteDetailsLoading by remember { mutableStateOf(false) }

	val sheetTitle = remember { mutableStateOf("") }

	val snackBarHostState = remember { SnackbarHostState() }

	val noteViewModelState by noteViewModel.uiState.collectAsState()

	val noteTypeViewModelState by noteTypeViewModel.uiState.collectAsState()

	val tagViewModelState by tagViewModel.uiState.collectAsState()

	val archiveViewModelState by archiveViewModel.uiState.collectAsState()

	val noteFilterViewModelState by noteFilterViewModel.uiState.collectAsState()

	var floatingButtonsVisible by remember { mutableStateOf(false) }

	var noteFilter by remember { mutableStateOf<NoteFilter?>(null) }


	LaunchedEffect(Unit) {
		errorManager.errorEvent.collect { error ->
			when(error) {
				NetworkErrorType.NO_INTERNET ->{
					showNoteDetailsSheet = false
					noteDetailsLoading = false
					currentNote = null
					snackBarHostState.showSnackbar(NO_INTERNET_ERROR_MESSAGE)
				}

				NetworkErrorType.SERVER_ERROR -> {
					showNoteDetailsSheet = false
					noteDetailsLoading = false
					currentNote = null
					snackBarHostState.showSnackbar(SERVER_ERROR_MESSAGE)
				}

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
		floatingButtonsVisible = false

		noteViewModel.getNotesList(0, notesLimit)

		floatingButtonsVisible = true
	}


	Box(Modifier
		.fillMaxSize()
		.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))) {

		if (noteViewModelState.loading){
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center){
				ProgressIndicatorComponent(60, colorResource(R.color.primary_color))
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
									actionType = ActionType.READ

									noteViewModel.getNotesList(noteViewModelState.offset - notesLimit, notesLimit, showLoading = false, filter = noteFilter)
								})
							}
						}
					}
				}

				items(noteViewModelState.list) {value ->
					NoteItemComponent(
						value,
						viewAction = {

							floatingButtonsVisible = false

							currentNote = null

							showNoteDetailsSheet = true

							noteDetailsLoading = true

							val result = noteViewModel.getNoteById(value.id)

							if (result != null){
								currentNote = result
							}

							noteDetailsLoading = false

							floatingButtonsVisible = true
						},

						editAction = {

							floatingButtonsVisible = false

							val result = noteViewModel.getNoteById(value.id)

							if (result != null){

								val noteTypes = noteTypeViewModel.getNoteTypesList(0, 10)

								if (!noteTypes.isEmpty()){
									tagViewModel.getTagsList(0, tagsLimit)
									archiveViewModel.getArchivesList(0, 100)

									currentNote = result
									actionType = ActionType.UPDATE
									sheetTitle.value = "Изменить запись"
									showNoteFormSheet = true
								}

								floatingButtonsVisible = true
							}
						},

						deleteAction = {

							floatingButtonsVisible = false

							val isDeleted = noteViewModel.deleteNote(value.id)

							if (isDeleted){
								val offset = if (noteViewModelState.list.size == 1) noteViewModelState.offset - notesLimit else noteViewModelState.offset

								noteViewModel.getNotesList(offset, notesLimit, showLoading = false, filter = noteFilterViewModel.uiState.value.noteFilter)
							}

							floatingButtonsVisible = true
						})
				}

				noteViewModelState.pagination?.let {
					if (it.hasNextPage){
						item {
							Row(Modifier.padding(top = 25.dp, bottom = 30.dp)) {
								LoadButtonComponent(action = {
									actionType = ActionType.READ

									noteViewModel.getNotesList(noteViewModelState.offset + notesLimit, notesLimit, showLoading = false, filter = noteFilterViewModel.uiState.value.noteFilter)
								})
							}
						}
					}
				}
			}
		}

		if (!noteViewModelState.loading && noteViewModelState.list.isEmpty()){
			Box(modifier = Modifier
				.background(colorResource(R.color.app_bg_color))
				.fillMaxSize(),
				contentAlignment = Alignment.Center){
				MessageComponent("Записей нет")
			}
		}


		Column(
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.padding(20.dp),
			horizontalAlignment = Alignment.End,
			verticalArrangement = Arrangement.spacedBy(10.dp)
		) {
			BadgedBox(
				modifier = Modifier.align(Alignment.CenterHorizontally),
				badge = {
					if (noteFilterViewModelState.filterApplied){
						Badge(
							Modifier.size(12.dp).offset(x = (-1).dp, y = 1.dp),
							containerColor = colorResource(R.color.danger_color)
						)
					}
				}
			) {
				SmallFloatingActionButton(
					shape = CircleShape,
					containerColor = Color.White,
					contentColor = colorResource(R.color.primary_color),
					onClick = {

						if(floatingButtonsVisible){
							scope.launch {

								floatingButtonsVisible = false

								if (noteFilterViewModelState.selectableNoteTypes.isEmpty()){
									val noteTypes = noteTypeViewModel.getNoteTypesList(0, 10)
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

								showNoteFilterFormSheet = true

								floatingButtonsVisible = true
							}
						}

					}) {

					Icon(painter = painterResource(R.drawable.ic_filter), contentDescription = "Filter")
				}



			}

			FloatingActionButton(
				containerColor = colorResource(R.color.primary_color),
				contentColor = Color.White,
				shape = CircleShape,
				modifier = Modifier
					.background(Color.Transparent),
				onClick = {

					if(floatingButtonsVisible){
						scope.launch {

							floatingButtonsVisible = false

							val noteTypes = noteTypeViewModel.getNoteTypesList(0, 10)

							if (!noteTypes.isEmpty()){
								tagViewModel.getTagsList(0, tagsLimit)
								archiveViewModel.getArchivesList(0, 100)

								currentNote = null
								actionType = ActionType.CREATE
								sheetTitle.value = "Добавить запись"
								showNoteFormSheet = true
							}

							floatingButtonsVisible = true
						}
					}

				}) {
				Icon(modifier = Modifier.size(25.dp),
					painter = painterResource(R.drawable.ic_plus),
					contentDescription = "Добавить")
			}
		}


		SnackbarHost(
			hostState = snackBarHostState,
			modifier = Modifier
				.padding(16.dp)
				.align(Alignment.BottomCenter)
		){data ->
			Snackbar(
				snackbarData = data,
				containerColor = colorResource(R.color.primary_color),
				contentColor = Color.White
			)
		}
	}


	AlertComponent(
		success = noteViewModelState.success,
		message = if (noteViewModelState.showAlert) noteViewModelState.message else noteTypeViewModelState.message,
		showed = noteViewModelState.showAlert || noteTypeViewModelState.showAlert,
		action = { noteViewModel.dismissAlert()	}
	)


	// Form sheet
	if (showNoteFormSheet){
		ModalBottomSheet(
			onDismissRequest = {
				currentNote = null
				showNoteFormSheet = false
			},
			containerColor = Color.White,
			sheetState = noteFormSheetState,
		) {

			Column {
				Row(Modifier.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))) {
					Text(
						color = colorResource(R.color.title_color),
						text = sheetTitle.value,
						fontFamily = getInterFont(),
						fontSize = 18.sp,
						fontWeight = FontWeight.Bold
					)
				}

				if (noteDetailsLoading){
					Box(
						modifier = Modifier
							.fillMaxWidth()
							.height(200.dp),
						contentAlignment = Alignment.Center
					) {
						ProgressIndicatorComponent(50, colorResource(R.color.primary_color))
					}
				}

				else{
					NoteFormComponent(
						currentNote,
						noteTypes = noteTypeViewModelState.list,
						archives = archiveViewModelState.list,
						tags = tagViewModelState.list,
						hasNextTags = tagViewModelState.pagination?.hasNextPage ?: false,

						loadTags = {
							val tags = tagViewModel.getTagsList(tagViewModelState.offset + tagsLimit, tagsLimit)
							tags.toMutableList()
						},

						action = {note ->

							if (actionType == ActionType.CREATE){

								val createdNote = noteViewModel.createNote(note)

								if (createdNote != null){

									noteFilterViewModel.resetFilter()

									noteViewModel.getNotesList(0, notesLimit)
								}
							}

							if (actionType == ActionType.UPDATE){

								val updatedNote = noteViewModel.updateNote(note)

								if (updatedNote != null){
									noteViewModel.getNotesList(noteViewModelState.offset, notesLimit, noteFilterViewModel.uiState.value.noteFilter)
								}
							}

							noteFormSheetState.hide()

							showNoteFormSheet = false

							currentNote = null
						}
					)
				}
			}
		}
	}


	// Filter form sheet
	if (showNoteFilterFormSheet){

		ModalBottomSheet(
			onDismissRequest = { showNoteFilterFormSheet = false },
			containerColor = Color.White,
			sheetState = noteSearchFormSheetState,
		){
			Column {
				Row(Modifier.padding(horizontal = dimensionResource(R.dimen.container_horizontal_padding))) {
					Text(
						color = colorResource(R.color.title_color),
						text = "Фильтр",
						fontFamily = getInterFont(),
						fontSize = 18.sp,
						fontWeight = FontWeight.Bold
					)
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

						showNoteFilterFormSheet = false

						scope.launch {
							noteViewModel.getNotesList(0, notesLimit, noteFilterViewModel.uiState.value.noteFilter)
						}
					},

					resetFilter = {
						showNoteFilterFormSheet = false

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
			onDismissRequest = {
				showNoteDetailsSheet = false
				currentNote = null
				floatingButtonsVisible = true
			},
			containerColor = Color.White,
			sheetState = noteDetailsSheetState,
		) {
			if (noteDetailsLoading){
				Box(
					modifier = Modifier
						.fillMaxWidth()
						.height(200.dp),
					contentAlignment = Alignment.Center
				) {
					ProgressIndicatorComponent(50, colorResource(R.color.primary_color))
				}
			}

			if(!noteDetailsLoading && currentNote != null){
				NoteDetailsComponent(currentNote)
			}
		}
	}
}