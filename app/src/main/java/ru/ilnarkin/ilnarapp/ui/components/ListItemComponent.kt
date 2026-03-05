package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
fun ListItemComponent(
    text: String,
    editAction: suspend () -> Unit,
    deleteAction: suspend () -> Unit) {

    var showConfirmAlert by remember { mutableStateOf(false) }

    var isLoading by remember { mutableStateOf(false) }

    var isDeleting by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {

        Row {
            Text(
                text = text,
                fontFamily = getInterFont(),
                fontWeight = FontWeight.SemiBold,
                color = colorResource(R.color.title_color),
                fontSize = dimensionResource(R.dimen.tag_item_title_font_size).value.sp
            )
        }

        Row(verticalAlignment = Alignment.CenterVertically) {

            Row(Modifier.size(35.dp).alpha(0.6f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                if (isLoading){
                    ProgressIndicatorComponent(25, colorResource(R.color.grey))
                }

                else{
                    IconButton(onClick = {

                        scope.launch {
                            isLoading = true
                            try {
                                editAction()
                            } finally {
                                isLoading = false
                            }
                        }

                    }) {
                        Icon(modifier = Modifier.size(25.dp).alpha(0.6f),
                            painter = painterResource(R.drawable.ic_edit), contentDescription = "",
                            tint = colorResource(R.color.grey))
                    }
                }
            }


            Row(Modifier.size(35.dp).alpha(0.6f),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {
                if (isDeleting){
                    ProgressIndicatorComponent(25, colorResource(R.color.danger_color))
                }

                else{
                    IconButton(onClick = {
                        showConfirmAlert = true
                    }) {
                        Icon(
                            modifier = Modifier.size(25.dp),
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

                scope.launch {
                    isDeleting = true
                    try {
                        deleteAction()
                    } finally {
                        isDeleting = false
                    }
                }
            }

            showConfirmAlert = false
        }
    )
}