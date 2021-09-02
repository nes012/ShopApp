package nesty.anzhy.shopapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import nesty.anzhy.shopapp.adapter.CartAdapter
import nesty.anzhy.shopapp.databinding.ActivityCheckoutBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Address
import nesty.anzhy.shopapp.models.Cart
import nesty.anzhy.shopapp.models.Order
import nesty.anzhy.shopapp.models.Product
import nesty.anzhy.shopapp.utils.Constants
import java.nio.file.FileStore
import java.nio.file.Files

class CheckoutActivity : AppCompatActivity() {

    private var mAddressDetails: Address? = null
    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartItemList: ArrayList<Cart>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0

    private lateinit var mOrderDetails: Order


    private lateinit var binding: ActivityCheckoutBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails =
                intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)!!
        }

        if(mAddressDetails != null){
            binding.tvAddressTypeCheckout.text = mAddressDetails?.type
            binding.tvFullNameCheckout.text = mAddressDetails?.name
            binding.tvAddressCheckout.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvAdditionalNoteCheckout.text = mAddressDetails?.additionalNote

            if(mAddressDetails?.otherDetails!!.isNotEmpty()){
                binding.tvOtherDetailsCheckout.text = mAddressDetails?.otherDetails
            }

            binding.tvMobileNumberCheckout.text = mAddressDetails?.mobileNumber
        }

        binding.btnPlaceOrderCheckout.setOnClickListener{
            placeAnOrder()
        }

        getProductList()
    }

    fun successProductsListFromFirestore(productsList: ArrayList<Product>){
        mProductList = productsList

        getCartItemList()
    }

    private fun getCartItemList(){
        Firestore().getCartList(this@CheckoutActivity)
    }

    fun successCartItemList(cartList: ArrayList<Cart>){
        for(product in mProductList){
            for(cartItem in cartList){
                if(product.product_id == cartItem.product_id){
                    cartItem.stock_quantity = product.stock_quantity
                }
            }
        }

        mCartItemList = cartList

        binding.rvCheckout.layoutManager = LinearLayoutManager(this@CheckoutActivity)
        binding.rvCheckout.setHasFixedSize(true)

        val cartListAdapter = CartAdapter(this@CheckoutActivity, mCartItemList, false)
        binding.rvCheckout.adapter = cartListAdapter


        for(item in mCartItemList){
            val availableQuantity = item.stock_quantity.toInt()
            if(availableQuantity>0){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal+=(price*quantity)
            }
        }


        /*
        var subTotal: Double = 0.0
        for(item in mCartItemList){
            val availableQuantity = item.stock_quantity.toInt()
            if(availableQuantity > 0){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)
            }
        }
         */

        binding.tvSubtotalAmount.text = "$$mSubTotal"
        binding.tvShippingChargeAmountCheckout.text = "$10.0"

        if(mSubTotal >0){
            binding.btnPlaceOrderCheckout.visibility = View.VISIBLE
            mTotalAmount = mSubTotal + 10
            binding.totalAmountOfOrder.text = "$$mTotalAmount"
        }
        else{
            binding.btnPlaceOrderCheckout.visibility = View.GONE
        }
    }

    private fun placeAnOrder(){
        if(mAddressDetails!=null) {
             mOrderDetails = Order(
                Firestore().getCurrentUserID(),
                mCartItemList,
                mAddressDetails!!,
                "My order ${System.currentTimeMillis()}",
                mCartItemList[0].image,
                mSubTotal.toString(),
                "10.0",
                mTotalAmount.toString(),
                System.currentTimeMillis()
            )
            Firestore().placeOrder(this@CheckoutActivity, mOrderDetails)
        }
    }

    fun orderPlacedSuccess(){
        Firestore().updateAllDetails(this, mCartItemList, mOrderDetails)
    }

    fun allDetailsUpdatedSuccessfully(){
        Toast.makeText(this@CheckoutActivity,
            "Your order placed successfully", Toast.LENGTH_SHORT).show()

        val intent = Intent(this@CheckoutActivity, DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }


    private fun getProductList() {
        // Show the progress dialog.
        Firestore().getAllProductList(this@CheckoutActivity)
    }
}