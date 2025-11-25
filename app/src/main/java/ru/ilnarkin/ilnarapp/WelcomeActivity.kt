package ru.ilnarkin.ilnarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.screens.LoginScreen
import ru.ilnarkin.ilnarapp.ui.screens.PinLockScreen
import ru.ilnarkin.ilnarapp.ui.screens.WelcomeScreen


class WelcomeActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		setContent {
			Welcome()
		}
	}
}



@Composable
fun Welcome(){
	val navController = rememberNavController()

	Box(Modifier.fillMaxSize().background(colorResource(R.color.app_bg_color))){
		NavHost(
			navController = navController,
			startDestination = NavRoutes.WelcomeScreen.route
		){
			composable(NavRoutes.WelcomeScreen.route) { WelcomeScreen(navController) }
			composable(NavRoutes.LoginScreen.route) { LoginScreen() }
			composable(NavRoutes.PinLockScreen.route) { PinLockScreen() }
		}
	}

}