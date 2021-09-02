package nesty.anzhy.shopapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import nesty.anzhy.shopapp.R
import nesty.anzhy.shopapp.databinding.ActivityAddEditAddressBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Address
import nesty.anzhy.shopapp.utils.Constants

class AddEditAddressActivity : AppCompatActivity() {

    private var mAddressDetails: Address? = null

    private lateinit var binding: ActivityAddEditAddressBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)) {
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }

        if (mAddressDetails != null) {
            if (mAddressDetails!!.id.isNotEmpty()) {
                binding.titleAddAddress.text = "Edit address"
                binding.btnEditSubmit.text = "Update"

                binding.etEditFullName.setText(mAddressDetails?.name)
                binding.etEditPhoneNumber.setText(mAddressDetails?.mobileNumber)
                binding.etEditAddress.setText(mAddressDetails?.address)
                binding.etEditZipCode.setText(mAddressDetails?.zipCode)
                binding.etEditAdditionalNote.setText(mAddressDetails?.additionalNote)

                when (mAddressDetails?.type) {

                    Constants.HOME -> {
                        binding.rbHome.isChecked = true
                    }
                    Constants.OFFICE -> {
                        binding.rbOffice.isChecked = true
                    }
                    else -> {
                        binding.rbOther.isChecked = true
                        binding.textInputLayoutOtherDetails.visibility = View.VISIBLE
                        binding.etOtherDetails.setText(mAddressDetails?.otherDetails.toString())
                    }
                }

            }
        }

        setSupportActionBar(binding.toolbarEditActivity)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        binding.btnEditSubmit.setOnClickListener{
            saveAddressToFirestore()
        }

        binding.radioGroupAddEditActivity.setOnCheckedChangeListener{ _, checkedId ->
            if (checkedId == R.id.rbOther) {
                binding.textInputLayoutOtherDetails.visibility = View.VISIBLE
            } else {
                binding.textInputLayoutOtherDetails.visibility = View.GONE
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }


    private fun saveAddressToFirestore() {
        val fullName: String = binding.etEditFullName.text.toString().trim { it <= ' ' }
        val address: String = binding.etEditAddress.text.toString().trim { it <= ' ' }
        val phoneNumber: String = binding.etEditPhoneNumber.text.toString().trim { it <= ' ' }
        val zipCode: String = binding.etEditZipCode.text.toString().trim { it <= ' ' }
        val additionalNote: String = binding.etEditAdditionalNote.text.toString().trim { it <= ' ' }
        val otherDetails: String = binding.etOtherDetails.text.toString().trim { it <= ' ' }


        if (validateData()) {
            val addressType: String = when {
                binding.rbHome.isChecked -> {
                    Constants.HOME
                }
                binding.rbOffice.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }

            val addressModel = Address(
                Firestore().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,
                otherDetails
            )


            if(mAddressDetails!=null &&mAddressDetails!!.id.isNotEmpty()){
                Firestore().updateAddress(this, addressModel, mAddressDetails!!.id)
            }
            else{
                Firestore().addAddress(this@AddEditAddressActivity, addressModel)
            }
        }
    }

    private fun validateData(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etEditFullName.text.toString().trim { it <= ' ' }) -> {
                binding.etEditFullName.error = "Please enter full name"
                false
            }
            TextUtils.isEmpty(binding.etEditPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                binding.etEditPhoneNumber.error = "Please enter phone number"
                false
            }
            TextUtils.isEmpty(binding.etEditAdditionalNote.text.toString().trim { it <= ' ' }) -> {
                binding.etEditAdditionalNote.error = "Please enter additional note"
                false
            }

            TextUtils.isEmpty(binding.etEditZipCode.text.toString().trim { it <= ' ' }) -> {
                binding.etEditZipCode.error = "Please enter zip code"
                false
            }
            else -> {
                true
            }
        }
    }

    fun addUpdateAddressSuccess() {

        val notifySuccessMessage:String = if (mAddressDetails != null && mAddressDetails!!.id.isNotEmpty()) {
            "your address updated successfully"
        } else {
            "Your address added successfully"
        }

        Toast.makeText(
            this@AddEditAddressActivity,
            notifySuccessMessage, Toast.LENGTH_LONG
        ).show()

        setResult(RESULT_OK)

        finish()
    }


}