package com.example.bookpointkotlin.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Explore
import androidx.compose.material.icons.filled.Home
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview

@Composable
class MainNavBar() {
    var selectedIndex by remember { mutableStateOf(0) }
    val items = listOf("Discover", "Explore", "Booking", "Chat")
    val icons = listOf(
        Icons.Filled.Home,
        Icons.Filled.Explore,
        Icons.Filled.ChatBubbleOutline,
        Icons.Filled.Chat
    )

    Column {
        BottomNavigation {
            items.forEachIndexed { index, item ->
                BottomNavigationItem(
                    icon = { Icon(icons[index], contentDescription = item) },
                    label = { Text(item) },
                    selected = selectedIndex == index,
                    onClick = {
                        selectedIndex = index
                        when (index) {
                            0 -> println(index)
                            1 -> println(index)
                            else -> {}
                        }
                    },
                    selectedContentColor = Color.Primary,
                    unselectedContentColor = Color.OnSurface.copy(alpha = 0.5f)
                )
            }
        }
    }
}

@Preview
@Composable
fun MainNavBarPreview() {
    MainNavBar()
}

