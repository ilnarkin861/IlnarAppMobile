package ru.ilnarkin.ilnarapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.Preview
import ru.ilnarkin.ilnarapp.appbars.BottomNavigationBar
import ru.ilnarkin.ilnarapp.appbars.TopBar


class MainActivity : ComponentActivity() {
	override fun onCreate(savedInstanceState: Bundle?) {
		super.onCreate(savedInstanceState)
		enableEdgeToEdge()
		setContent {
			Main()
		}
	}

}


@Preview(showBackground = true, showSystemUi = true)
@Composable
fun Main(){
	Column(Modifier.displayCutoutPadding()) {
		TopBar()
		Column(modifier = Modifier.fillMaxSize().weight(1f).background(colorResource(R.color.app_bg_color))) {  }
		BottomNavigationBar()
	}
}