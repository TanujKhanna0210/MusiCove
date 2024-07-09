package com.example.musicove.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.musicove.R

// Set of Material typography styles to start with
private val nunito = FontFamily(
    Font(R.font.nunito_regular, FontWeight.W400),
    Font(R.font.nunito_medium, FontWeight.W500),
    Font(R.font.nunito_bold, FontWeight.W600),
)

val Typography = Typography(
    headlineLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W500,
        fontSize = 30.sp
    ),
    headlineMedium = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W500,
        fontSize = 24.sp
    ),
    headlineSmall = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W500,
        fontSize = 20.sp
    ),
    titleLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W400,
        fontSize = 18.sp
    ),
    titleMedium = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp
    ),
    titleSmall = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp
    ),
    labelLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W500,
        fontSize = 16.sp,
    ),
    labelMedium = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = nunito,
        fontSize = 14.sp
    ),
    displayLarge = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W400,
        fontSize = 15.sp,
        color = Color.White
    ),
    displayMedium = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    ),
    displaySmall = TextStyle(
        fontFamily = nunito,
        fontWeight = FontWeight.W400,
        fontSize = 10.sp
    )
)