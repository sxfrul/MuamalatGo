package com.example.muamalatgo

import android.os.Bundle
import android.view.*
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch


class LoginPendermaFragment : Fragment() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_login_penderma, container, false)

        emailInput = view.findViewById(R.id.emailInputLogin)
        passwordInput = view.findViewById(R.id.passwordInputLogin)
        loginButton = view.findViewById(R.id.buttonLoginPenderma)

        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        return view
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()

                    // 🔥 BACKEND PING HERE
                    lifecycleScope.launch {
                        try {
                            val response = NetworkClient.api.hello()
                            if (response.isSuccessful) {
                                Toast.makeText(context, "Server: ${response.body()}", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Server error: ${response.code()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Connection failed: ${e.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    // TODO: Navigate to dashboard if needed
                } else {
                    Toast.makeText(context, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

}
