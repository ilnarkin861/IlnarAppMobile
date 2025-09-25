package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import ru.ilnarkin.ilnarapp.R


@Composable
fun ProgressIndicatorComponent(size: Int) {
	CircularProgressIndicator(
		modifier = Modifier.size(size.dp),
		strokeWidth = 2.dp,
		color = colorResource(R.color.primary_color),
		trackColor = MaterialTheme.colorScheme.surfaceVariant,
	)
}