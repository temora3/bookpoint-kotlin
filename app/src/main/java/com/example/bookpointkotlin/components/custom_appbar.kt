import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController

@Composable
fun CustomAppBar(
    appTitle: String,
    route: String? = null,
    icon: @Composable (() -> Unit)? = null,
    actions: @Composable (() -> Unit)? = null,
    navController: NavController
) {
    TopAppBar(
        title = {
            Text(
                text = appTitle,
                style = MaterialTheme.typography.h6.copy(
                    fontSize = 20.sp,
                    color = Color.Black
                )
            )
        },
        backgroundColor = Color.White,
        elevation = 0.dp,
        navigationIcon = icon?.let {
            {
                Box(
                    modifier = Modifier
                        .padding(horizontal = 10.dp, vertical = 10.dp)
                        .background(
                            color = Config.primaryColor,
                            shape = RoundedCornerShape(10.dp)
                        )
                ) {
                    IconButton(
                        onClick = {
                            if (route != null) {
                                navController.navigate(route)
                            } else {
                                navController.popBackStack()
                            }
                        }
                    ) {
                        icon()
                    }
                }
            }
        },
        actions = {
            actions?.invoke()
        }
    )
}

