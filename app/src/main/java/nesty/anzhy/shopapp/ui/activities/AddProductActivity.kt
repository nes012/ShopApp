package nesty.anzhy.shopapp.ui.activities

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import nesty.anzhy.shopapp.R
import nesty.anzhy.shopapp.databinding.ActivityAddProductBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Product
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader
import java.io.IOException


class AddProductActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAddProductBinding

    private var mSelectedImageUri: Uri? = null
    private var mProductImageURL: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddProductBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imgButtonBack.setOnClickListener {
            onBackPressed()
        }

        binding.addProduct.setOnClickListener {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
                == PackageManager.PERMISSION_GRANTED
            ) {
                Constants.showImageChooser(this@AddProductActivity)
            } else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )
            }
        }

        binding.btnAddProductSubmit.setOnClickListener {
            validateProductDetails()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Constants.showImageChooser(this)

                Toast.makeText(
                    this@AddProductActivity,
                    "Permission granted to storage", Toast.LENGTH_LONG
                ).show()
            } else {
                Toast.makeText(
                    this@AddProductActivity,
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
                        binding.addProduct.setImageDrawable(
                            ContextCompat.getDrawable(
                                this,
                                R.drawable.edit
                            )
                        )
                        mSelectedImageUri = data.data!!
                        // binding.userImage.setImageURI(Uri.parse(selectedImageFileUri.toString()))
                        GlideLoader(this).loadUserPicture(
                            mSelectedImageUri!!,
                            binding.viewForImage
                        )
                    } catch (e: IOException) {
                        e.printStackTrace()
                        Toast.makeText(
                            this@AddProductActivity,
                            "image selection failed", Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
        }
    }

    private fun validateProductDetails(): Boolean {
        return when {
            mSelectedImageUri == null -> {
                Toast.makeText(
                    this@AddProductActivity,
                    "please select product image", Toast.LENGTH_LONG
                ).show()
                false
            }

            TextUtils.isEmpty(binding.etProductTitle.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@AddProductActivity,
                    "please enter product title", Toast.LENGTH_LONG
                ).show()
                false
            }

            TextUtils.isEmpty(binding.etProductPrice.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@AddProductActivity,
                    "please enter product price", Toast.LENGTH_LONG
                ).show()
                false
            }

            TextUtils.isEmpty(binding.etProductDescription.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@AddProductActivity,
                    "please enter description", Toast.LENGTH_LONG
                ).show()
                false
            }

            TextUtils.isEmpty(binding.etProductQuantity.text.toString().trim { it <= ' ' }) -> {
                Toast.makeText(
                    this@AddProductActivity,
                    "please enter quantity", Toast.LENGTH_LONG
                ).show()
                false
            }

            else -> {
                Toast.makeText(
                    this@AddProductActivity,
                    "Your product was added", Toast.LENGTH_LONG
                ).show()

                uploadProductImage()
                true
            }
        }

    }

    fun imageUploadSuccess(imageURL: String) {
        /*
        Toast.makeText(
            this@AddProductActivity,
            "Product image update successfully. Image URL: $imageURL", Toast.LENGTH_LONG
        ).show()
         */
        mProductImageURL = imageURL

        uploadProductDetails()
    }

    fun productUploadSuccess(){
        Toast.makeText(
            this@AddProductActivity,
            "Your product was added", Toast.LENGTH_LONG
        ).show()

        finish()
    }




    private fun uploadProductDetails(){
        val username = this.getSharedPreferences(
            Constants.SHOPAPP_PREFERENCES, MODE_PRIVATE
        ).getString(Constants.LOGGED_IN_USERNAME, "")!!

        val product = Product(
            Firestore().getCurrentUserID(),
            username,
            binding.etProductTitle.text.toString().trim{it<=' '},
            binding.etProductPrice.text.toString().trim{it<=' '},
            binding.etProductDescription.text.toString().trim{it<=' '},
            binding.etProductQuantity.text.toString().trim{it<=' '},
            mProductImageURL
            )

        Firestore().uploadProductDetails(this@AddProductActivity, product)
    }

    private fun uploadProductImage() {
        Firestore().uploadImageToCloudStorage(this, mSelectedImageUri, Constants.PRODUCT_IMAGE)
    }
}