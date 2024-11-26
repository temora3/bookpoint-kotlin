import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.IconButton
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import com.example.bookpoint.R
import com.example.bookpoint.components.Button
import com.example.bookpoint.models.AuthModel
import com.example.bookpoint.providers.DioProvider
import com.example.bookpoint.utils.Config
import com.google.android.material.textfield.TextInputLayout

class SignUpForm : AppCompatActivity() {
    private lateinit var formKey: String
    private lateinit var idController: EditText
    private lateinit var fnameController: EditText
    private lateinit var lnameController: EditText
    private lateinit var emailController: EditText
    private lateinit var passController: EditText
    private lateinit var confirmPassController: EditText
    private var obscurePass = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_form)

        formKey = "signUpForm"
        idController = findViewById(R.id.idEditText)
        fnameController = findViewById(R.id.fnameEditText)
        lnameController = findViewById(R.id.lnameEditText)
        emailController = findViewById(R.id.emailEditText)
        passController = findViewById(R.id.passEditText)
        confirmPassController = findViewById(R.id.confirmPassEditText)

        setupTextFields()
        setupSignUpButton()
    }

    private fun setupTextFields() {
        val textFields = listOf(
            Triple(R.id.fnameInputLayout, R.string.first_name_hint, R.drawable.ic_person),
            Triple(R.id.lnameInputLayout, R.string.last_name_hint, R.drawable.ic_person),
            Triple(R.id.emailInputLayout, R.string.email_hint, R.drawable.ic_email),
            Triple(R.id.idInputLayout, R.string.id_hint, R.drawable.ic_badge),
            Triple(R.id.passInputLayout, R.string.password_hint, R.drawable.ic_lock),
            Triple(R.id.confirmPassInputLayout, R.string.confirm_password_hint, R.drawable.ic_lock)
        )

        textFields.forEach { (layoutId, hintId, iconId) ->
            val layout = findViewById<TextInputLayout>(layoutId)
            layout.apply {
                hint = getString(hintId)
                setStartIconDrawable(iconId)
                setStartIconTintList(Config.primaryColor)
            }
        }

        setupPasswordVisibility(R.id.passInputLayout)
        setupPasswordVisibility(R.id.confirmPassInputLayout)
    }

    private fun setupPasswordVisibility(layoutId: Int) {
        val layout = findViewById<TextInputLayout>(layoutId)
        layout.setEndIconOnClickListener {
            obscurePass = !obscurePass
            layout.endIconDrawable = getDrawable(
                if (obscurePass) R.drawable.ic_visibility_off
                else R.drawable.ic_visibility
            )
            layout.editText?.transformationMethod = if (obscurePass) {
                android.text.method.PasswordTransformationMethod.getInstance()
            } else {
                android.text.method.HideReturnsTransformationMethod.getInstance()
            }
        }
    }

    private fun setupSignUpButton() {
        val signUpButton = findViewById<Button>(R.id.signUpButton)
        val authModel = ViewModelProvider(this).get(AuthModel::class.java)

        signUpButton.setOnClickListener {
            if (validateForm()) {
                signUp(authModel)
            }
        }
    }

    private fun validateForm(): Boolean {
        // Add your form validation logic here
        return true
    }

    private fun signUp(authModel: AuthModel) {
        val dioProvider = DioProvider()
        dioProvider.registerUser(
            emailController.text.toString(),
            passController.text.toString(),
            confirmPassController.text.toString()
        ) { success ->
            if (success) {
                dioProvider.getToken(
                    emailController.text.toString(),
                    passController.text.toString()
                ) { token ->
                    if (token) {
                        authModel.loginSuccess(emptyMap(), emptyMap())
                        // Navigate to main page
                        // You might want to use Android's Navigation component or start a new activity here
                    }
                }
            } else {
                println("Register not successful")
            }
        }
    }
}

