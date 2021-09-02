package nesty.anzhy.shopapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import nesty.anzhy.shopapp.databinding.ActivitySettingsBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.User
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader



class SettingsActivity : AppCompatActivity() {

    private lateinit var mUserDetails: User


    private lateinit var binding: ActivitySettingsBinding;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnEditSettings.setOnClickListener{
            val intent = Intent(this@SettingsActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
            startActivity(intent)
        }

        binding.btnLogoutSettings.setOnClickListener{
            FirebaseAuth.getInstance().signOut()

            val intent = Intent(this@SettingsActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()

        }


        binding.etAddressesSettings.setOnClickListener{
            val intent = Intent(this@SettingsActivity, AddressListActivity::class.java)
            startActivity(intent)
        }

    }

    private fun getUserDetails(){
        Firestore().getUserDetails(this@SettingsActivity)
    }

    fun userDetailsSuccess(user: User){
        mUserDetails = user

        GlideLoader(this@SettingsActivity)
           .loadUserPicture(user.image, binding.imageViewUser)

        binding.textViewSettingsName.text = "${user.firstName} ${user.lastName}"
        binding.textViewSettingsGender.text = user.gender
        binding.textViewSettingsEmail.text = user.email
        binding.textViewSettingsPhoneNumber.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()

        getUserDetails()
    }
}