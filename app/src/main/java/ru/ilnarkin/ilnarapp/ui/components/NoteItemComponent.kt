package ru.ilnarkin.ilnarapp.ui.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.DEFAULT_NOTE_TITLE
import ru.ilnarkin.ilnarapp.models.Note
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun NoteItemComponent(
	note: Note,
	viewAction: suspend () -> Unit,
	editAction: suspend () -> Unit,
	deleteAction: suspend () -> Unit)
{

	val scope = rememberCoroutineScope()
	var saving by remember { mutableStateOf(false) }
	var deleting by remember { mutableStateOf(false) }
	var confirmAlertVisible by remember { mutableStateOf(false) }
	val titleColor = if (note.title != null) AppTheme.colors.titleColor else AppTheme.colors.titleColor.copy(alpha = 0.4f)


	Box(
		modifier = Modifier.fillMaxSize()
			.defaultMinSize(minHeight = 150.dp)
			.padding(bottom = 15.dp)
			.clip(RoundedCornerShape(10.dp))
			.background(color = Color.White)
			.clickable(
				interactionSource = remember { MutableInteractionSource() },
				indication = ripple(),
				onClick = {
					scope.launch { viewAction() }
				}
			))
	{

		Column (
			modifier = Modifier.padding(
				start = 10.dp,
				top = 15.dp,
				end = 15.dp,
				bottom = 20.dp))
		{
			Row {
				Text(
					text = note.title ?: DEFAULT_NOTE_TITLE,
					style = AppTheme.typography.noteCardTitle,
					color = titleColor)
			}

			Row(modifier = Modifier.padding(top = 5.dp, bottom = 20.dp))
			{
				Text(
					text = DateTimeFormatter
						.ofPattern("d MMMM yyyy, EEEE")
						.format(LocalDate.parse(note.date)),
					color = AppTheme.colors.colorGrey,
					style = AppTheme.typography.noteCardDate)
			}

			Row {
				HorizontalDivider(
					thickness = 1.dp,
					color = AppTheme.colors.borderColor)
			}

			Row(modifier = Modifier.padding(vertical = 15.dp))
			{
				Text(
					text = note.text,
					maxLines = 3,
					overflow = TextOverflow.Ellipsis,
					style = AppTheme.typography.noteCardText,
					color = AppTheme.colors.textColor)
			}

			Row {
				HorizontalDivider(
					thickness = 1.dp,
					color = AppTheme.colors.borderColor)
			}

			Row(
				modifier = Modifier
					.padding(top = 20.dp)
					.fillMaxWidth(),
				verticalAlignment = Alignment.CenterVertically)
			{
				Row(verticalAlignment = Alignment.CenterVertically)
				{
					Row(
						modifier = Modifier
							.size(35.dp)
							.alpha(0.6f),
						horizontalArrangement = Arrangement.Center,
						verticalAlignment = Alignment.CenterVertically)
					{

						if (saving){
							Row(
								modifier = Modifier.fillMaxSize(),
								horizontalArrangement = Arrangement.Center,
								verticalAlignment = Alignment.CenterVertically)
							{
								ProgressIndicatorComponent(25, AppTheme.colors.colorGrey)
							}
						}
						IconButton(onClick = {
							scope.launch {
								saving = true
								try {
									editAction()
								} finally {
									saving = false
								}
							}
						})
						{
							Icon(modifier = Modifier.size(25.dp),
								painter = painterResource(R.drawable.ic_edit),
								contentDescription = "",
								tint = AppTheme.colors.colorGrey)
						}
					}
					Row(
						modifier = Modifier
							.size(35.dp)
							.alpha(0.6f))
					{
						if (deleting){
							Row(
								modifier = Modifier.fillMaxSize(),
								horizontalArrangement = Arrangement.Center,
								verticalAlignment = Alignment.CenterVertically)
							{
								ProgressIndicatorComponent(25, AppTheme.colors.dangerColor)
							}
						}

						else{
							IconButton(onClick = {
								confirmAlertVisible = true
							})
							{
								Icon(
									modifier = Modifier.size(25.dp),
									painter = painterResource(R.drawable.ic_trash),
									contentDescription = "",
									tint = AppTheme.colors.dangerColor)
							}
						}
					}
				}
			}
		}
	}

	ConfirmComponent(
		visible = confirmAlertVisible,
		action = {confirmed ->

			if (confirmed){
				deleting = true

				scope.launch {
					deleting = true
					try {
						deleteAction()
					} finally {
						deleting = false
					}
				}
			}

			confirmAlertVisible = false
		}
	)
}