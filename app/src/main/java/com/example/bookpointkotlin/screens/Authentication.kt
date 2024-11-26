import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AuthenticationActivity : AppCompatActivity() {

    private var isSignIn: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authentication)

        setupUI()
    }

    private fun setupUI() {
        val welcomeTextView = findViewById<TextView>(R.id.welcomeText)
        val toggleTextView = findViewById<TextView>(R.id.toggleText)
        val formContainer = findViewById<FrameLayout>(R.id.formContainer)
        val socialLoginTextView = findViewById<TextView>(R.id.socialLoginText)
        val googleButton = findViewById<Button>(R.id.googleButton)
        val facebookButton = findViewById<Button>(R.id.facebookButton)
        val toggleButton = findViewById<Button>(R.id.toggleButton)

        updateUI(welcomeTextView, toggleTextView, formContainer)

        toggleButton.setOnClickListener {
            isSignIn = !isSignIn
            updateUI(welcomeTextView, toggleTextView, formContainer)
        }

        googleButton.setOnClickListener {
            // Handle Google login
        }

        facebookButton.setOnClickListener {
            // Handle Facebook login
        }
    }

    private fun updateUI(welcomeTextView: TextView, toggleTextView: TextView, formContainer: FrameLayout) {
        welcomeTextView.text = if (isSignIn) "Welcome Back!" else "Create an Account"
        toggleTextView.text = if (isSignIn) "Sign in to your account" else "Register for an account"

        supportFragmentManager.beginTransaction().replace(
            R.id.formContainer,
            if (isSignIn) LoginFormFragment() else SignUpFormFragment()
        ).commit()
    }

    fun onLoginSuccess(userData: JSONObject, tokenData: JSONObject) {
        lifecycleScope.launch {
            saveAuthData(userData, tokenData)
            navigateToMainScreen()
        }
    }

    private suspend fun saveAuthData(userData: JSONObject, tokenData: JSONObject) {
        withContext(Dispatchers.IO) {
            val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            sharedPreferences.edit {
                putString("userData", userData.toString())
                putString("token", tokenData.toString())
            }
        }
    }

    private fun navigateToMainScreen() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
