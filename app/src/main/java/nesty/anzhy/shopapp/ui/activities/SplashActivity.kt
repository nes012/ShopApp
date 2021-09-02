package nesty.anzhy.shopapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import nesty.anzhy.shopapp.databinding.ActivitySplashBinding
import nesty.anzhy.shopapp.firestore.Firestore

class SplashActivity : AppCompatActivity(){
    private lateinit var binding: ActivitySplashBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

    Handler(Looper.getMainLooper()).postDelayed({
        // Get the current logged in user id
        val currentUserID = Firestore().getCurrentUserID()
        if(currentUserID.isNotEmpty()){
            // Launch dashboard screen.
            startActivity(Intent(this@SplashActivity, DashboardActivity::class.java))
        }
        else {
            // Launch the Login Activity
            startActivity(Intent(this@SplashActivity, LoginActivity::class.java))
        }
        finish()
        }, 1500)
    }
}