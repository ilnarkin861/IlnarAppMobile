package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@Composable
fun LoadButtonComponent(nextButton: Boolean = true, action: suspend () -> Unit) {

	val interactionSource = remember { MutableInteractionSource() }

	val scope = rememberCoroutineScope()

	val text = if (nextButton) "Следующие" else "Предыдущие"

	var loading by remember { mutableStateOf(false) }


	Row(
		modifier = Modifier.fillMaxWidth().height(25.dp),
		horizontalArrangement = Arrangement.Center
	){
		if (loading){
			ProgressIndicatorComponent(25, Color.Gray)
		}

		else{
			Text(
				modifier = Modifier.clickable(
					interactionSource = interactionSource,
					indication = null,
					onClick = {
						scope.launch {
							loading = true
							try {
								action()
							} finally {
								loading = false
							}
						}
					}
				),
				text = text,
				color = AppTheme.colors.colorGrey,
				style = AppTheme.typography.textButton.copy(fontWeight = FontWeight.Bold),
			)
		}
	}
}