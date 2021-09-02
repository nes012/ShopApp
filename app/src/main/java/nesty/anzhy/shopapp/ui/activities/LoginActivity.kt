package nesty.anzhy.shopapp.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import nesty.anzhy.shopapp.databinding.ActivityLoginBinding;
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.User
import nesty.anzhy.shopapp.utils.Constants

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.textViewRegister.setOnClickListener {
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
        }

        binding.btnLogin.setOnClickListener {
            logInRegisteredUser()
        }

        binding.txtForgotPassword.setOnClickListener {
            startActivity(Intent(this@LoginActivity, ForgotPasswordActivity::class.java))
        }
    }


    private fun validateLogin(): Boolean {
        when {
            TextUtils.isEmpty(
                binding.editTextTextEmailAddress.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter email.", Toast.LENGTH_SHORT
                ).show()
                false
            }
            TextUtils.isEmpty(
                binding.editTextTextPassword.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@LoginActivity,
                    "Please enter password.", Toast.LENGTH_SHORT
                ).show()
                false
            }
            else -> {
                true
            }
        }
        return true
    }

    private fun logInRegisteredUser() {
        if (validateLogin()) {
            val email: String =
                binding.editTextTextEmailAddress.text.toString().trim { it <= ' ' }
            val password: String =
                binding.editTextTextPassword.text.toString().trim { it <= ' ' }

            // Log-In using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Firestore().getUserDetails(this@LoginActivity)
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            task.exception!!.message.toString(),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }


    fun userLoggedInSuccess(user: User) {
        if (user.profileCompleted == 0) {
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, user)
            startActivity(intent)

        } else {
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }
}