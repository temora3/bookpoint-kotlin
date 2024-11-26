import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.IconButton
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class LoginForm : Fragment() {
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var signInButton: Button
    private lateinit var authViewModel: AuthViewModel
    private var obscurePass = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_login_form, container, false)

        emailEditText = view.findViewById(R.id.emailEditText)
        passwordEditText = view.findViewById(R.id.passwordEditText)
        signInButton = view.findViewById(R.id.signInButton)

        authViewModel = ViewModelProvider(requireActivity()).get(AuthViewModel::class.java)

        setupPasswordVisibilityToggle()
        setupSignInButton()

        return view
    }

    private fun setupPasswordVisibilityToggle() {
        val visibilityToggle: IconButton = view?.findViewById(R.id.passwordVisibilityToggle) ?: return
        visibilityToggle.setOnClickListener {
            obscurePass = !obscurePass
            passwordEditText.inputType = if (obscurePass) {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            } else {
                InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD
            }
            visibilityToggle.setImageResource(
                if (obscurePass) R.drawable.ic_visibility_off else R.drawable.ic_visibility
            )
        }
    }

    private fun setupSignInButton() {
        signInButton.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                val token = withContext(Dispatchers.IO) {
                    DioProvider.getToken(emailEditText.text.toString(), passwordEditText.text.toString())
                }

                if (token) {
                    val prefs = requireContext().getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
                    val tokenValue = prefs.getString("token", "") ?: ""

                    if (tokenValue.isNotEmpty()) {
                        val response = withContext(Dispatchers.IO) {
                            DioProvider.getUser(tokenValue)
                        }

                        response?.let {
                            val user = JSONObject(it)
                            var appointment: JSONObject? = null

                            val doctorArray = user.getJSONArray("doctor")
                            for (i in 0 until doctorArray.length()) {
                                val doctorData = doctorArray.getJSONObject(i)
                                if (doctorData.has("appointments")) {
                                    appointment = doctorData
                                    break
                                }
                            }

                            authViewModel.loginSuccess(user, appointment)
                            (activity as? MainActivity)?.navigateTo(R.id.mainFragment)
                        }
                    }
                }
            }
        }
    }
}

