package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@Composable
fun MessageComponent(text: String)
{
	Text(
		text = text,
		color = AppTheme.colors.primaryColor,
		style = AppTheme.typography.messageText
	)
}