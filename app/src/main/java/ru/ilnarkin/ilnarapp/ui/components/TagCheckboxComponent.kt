package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import ru.ilnarkin.ilnarapp.models.Tag
import ru.ilnarkin.ilnarapp.ui.theme.AppTheme


@Composable
fun TagCheckboxComponent(
	tag: Tag,
	isChecked: Boolean = false,
	onChecked: (tag: Tag) -> Unit
) {
	var checked by remember { mutableStateOf(isChecked) }

	Row(Modifier.fillMaxWidth().clickable(
		interactionSource = remember { MutableInteractionSource() },
		indication = ripple(),
		onClick = {
			checked = !checked
			onChecked(tag)
		}
	)) {

		Row(
			modifier = Modifier.fillMaxWidth().padding(horizontal = AppTheme.dimensions.containerHorizontalPadding, vertical = 15.dp),
			verticalAlignment = Alignment.CenterVertically
		) {

			Row(
				modifier = Modifier.size(20.dp)
					.border(
						width = 1.dp,
						color = AppTheme.colors.inputsBorderColor,
						shape = RoundedCornerShape(2.dp)),
				Arrangement.Center,
				Alignment.CenterVertically
			) {
				if (checked){
					Box(
						Modifier.size(12.dp)
							.clip(RoundedCornerShape(1.dp))
							.background(AppTheme.colors.inputsBorderColor)
					)
				}
			}

			Text(
				modifier = Modifier.padding(start = 10.dp),
				text = tag.title,
				color = AppTheme.colors.textColor,
				style = AppTheme.typography.tagCheckboxText
			)
		}
	}
}