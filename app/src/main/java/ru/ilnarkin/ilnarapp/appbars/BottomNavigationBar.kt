package ru.ilnarkin.ilnarapp.appbars

import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
fun BottomNavigationBar() {
	val borderColor = colorResource(R.color.border_color)

	val colors = NavigationBarItemDefaults.colors(
		selectedIconColor = colorResource(R.color.primary_color),
		unselectedIconColor = colorResource(R.color.bottom_navigation_color),
		selectedTextColor = colorResource(R.color.primary_color),
		unselectedTextColor = colorResource(R.color.bottom_navigation_color),
		indicatorColor = Color.Transparent)

	NavigationBar(
		modifier = Modifier.drawBehind {
			var borderStrokeWidth = 2.dp
			val strokeWidthPx = borderStrokeWidth.toPx()
			drawLine(
				color = borderColor,
				start = Offset(x = 0f, y = 0f),
				end = Offset(x = size.width, y = 0f),
				strokeWidth = strokeWidthPx
			)
		},
		containerColor = Color.White,
	) {

		NavigationBarItem(
			selected = true,
			colors = colors,
			icon = { Icon(painter = painterResource(R.drawable.ic_notes), contentDescription = "") },
			label = { Text(
				text = stringResource(R.string.notes_title),
				fontFamily = getInterFont(),
				fontSize = dimensionResource(R.dimen.bottom_bar_label_font_size).value.sp,
				fontWeight = FontWeight.Bold)
			},
			onClick = {})

		NavigationBarItem(
			selected = false,
			colors = colors,
			icon = { Icon(painter = painterResource(R.drawable.ic_hashtag), contentDescription = "") },
			label = { Text(
				text = stringResource(R.string.tags_title),
				fontFamily = getInterFont(),
				fontSize = dimensionResource(R.dimen.bottom_bar_label_font_size).value.sp,
				fontWeight = FontWeight.Bold)
			},
			onClick = {})

		NavigationBarItem(
			selected = false,
			colors = colors,
			icon = { Icon(painter = painterResource(R.drawable.ic_archive), contentDescription = "") },
			label = { Text(
				text = stringResource(R.string.archives_title),
				fontFamily = getInterFont(),
				fontSize = dimensionResource(R.dimen.bottom_bar_label_font_size).value.sp,
				fontWeight = FontWeight.Bold)
			},
			onClick = {})

		NavigationBarItem(
			selected = false,
			colors = colors,
			icon = { Icon(painter = painterResource(R.drawable.ic_search), contentDescription = "") },
			label = { Text(
				text = stringResource(R.string.search_title),
				fontFamily = getInterFont(),
				fontSize = dimensionResource(R.dimen.bottom_bar_label_font_size).value.sp,
				fontWeight = FontWeight.Bold)
			},
			onClick = {})
	}
}