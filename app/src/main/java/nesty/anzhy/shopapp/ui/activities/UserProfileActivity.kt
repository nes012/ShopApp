package nesty.anzhy.shopapp.ui.activities

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import nesty.anzhy.shopapp.databinding.ActivityUserProfileBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.User
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader
import java.io.IOException

class UserProfileActivity : AppCompatActivity() {

    private var mSelectedImageUri: Uri? = null

    private lateinit var binding: ActivityUserProfileBinding
    private var mUserProfileImageURL: String = ""

    private lateinit var mUserDetails: User


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_USER_DETAILS)) {
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        binding.etUserEmail.setText(mUserDetails.email)
        binding.etUserEmail.isEnabled = false

        binding.etUserFirstName.setText(mUserDetails.firstName)
        binding.etUserLastName.setText(mUserDetails.lastName)


        if(mUserDetails.profileCompleted == 0){
            binding.tvTitle.text = "COMPLETE PROFILE"
            binding.etUserFirstName.isEnabled = false
            binding.etUserLastName.isEnabled = false

        }
        else {
            binding.tvTitle.text = "EDIT PROFILE"
            GlideLoader(this@UserProfileActivity)
                .loadUserPicture(mUserDetails.image, binding.userImage)
            binding.etUserFirstName.setText(mUserDetails.firstName)
            binding.etUserLastName.setText(mUserDetails.lastName)
            if(mUserDetails.mobile!=0L)
            binding.etUserMobileNumber.setText(mUserDetails.mobile.toString())

            if(mUserDetails.gender == Constants.MALE)
            binding.btnUserMale.isChecked = true
            else binding.btnUserFemale.isChecked = true
        }



        binding.userImage.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "You already have storage permission", Toast.LENGTH_LONG)
                    .show()

                Constants.showImageChooser(this)

            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }



        binding.btnSaveUser.setOnClickListener {
            if (validateUserProfileDetails())
                Toast.makeText(
                    this@UserProfileActivity,
                    "Your details are valid. You can update them.", Toast.LENGTH_LONG
                ).show()

            if(mSelectedImageUri!=null)
            Firestore().uploadImageToCloudStorage(this, mSelectedImageUri, Constants.USER_PROFILE_IMAGE)
            else{
                updateUserProfileDetails()
            }

        }
    }

    private fun updateUserProfileDetails(){
        val userHashMap = HashMap<String, Any>()

        val firstName = binding.etUserFirstName.text.toString()
            .trim{it<=' '}

        if(firstName != mUserDetails.firstName){
            userHashMap[Constants.FIRST_NAME] = firstName
        }
        val lastName = binding.etUserLastName.text.toString()
            .trim{it<=' '}

        if(lastName != mUserDetails.lastName){
            userHashMap[Constants.LAST_NAME] = lastName
        }

        if(mUserProfileImageURL.isNotEmpty()){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
        }


        if (binding.etUserMobileNumber.text.toString().isNotEmpty()
            && binding.etUserMobileNumber.text.toString()!=mUserDetails.mobile.toString()) {
            userHashMap[Constants.MOBILE] = binding.etUserMobileNumber.text.toString().toLong()
        }

        val gender = if (binding.btnUserMale.isChecked) {
            Constants.MALE
        } else {
            Constants.FEMALE
        }
        if(gender!=mUserDetails.gender) {
            userHashMap[Constants.GENDER] = gender
        }
      //  userHashMap[Constants.GENDER] = gender
        userHashMap[Constants.COMPLETE_PROFILE] = 1

        Firestore().updateUserProfileData(
            this@UserProfileActivity,
            userHashMap
        )
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this)

                Toast.makeText(
                    this@UserProfileActivity,
                    "Permission granted to storage", Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@UserProfileActivity,
                    "Permission to storage denied", Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.PICK_IMAGE_REQUEST_CODE) {
                if (data != null) {
                    try {
                        mSelectedImageUri = data.data!!
                        // binding.userImage.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(mSelectedImageUri!!, binding.userImage)
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@UserProfileActivity,
                            "image selection failed", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }


    private fun validateUserProfileDetails(): Boolean {
        return when {
            TextUtils.isEmpty(
                binding.etUserMobileNumber.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(this@UserProfileActivity, "enter mobile number", Toast.LENGTH_LONG)
                    .show()
                false
            }
            else -> {
                true
            }

        }
    }


    fun userProfileUpdateSuccess() {
        // Hide the progress dialog
        Toast.makeText(
            this@UserProfileActivity,
            "profile update success",
            Toast.LENGTH_SHORT
        ).show()
        // Redirect to the Main Screen after profile completion.
        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
        finish()
    }


    fun imageUploadSuccess(imageURL: String) {
        mUserProfileImageURL = imageURL
        updateUserProfileDetails()
    }
}