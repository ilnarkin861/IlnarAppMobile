package ru.ilnarkin.ilnarapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.R
import ru.ilnarkin.ilnarapp.helpers.getInterFont


@Composable
fun ListItemComponent(
    id: String,
    title: String,
    editAction: () -> Unit,
    deleteAction: () -> Unit) {

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

        Row {
            IconButton(onClick = { }) {
                Icon(modifier = Modifier.size(22.dp),
                    painter = painterResource(R.drawable.ic_edit), contentDescription = "",
                    tint = colorResource(R.color.primary_color))
            }

            IconButton(onClick = { }) {
                Icon(
                    modifier = Modifier.size(22.dp),
                    painter = painterResource(R.drawable.ic_trash), contentDescription = "",
                    tint = colorResource(R.color.danger_color))
            }
        }
    }
}