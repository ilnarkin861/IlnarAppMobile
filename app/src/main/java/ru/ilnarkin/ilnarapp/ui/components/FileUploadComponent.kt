package ru.ilnarkin.ilnarapp.ui.components

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getFileName
import ru.ilnarkin.ilnarapp.models.SelectedFileInfo
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.FileViewModel


@Composable
fun FileUploadComponent(
	fileViewModel: FileViewModel = koinViewModel(),
	upload: () -> Unit,
	close: () -> Unit)
{
	val context = LocalContext.current
	val launcher = rememberLauncherForActivityResult(
		contract = ActivityResultContracts.OpenMultipleDocuments(),
		onResult = { uris ->
			uris.forEach { uri ->
				val name = getFileName(context, uri)
				val type = context.contentResolver.getType(uri)
				fileViewModel.addLocalSelectedFile(SelectedFileInfo(uri = uri, name = name, mimeType = type))
			}
		}
	)

	val selectedFilesState by fileViewModel.localSelectedFiles.collectAsState()

	var uploading by rememberSaveable { mutableStateOf(false) }

	val stroke = Stroke(
		width = 2f,
		pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
	)

	val primaryColor = AppTheme.colors.primaryColor



	Column(
		modifier = Modifier
			.background(Color.White)
			.verticalScroll(rememberScrollState()))
	{

		BackHandler {
			if (!uploading){
				fileViewModel.clearLocalSelectedFiles()
				close()
			}
		}

		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(top = 30.dp,
					start = AppTheme.dimensions.containerHorizontalPadding,
					end = AppTheme.dimensions.containerHorizontalPadding,
					bottom = 40.dp))
		{
			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(bottom = 10.dp))
			{
				Text(
					modifier = Modifier.fillMaxWidth(),
					text = "Загрузка файлов",
					color = AppTheme.colors.colorGrey,
					style = AppTheme.typography.formTitleText.copy(textAlign = TextAlign.Center))
			}


			Column(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 10.dp))
			{
				Column(
					modifier = Modifier
						.fillMaxWidth()
						.padding(bottom = 20.dp)
						.background(AppTheme.colors.appBgColor)
						.drawBehind {
							drawRoundRect(
								color = primaryColor,
								cornerRadius = CornerRadius(10.dp.toPx()),
								style = stroke)
						}
						.clickable(
							onClick = {
								launcher.launch(arrayOf("image/*"))
							}
						))
				{
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 20.dp, start = 5.dp, end = 5.dp, bottom = 10.dp),
						horizontalArrangement = Arrangement.Center)
					{
						Image(
							modifier = Modifier.size(60.dp),
							painter = painterResource(R.drawable.ic_upload),
							contentDescription = "Upload",
							alpha = 0.4f)
					}

					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(bottom = 40.dp))
					{
						Text(
							modifier = Modifier
								.fillMaxWidth(),
							text = if (selectedFilesState.isEmpty()) "Нажми для выбора" else "Выбрано файлов: ${selectedFilesState.size}",
							color = AppTheme.colors.primaryColor.copy(alpha = 0.4f),
							textAlign = TextAlign.Center,
							fontSize = 15.sp,
							fontWeight = FontWeight.SemiBold
						)
					}
				}

				if (selectedFilesState.isNotEmpty()){
					Column(
						modifier = Modifier
							.fillMaxWidth()
							.heightIn(max = 250.dp)
							.verticalScroll(rememberScrollState()))
					{
						selectedFilesState.forEach { file ->
							SelectedFileComponent(
								file = file,
								delete = {file ->
									fileViewModel.removeLocalSelectedFile(file)
								}
							)
						}
					}
				}


				if (selectedFilesState.isNotEmpty()){
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 40.dp))
					{
						Button(
							modifier = Modifier
								.fillMaxWidth()
								.height(60.dp),
							enabled = !uploading,
							shape = RoundedCornerShape(10.dp),
							colors = ButtonDefaults.buttonColors(
								containerColor = AppTheme.colors.primaryColor,
								disabledContainerColor = AppTheme.colors.primaryColor.copy(alpha = 0.8f)),
							onClick = {
							})
						{
							if (uploading){
								CircularProgressIndicator(
									modifier = Modifier.size(20.dp),
									strokeWidth = 2.dp,
									color = Color.White,
									trackColor = Color.Transparent)
							}
							else{
								Text(
									text = "Загрузить",
									style = AppTheme.typography.inputButtonText
								)
							}
						}
					}
				}

				if (!uploading){
					Row(
						modifier = Modifier
							.fillMaxWidth()
							.padding(top = 15.dp),
						horizontalArrangement = Arrangement.Center)
					{
						Text(
							modifier = Modifier.clickable(
								interactionSource = remember { MutableInteractionSource() },
								indication = null,
								onClick = {
									fileViewModel.clearLocalSelectedFiles()
									close()
								}),
							text = "Закрыть",
							color = AppTheme.colors.colorGrey,
							style = AppTheme.typography.textButton)
					}
				}
			}
		}
	}
}