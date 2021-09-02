package nesty.anzhy.shopapp.ui.activities

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.shopapp.adapter.AddressAdapter
import nesty.anzhy.shopapp.databinding.ActivityAddressListBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Address
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.SwipeToDeleteCallback
import nesty.anzhy.shopapp.utils.SwipeToEditCallback
import java.util.ArrayList

class AddressListActivity : AppCompatActivity() {

    private var mSelectedAddress: Boolean = false

    private lateinit var binding: ActivityAddressListBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        binding.tvAddAddress.setOnClickListener {
            val intent = Intent(this@AddressListActivity, AddEditAddressActivity::class.java)
            startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        }

        if(intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectedAddress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS, false)
        }

        if(mSelectedAddress){

            binding.addressListTitle.text = "select address"
        }

//we will call this method in onResume
        getAddressList()

    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>) {
        if(addressList.size>0){
            binding.recyclerViewAddress.visibility = View.VISIBLE
            binding.textViewNoAddressFound.visibility = View.GONE

            binding.recyclerViewAddress.layoutManager = LinearLayoutManager(applicationContext)
            binding.recyclerViewAddress.setHasFixedSize(true)

            val adapter = AddressAdapter(this, addressList, mSelectedAddress)
            binding.recyclerViewAddress.adapter = adapter

            if(!mSelectedAddress){
                val editSwipeHandler = object: SwipeToEditCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = binding.recyclerViewAddress.adapter as AddressAdapter
                        adapter.notifyEditItem(this@AddressListActivity,
                            viewHolder.adapterPosition)
                    }
                }

                val deleteSwipeHandler = object: SwipeToDeleteCallback(this){
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        Firestore().deleteAddress(this@AddressListActivity,
                            addressList[viewHolder.adapterPosition].id)
                    }

                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(binding.recyclerViewAddress)

                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(binding.recyclerViewAddress)

            }
        }
        else{
            binding.recyclerViewAddress.visibility = View.GONE
            binding.textViewNoAddressFound.visibility = View.VISIBLE

        }
    }

    fun deleteAddressSuccess(){
        Toast.makeText(
            this@AddressListActivity,
            "address deleted successfully", Toast.LENGTH_SHORT
        ).show()

        getAddressList()
    }

    private fun getAddressList(){
        Firestore().getAddressList(this)
    }

    override fun onResume() {
        super.onResume()
        getAddressList()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK){
            getAddressList()
        }
    }
}