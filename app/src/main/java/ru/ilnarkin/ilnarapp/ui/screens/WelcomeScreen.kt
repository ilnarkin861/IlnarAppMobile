package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import kotlinx.coroutines.delay
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.routes.NavRoutes


@Composable
fun WelcomeScreen(navController: NavController) {

	LaunchedEffect(Unit) {
		delay(2000)
		navController.navigate(NavRoutes.LoginScreen.route){
			popUpTo(navController.graph.findStartDestination().id) {
				inclusive = true
			}
		}
	}

	Box(modifier = Modifier
		.fillMaxSize()
		.background(colorResource(R.color.primary_color)),
		contentAlignment = Alignment.Center){

		Image(
			painter = painterResource(R.drawable.logo),
			contentDescription = "Logo")
	}
}