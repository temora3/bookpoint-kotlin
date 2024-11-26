import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp

@Composable
fun SocialButton(social: String) {
    OutlinedButton(
        onClick = { /* TODO: Implement onClick action */ },
        modifier = Modifier.padding(vertical = 15.dp),
        border = BorderStroke(1.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier.width(Config.widthSize * 0.04f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.google),
                contentDescription = "Google Icon",
                modifier = Modifier.size(40.dp)
            )
            Text(
                text = social.toUpperCase(),
                color = Color.Black
            )
        }
    }
}

// Note: You'll need to implement the Config object separately
object Config {
    var widthSize: Float = 0f

    fun init(context: Context) {
        // Initialize widthSize based on your requirements
    }
}

