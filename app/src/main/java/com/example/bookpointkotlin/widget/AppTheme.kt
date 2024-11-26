package com.example.bookpointkotlin.widget

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color as ComposeColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import com.google.accompanist.systemuicontroller.rememberSystemUiController

object AppTheme {
    @Composable
    fun themeData(darkTheme: Boolean = isSystemInDarkTheme()): MaterialTheme {
        val colorScheme = _colorScheme(darkTheme)
        val typography = _textTheme()
        val shapes = _shapes()

        val systemUiController = rememberSystemUiController()
        systemUiController.setSystemBarsColor(
            color = colorScheme.primary
        )

        return MaterialTheme(
            colorScheme = colorScheme,
            typography = typography,
            shapes = shapes
        )
    }

    @Composable
    private fun _colorScheme(darkTheme: Boolean): ColorScheme {
        return ColorScheme(
            primary = ComposeColor(0xFF909CDF),
            onPrimary = ComposeColor(0xFFFFFFFF),
            primaryContainer = ComposeColor(0xFFF2F3FB),
            onPrimaryContainer = ComposeColor(0xFF000000),
            secondary = ComposeColor(0xFF9C254D),
            onSecondary = ComposeColor(0xFFFFFFFF),
            secondaryContainer = ComposeColor(0xFFDFA3B7),
            tertiary = ComposeColor(0xFFB6C2FF),
            tertiaryContainer = ComposeColor(0xFFFFFFFF),
            errorContainer = ComposeColor(0xFFFCD8DF),
            onErrorContainer = ComposeColor(0xFF000000),
            surfaceContainer = ComposeColor(0xFFEEEEEE),
            outline = ComposeColor(0xFF737373),
            outlineVariant = ComposeColor(0xFFBFBFBF),
            inverseSurface = ComposeColor(0xFF121212),
            surfaceTint = ComposeColor(0xFF909CDF),
            error = ComposeColor(0xFF5E162E),
            onError = ComposeColor(0xFFF5E9ED),
            surface = ComposeColor(0xFFF4F5FC),
            onSurface = ComposeColor(0xFF0E1016)
        )
    }

    @Composable
    private fun _textTheme(): Typography {
        val bodyFont = FontFamily.Default // Replace with actual Google Font
        val headingFont = FontFamily.Default // Replace with actual Google Font

        return Typography(
            displayLarge = TextStyle(fontFamily = headingFont),
            displayMedium = TextStyle(fontFamily = headingFont),
            displaySmall = TextStyle(fontFamily = headingFont),
            headlineLarge = TextStyle(fontFamily = headingFont),
            headlineMedium = TextStyle(fontFamily = headingFont),
            headlineSmall = TextStyle(fontFamily = headingFont),
            bodyLarge = TextStyle(fontFamily = bodyFont),
            bodyMedium = TextStyle(fontFamily = bodyFont),
            bodySmall = TextStyle(fontFamily = bodyFont)
        )
    }

    @Composable
    private fun _shapes(): Shapes {
        return Shapes(
            small = RoundedCornerShape(4.dp),
            medium = RoundedCornerShape(8.dp),
            large = RoundedCornerShape(12.dp)
        )
    }

    @Composable
    fun _inputDecorationTheme(): TextFieldDefaults {
        return TextFieldDefaults.textFieldColors(
            textColor = MaterialTheme.colorScheme.onSurface,
            containerColor = MaterialTheme.colorScheme.surface,
            focusedIndicatorColor = ComposeColor.Transparent,
            unfocusedIndicatorColor = ComposeColor.Transparent,
            disabledIndicatorColor = ComposeColor.Transparent
        )
    }

    @Composable
    fun _filledButtonTheme(): ButtonDefaults {
        return ButtonDefaults.buttonColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = ComposeColor.White
        )
    }
}

