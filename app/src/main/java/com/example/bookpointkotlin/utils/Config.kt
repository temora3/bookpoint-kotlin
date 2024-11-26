package com.example.bookpointkotlin.utils

import android.content.Context
import android.util.DisplayMetrics
import android.view.WindowManager
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

object Config {
    private var screenWidth: Float? = null
    private var screenHeight: Float? = null

    // Width and height initialization
    fun init(context: Context) {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels.toFloat()
        screenHeight = displayMetrics.heightPixels.toFloat()
    }

    val widthSize: Float?
        get() = screenWidth

    val heightSize: Float?
        get() = screenHeight

    // Defining spacing height
    val spaceSmall = Spacer(modifier = Modifier.height(25.dp))

    val spaceMedium: Spacer
        get() = Spacer(modifier = Modifier.height((screenHeight!! * 0.05).dp))

    val spaceBig: Spacer
        get() = Spacer(modifier = Modifier.height((screenHeight!! * 0.08).dp))

    // TextField styles
    val outlinedBorder = RoundedCornerShape(8.dp)

    val focusBorder = OutlinedTextField(
        value = "",
        onValueChange = {},
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            focusedBorderColor = Color.Green
        )
    )

    val errorBorder = OutlinedTextField(
        value = "",
        onValueChange = {},
        shape = RoundedCornerShape(8.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            errorBorderColor = Color.Red
        )
    )

    val primaryColor = Color.Green
}

