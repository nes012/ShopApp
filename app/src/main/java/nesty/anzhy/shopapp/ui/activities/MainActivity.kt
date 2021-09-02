package nesty.anzhy.shopapp.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import nesty.anzhy.shopapp.databinding.ActivityMainBinding
import nesty.anzhy.shopapp.utils.Constants


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //   val userId = intent.getStringExtra("user_id")
        //   val emailId = intent.getStringExtra("email_id")

        // binding.tvUserId.text = "User ID : $userId"
        //binding.tvEmailId.text = "Email ID : $emailId"

        val sharedPreferences =
            getSharedPreferences(Constants.SHOPAPP_PREFERENCES, Context.MODE_PRIVATE)
        val userName = sharedPreferences.getString(Constants.LOGGED_IN_USERNAME, "")!!

        binding.tvUserId.text = "Hello $userName."

        binding.btnLogout.setOnClickListener{
            FirebaseAuth.getInstance().signOut()
            startActivity(Intent(this@MainActivity, LoginActivity::class.java))
            finish()}

    }

}