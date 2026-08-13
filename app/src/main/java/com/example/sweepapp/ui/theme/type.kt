package com.example.sweepapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.sweepapp.R

val MontserratFamily = FontFamily(
    Font(R.font.montserrat, FontWeight.Normal, FontStyle.Normal),
    Font(R.font.montserrat_italic, FontWeight.Normal, FontStyle.Italic)
)

fun Typography() = Typography(
    headlineMedium = TextStyle(
        fontFamily= MontserratFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 35.sp
    ),
    titleLarge = TextStyle(
        fontFamily= MontserratFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 22.sp
    ),
    bodyLarge = TextStyle(
        fontFamily= MontserratFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 18.sp
    )
)

val AboutSweepStyle = TextStyle(
    fontFamily = MontserratFamily,
    fontWeight = FontWeight.Normal,
    fontSize = 30.sp
)