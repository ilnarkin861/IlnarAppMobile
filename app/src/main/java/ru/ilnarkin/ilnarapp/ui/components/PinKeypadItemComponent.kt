package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@Composable
fun PinKeypadItemComponent(
	onClick: () -> Unit,
	bordered: Boolean = true,
	content: @Composable () -> Unit
) {
	Surface(
		modifier = Modifier.padding(10.dp)
			.clip(shape = CircleShape)
			.border(width = if (bordered) 1.dp else 0.dp,
				color = if (bordered) AppTheme.colors.colorGrey else Color.Transparent,
				shape = CircleShape
			).size(60.dp),
		onClick = onClick,
	) {
		Box(modifier = Modifier.fillMaxSize().background(AppTheme.colors.appBgColor),
			contentAlignment = Alignment.Center) {  content() }
	}
}