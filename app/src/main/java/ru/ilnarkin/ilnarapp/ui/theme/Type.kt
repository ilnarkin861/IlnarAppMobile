package ru.ilnarkin.ilnarapp.ui.theme

import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import ru.ilnarkin.ilnarapp.helpers.getInterFont


val fontFamily = getInterFont()


data class AppTypography(

    val baseStyle: TextStyle = TextStyle(
        fontFamily = fontFamily
    ),

    val appBarTitle: TextStyle = baseStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.ExtraBold
    ),

    val navBarItemTitle: TextStyle = baseStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold
    ),

    val noteItemTitle: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),

    val noteItemDate: TextStyle = baseStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    ),

    val noteItemText: TextStyle = baseStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.5.em,
    ),

    val noteTitle: TextStyle = baseStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.Bold
    ),

    val noteDate: TextStyle = baseStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.Normal
    ),

    val noteText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        lineHeight = 1.5.em,
    ),

    val noteDetailsText: TextStyle = baseStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    ),

    val noteTags: TextStyle = baseStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    ),

    val listItemText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),

    val modalText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center
    ),

    val modalTitleText: TextStyle = baseStyle.copy(
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold
    ),

    val textButton: TextStyle = baseStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.SemiBold
    ),

    val formInputText: TextStyle = baseStyle.copy(
        fontSize = 15.sp,
        fontWeight = FontWeight.Normal
    ),

    val inputButtonText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.SemiBold
    ),

    val errorText: TextStyle = baseStyle.copy(
        fontSize = 13.sp,
        fontWeight = FontWeight.Normal
    ),

    val messageText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),

    val tagCheckboxText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),

    val authMessageText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal,
        textAlign = TextAlign.Center
    ),

    val keyPadItemText: TextStyle = baseStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.SemiBold
    ),

    val pinResetTitle: TextStyle = baseStyle.copy(
        fontSize = 20.sp,
        fontWeight = FontWeight.Normal
    ),

    val forgotPinText: TextStyle = baseStyle.copy(
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        textAlign = TextAlign.Center
    ),

    val pinModalText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    ),

    val settingsItemText: TextStyle = baseStyle.copy(
        fontSize = 16.sp,
        fontWeight = FontWeight.Normal
    )
)