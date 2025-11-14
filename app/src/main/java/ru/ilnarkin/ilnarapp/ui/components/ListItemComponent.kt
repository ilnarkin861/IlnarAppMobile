package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
fun ListItemComponent(
    id: String,
    title: String,
    editAction: suspend (id: String) -> Unit,
    deleteAction: suspend (id: String) -> Unit) {

    var loading by remember { mutableStateOf(false) }
    var deleting by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var showConfirmAlert by remember { mutableStateOf(false) }

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Row {
            Text(
                text = title,
                fontFamily = getInterFont(),
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.title_color),
                fontSize = dimensionResource(R.dimen.tag_item_title_font_size).value.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            Row(Modifier.size(35.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                if (loading){
                    ProgressIndicatorComponent(25, colorResource(R.color.primary_color))
                }

                else{
                    IconButton(onClick = {
                        loading = true

                        scope.launch {
                            scope.async {
                                // в идеале передаем айдишники, достаем по ним из бд и передаем в форму
                                editAction(title)
                            }.await()
                        }.invokeOnCompletion{ loading = false }

                    }) {
                        Icon(modifier = Modifier.size(22.dp),
                            painter = painterResource(R.drawable.ic_edit), contentDescription = "",
                            tint = colorResource(R.color.primary_color))
                    }
                }
            }


            Row(Modifier.size(35.dp).padding(start = 10.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                if (deleting){
                    ProgressIndicatorComponent(25, colorResource(R.color.danger_color))
                }

                else{
                    IconButton(onClick = {
                        showConfirmAlert = true
                    }) {
                        Icon(
                            modifier = Modifier.size(22.dp),
                            painter = painterResource(R.drawable.ic_trash), contentDescription = "",
                            tint = colorResource(R.color.danger_color))
                    }
                }
            }
        }
    }

    ConfirmComponent(
        showed = showConfirmAlert,
        action = {confirmed ->

            if (confirmed){
                deleting = true

                scope.launch {
                    scope.async {
                        deleteAction(id)
                    }.await()
                }.invokeOnCompletion{ deleting = false }
            }

            showConfirmAlert = false
        }
    )
}