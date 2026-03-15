package ru.ilnarkin.ilnarapp.ui.screens

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties
import androidx.navigation.NavController
import org.koin.androidx.compose.koinViewModel
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.components.PinKeypadItemComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme
import ru.ilnarkin.ilnarapp.viewModels.UserViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PinResetScreen(
	navController: NavController,
	userViewModel: UserViewModel = koinViewModel())
{

	val scrollState = rememberScrollState()
	val orientation = LocalConfiguration.current.orientation
	val isLandscape = orientation == Configuration.ORIENTATION_LANDSCAPE
	val modifier = if(isLandscape) Modifier.wrapContentHeight() else Modifier
	var pinCodeError by rememberSaveable { mutableStateOf(false) }
	var newPinEntered by rememberSaveable { mutableStateOf(false) }
	var newPin: String? by rememberSaveable { mutableStateOf(null) }
	val newPinTitle = "Введи новый PIN-код"
	val title = rememberSaveable { mutableStateOf(newPinTitle) }
	var confirmPinEntered by rememberSaveable { mutableStateOf(false) }
	val confirmPinTitle = "Подтверди PIN-код"
	var confirmPin: String? by rememberSaveable { mutableStateOf(null) }
	val inputPin = rememberSaveable { mutableStateListOf<Int>() }
	var loading by rememberSaveable { mutableStateOf(false) }


	if (inputPin.size == 4){
		LaunchedEffect(true) {

			if (newPin != null && !confirmPinEntered){
				confirmPin = inputPin.joinToString("")
				confirmPinEntered = true
			}

			if (!newPinEntered && newPin == null){
				newPin = inputPin.joinToString("")
				newPinEntered = true
				title.value = confirmPinTitle
			}

			if (newPinEntered && confirmPinEntered){
				if (newPin != confirmPin){
					newPin = null
					confirmPin = null
					newPinEntered = false
					confirmPinEntered = false
					pinCodeError = true
					title.value = newPinTitle
				}

				else{
					loading = true

					userViewModel.setPinCode(inputPin.joinToString(""))

					loading = false

					navController.navigate(NavRoutes.OverlayScreen.route) {
						popUpTo(NavRoutes.PinResetScreen.route) { inclusive = true }
					}
				}
			}

			inputPin.clear()
		}
	}


	Column(
		modifier = modifier
			.fillMaxSize()
			.padding(horizontal = AppTheme.dimensions.containerHorizontalPadding)
			.verticalScroll(scrollState),
		verticalArrangement = Arrangement.SpaceBetween)
	{

		Column(modifier = Modifier.padding(top = 100.dp))
		{
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center)
			{
				Text(title.value,
					color = AppTheme.colors.titleColor,
					style = AppTheme.typography.pinResetTitle)
			}

			Row(
				modifier = Modifier
					.fillMaxWidth()
					.padding(top = 20.dp),
				horizontalArrangement = Arrangement.Center)
			{
				Row(
					modifier = Modifier.padding(bottom = 40.dp))
				{
					(0 until 4).forEach {
						Box(
							modifier = Modifier
								.padding(10.dp)
								.alpha(if (inputPin.size > it) 1f else 0.5f)
								.background(
									color =  Color.DarkGray,
									shape = CircleShape)
								.size(15.dp))
					}
				}
			}

			if (pinCodeError){
				Row(
					modifier = Modifier.fillMaxWidth(),
					horizontalArrangement = Arrangement.Center)
				{
					Text(
						text = "PIN-коды не совпадают",
						color = AppTheme.colors.dangerColor,
						style = AppTheme.typography.authMessageText)
				}
			}
		}

		Column(
			modifier = Modifier
				.fillMaxWidth()
				.padding(
					top = if(isLandscape) 100.dp else 0.dp,
					bottom = 50.dp))
		{
			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center)
			{
				Row {
					(1..3).forEach {
						PinKeypadItemComponent(
							onClick = {
								if(inputPin.size < 4) {
									pinCodeError = false
									inputPin.add(it)
								}
							})
						{
							Text(
								text = it.toString(),
								color = AppTheme.colors.colorGrey,
								style = AppTheme.typography.keyPadItemText
							)
						}
					}
				}
			}

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center)
			{
				Row {
					(4..6).forEach {
						PinKeypadItemComponent(
							onClick = {
								if(inputPin.size < 4) {
									pinCodeError = false
									inputPin.add(it)
								}
							})
						{
							Text(
								text = it.toString(),
								color = AppTheme.colors.colorGrey,
								style = AppTheme.typography.keyPadItemText
							)
						}
					}
				}
			}

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center)
			{
				Row {
					(7..9).forEach {
						PinKeypadItemComponent(
							onClick = {
								if(inputPin.size < 4) {
									pinCodeError = false
									inputPin.add(it)
								}
							})
						{
							Text(
								text = it.toString(),
								color = AppTheme.colors.colorGrey,
								style = AppTheme.typography.keyPadItemText)
						}
					}
				}
			}

			Row(
				modifier = Modifier.fillMaxWidth(),
				horizontalArrangement = Arrangement.Center)
			{
				PinKeypadItemComponent(
					onClick = {
						if(inputPin.size < 4) {
							pinCodeError = false
							inputPin.add(0)
						}
					})
				{
					Text(
						text = "0",
						color = AppTheme.colors.colorGrey,
						style = AppTheme.typography.keyPadItemText)
				}

			}
		}
	}

	if (loading){
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
				Column(
					modifier = Modifier
						.background(Color.White)
						.padding(horizontal = 16.dp, vertical = 20.dp))
				{
					Row(
						modifier = Modifier.fillMaxWidth(),
						verticalAlignment = Alignment.CenterVertically)
					{
						ProgressIndicatorComponent(size = 40, color = AppTheme.colors.primaryColor)

						Text(
							modifier = Modifier.padding(start = 15.dp),
							text = "Подожди...",
							color = AppTheme.colors.textColor,
							style = AppTheme.typography.pinModalText)
					}
				}
			}
		}
	}
}