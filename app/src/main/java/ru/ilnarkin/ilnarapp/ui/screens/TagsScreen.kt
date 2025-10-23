package ru.ilnarkin.ilnarapp.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.absolutePadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Devices.PIXEL_3
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.ui.components.AlertComponent
import ru.ilnarkin.ilnarapp.ui.components.ListItemComponent
import ru.ilnarkin.ilnarapp.ui.components.LoadButtonComponent
import ru.ilnarkin.ilnarapp.ui.components.ProgressIndicatorComponent


@Composable
@Preview(showBackground = true, showSystemUi = true, device = PIXEL_3)
fun TagsScreen() {

	val tags = getTags(20)
	var loading by remember { mutableStateOf(false) }
	val listState = rememberLazyListState()
	val alertTitle = remember { mutableStateOf("") }
	var showAlert by remember { mutableStateOf(false) }
	var success by remember { mutableStateOf(true) }


	LaunchedEffect(Unit) {
		loading = true

		delay(1500)

		loading = false
	}


	Box(Modifier.fillMaxSize()) {

		if (loading){
			Box(
				modifier = Modifier.fillMaxSize(),
				contentAlignment = Alignment.Center){
				ProgressIndicatorComponent(60)
			}
		}

		if (!loading && !tags.isEmpty()){
			LazyColumn(
				state = listState,
				contentPadding = PaddingValues(top = 30.dp, bottom = 80.dp)
			) {
				item {
					Row(Modifier.padding(bottom = 25.dp)) {
						LoadButtonComponent(nextButton = false, action = { delay(1500) })
					}
				}

				itemsIndexed(tags){index, tag ->

					Row(Modifier.fillMaxWidth().padding(vertical = 5.dp)) {
						ListItemComponent(
							tag.id,
							tag.title,
							editAction = {},
							deleteAction = {
								delay(1500)
								alertTitle.value = "Тег успешно удален"
								showAlert = true
							})
					}

					if (index != tags.count() -1){
						HorizontalDivider(thickness = 1.dp, color = colorResource(R.color.border_color))
					}
				}

				item {
					Row(Modifier.padding(top = 25.dp, bottom = 30.dp)) {
						LoadButtonComponent(action = {
							delay(1500)
							listState.scrollToItem(0)
						})
					}
				}
			}
		}

		AlertComponent(
			success = success,
			message = alertTitle.value,
			showed = showAlert,
			action = {
				showAlert = false

				// еще что-то делаем
			}
		)


		FloatingActionButton(
			containerColor = colorResource(R.color.primary_color),
			contentColor = Color.White,
			shape = CircleShape,
			modifier = Modifier
				.align(Alignment.BottomEnd)
				.absolutePadding(bottom = 30.dp, right = 30.dp)
				.background(Color.Transparent),
			onClick = {}) {
			Icon(painter = painterResource(R.drawable.ic_plus), contentDescription = "Добавить")
		}
	}
}

