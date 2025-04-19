package com.example.muamalatgo
import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.auth.FirebaseAuth
import android.widget.EditText
import android.widget.Button
import android.widget.FrameLayout
import android.widget.Toast

class SigninPenderma : AppCompatActivity() {

    private lateinit var emailInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var buttonPenderma: Button
    private lateinit var buttonPenerima: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_signin)
        val dafterButton = findViewById<FrameLayout>(R.id.daftarPendermaFrame)
        val SignUpIntent = Intent(this, Signup_donor1::class.java)

        dafterButton.setOnClickListener {
            startActivity(SignUpIntent)
            finish()
        }

        // Handle edge insets (optional)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Bind views
        emailInput = findViewById(R.id.emailInputLogin)
        passwordInput = findViewById(R.id.passwordInputLogin)
        loginButton = findViewById(R.id.buttonLoginPenderma)
        buttonPenderma = findViewById(R.id.buttonPenderma)
        buttonPenerima = findViewById(R.id.buttonPenerima)

        // Firebase auth
        auth = FirebaseAuth.getInstance()

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(email, password)
            }
        }

        // 🟦 When Penderma button clicked: (stay on this activity or show toast)
        buttonPenderma.setOnClickListener {
            Toast.makeText(this, "Anda sedang dalam skrin Penderma", Toast.LENGTH_SHORT).show()
        }

        // 🟨 When Penerima button clicked: move to SigninPenerima activity
        buttonPenerima.setOnClickListener {
            val intent = Intent(this, SigninPenerima::class.java)
            startActivity(intent)
        }
    }

    private fun loginUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomepageDonorActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}
