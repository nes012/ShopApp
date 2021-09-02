package nesty.anzhy.shopapp.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import nesty.anzhy.shopapp.adapter.CartAdapter
import nesty.anzhy.shopapp.databinding.ActivityCartBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Cart
import nesty.anzhy.shopapp.models.Product
import nesty.anzhy.shopapp.utils.Constants

class CartListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCartBinding

    private lateinit var mProductsList: ArrayList<Product>
    private lateinit var mCartLists:ArrayList<Cart>


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityCartBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCheckout.setOnClickListener{
            val intent = Intent(this@CartListActivity, AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS, true)
            startActivity(intent)
        }

        getProductList()

    }

    fun successCartItemList(cartList: ArrayList<Cart>){

        for(product in mProductsList){
            for(cartItem in cartList){
                if(product.product_id == cartItem.product_id){
                    cartItem.stock_quantity = product.stock_quantity
                    
                    if(product.stock_quantity.toInt()==0){
                        cartItem.cart_quantity = product.stock_quantity
                    }
                }
            }
        }

        mCartLists = cartList

        if(mCartLists.size>0){

            binding.layoutCheckout.visibility - View.VISIBLE
            binding.cartListRecyclerView.visibility = View.VISIBLE
            binding.cartActivityNoItemFoundInCart.visibility = View.INVISIBLE
            binding.cartListRecyclerView.layoutManager = LinearLayoutManager(this@CartListActivity)
            binding.cartListRecyclerView.setHasFixedSize(true)

            val adapter = CartAdapter(this@CartListActivity, mCartLists, true)
            binding.cartListRecyclerView.adapter = adapter


            var subtotal = 0.0
            for(item in mCartLists){
                val availableQuantity = item.stock_quantity.toInt()
                if(availableQuantity>0) {
                    val price = item.price.toDouble()
                    val quantity = item.cart_quantity.toInt()
                    subtotal += (price * quantity)
                }
            }
            if(subtotal>0){
                binding.textViewSubtotal.text = "$${subtotal}"
                binding.textViewShippingCharge.text = "$10"
                binding.layoutCheckout.visibility - View.VISIBLE
                binding.textViewTotalAmount.text = "${subtotal+10}"
            }
        }
        else{
            binding.cartListRecyclerView.visibility = View.INVISIBLE
            binding.layoutCheckout.visibility = View.INVISIBLE
            binding.cartActivityNoItemFoundInCart.visibility = View.VISIBLE
        }
    }

    private fun getCartItemsList(){
        Firestore().getCartList(this@CartListActivity)
    }


    private fun getProductList(){
        Firestore().getAllProductList(this@CartListActivity)
    }

    fun successProductsListFromFirestore(productsList: ArrayList<Product>){
        //progressDialog hide

        mProductsList = productsList

        getCartItemsList()
    }

    fun itemRemovedSuccess(){
        //hide progress dialog
        Toast.makeText(this@CartListActivity,
        "Item removed successfully", Toast.LENGTH_SHORT).show()

        getCartItemsList()
    }

    fun itemUpdateSuccess(){
        //hide progress dialog
        Toast.makeText(this@CartListActivity,
            "Item updated successfully", Toast.LENGTH_SHORT).show()

        getCartItemsList()
    }

    override fun onResume() {
        super.onResume()
        //getCartItemsList()
        getProductList()
    }
}