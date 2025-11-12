package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp


@Composable
fun ProgressIndicatorComponent(size: Int, color: Color) {
	CircularProgressIndicator(
		modifier = Modifier.size(size.dp),
		strokeWidth = 2.dp,
		color = color,
		trackColor = MaterialTheme.colorScheme.surfaceVariant,
	)
}