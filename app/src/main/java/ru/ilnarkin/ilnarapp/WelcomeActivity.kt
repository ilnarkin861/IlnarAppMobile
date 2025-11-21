package ru.ilnarkin.ilnarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ru.ilnarkin.ilnarapp.routes.NavRoutes
import ru.ilnarkin.ilnarapp.ui.screens.NotesScreen
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

	Box(Modifier.fillMaxSize()){
		NavHost(
			navController = navController,
			startDestination = NavRoutes.WelcomeScreen.route
		){
			composable(NavRoutes.WelcomeScreen.route) { WelcomeScreen() }
		}
	}

}