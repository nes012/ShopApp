package nesty.anzhy.shopapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import nesty.anzhy.shopapp.R
import nesty.anzhy.shopapp.databinding.ActivityRegisterBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.User

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtRegitserLogin.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        binding.backPressed.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            validateRegisterDetails()
            registerUser()
        }

        binding.txtRegitserLogin.setOnClickListener {
            onBackPressed()
        }
    }



    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etRegisterFirstName.text.toString().trim { it <= ' ' }) -> {
                binding.etRegisterFirstName.setError("enter your first name")
                false
            }

            TextUtils.isEmpty(binding.etRegisterLastName.text.toString().trim { it <= ' ' }) -> {
                binding.etRegisterLastName.setError("enter your last name")
                false
            }

            TextUtils.isEmpty(binding.etRegisterEmail.text.toString().trim { it <= ' ' }) -> {
                binding.etRegisterEmail.setError("enter your email")
                false
            }

            TextUtils.isEmpty(binding.etRegisterPassword.text.toString().trim { it <= ' ' }) -> {
                binding.etRegisterPassword.setError("enter your password")
                false
            }

            TextUtils.isEmpty(binding.etRegisterConfirmPassword.text.toString().trim { it <= ' ' }) -> {
                binding.etRegisterConfirmPassword.setError("confirm your password")
                false
            }

            binding.etRegisterPassword.text.toString().trim { it <= ' ' }!= binding.etRegisterConfirmPassword.text.toString()
                .trim { it <= ' ' } -> {
                Toast.makeText(this@RegisterActivity, "password mismatch", Toast.LENGTH_LONG).show()
                false
            }
            !binding.checkBoxRegisterAgreeCondition.isChecked -> {
                Toast.makeText(this@RegisterActivity, "Please agree terms and condition", Toast.LENGTH_LONG).show()
                false
            }
            else -> {
                // TODO Step 4: Remove this success message as we are now validating and registering the user.
                true
            }
        }
    }

    // TODO Step 2: Create a function to register the user with email and password using FirebaseAuth.
    // START
    /**
     * A function to register the user with email and password using FirebaseAuth.
     */
    private fun registerUser() {

        // Check with validate function if the entries are valid or not.
        if (validateRegisterDetails()) {

            val email: String = binding.etRegisterEmail.text.toString().trim { it <= ' ' }
            val password: String = binding.etRegisterPassword.text.toString().trim { it <= ' ' }

            // Create an instance and create a register a user with email and password.
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        // If the registration is successfully done
                        if (task.isSuccessful) {

                            // Firebase registered user
                            val firebaseUser: FirebaseUser = task.result!!.user!!

                            //создаем юзер. И его данные мы будем закидывать в firebaseUser(данные имя, фамилия и тп)
                            //мы загружаем класс User из нашего моделс
                            val user = User(
                                //айди получаем из firebaseUser
                            firebaseUser.uid,
                                binding.etRegisterFirstName.text.toString().trim{it<=' '},
                                binding.etRegisterLastName.text.toString().trim{it<=' '},
                                binding.etRegisterEmail.text.toString().trim{it<=' '}
                            )

                            Firestore().registerUser(this@RegisterActivity, user)

                            userRegistrationSuccess()


                           // FirebaseAuth.getInstance().signOut()
                           // finish()

                        } else {
                            // If the registering is not successful then show error message.
                           Toast.makeText(this@RegisterActivity, task.exception!!.message.toString(), Toast.LENGTH_LONG).show()
                        }
                    })
        }
    }



    fun userRegistrationSuccess(){
        Toast.makeText(this@RegisterActivity,
            "You are registered successfully.",
            Toast.LENGTH_LONG).show()
    }

}
