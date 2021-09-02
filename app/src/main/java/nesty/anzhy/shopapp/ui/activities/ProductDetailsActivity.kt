package nesty.anzhy.shopapp.ui.activities

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import nesty.anzhy.shopapp.R
import nesty.anzhy.shopapp.databinding.ActivityProductDetailsBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Cart
import nesty.anzhy.shopapp.models.Product
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader

class ProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProductDetailsBinding;

    private lateinit var mProductDetails: Product
    private var mProductOwnerId: String = ""

    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)) {
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        //var productOwnerId :String = ""

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            mProductOwnerId = intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }


        if(mProductOwnerId != Firestore().getCurrentUserID()){
            binding.btnAddToCart.visibility = View.VISIBLE
        }
        else {
            binding.btnAddToCart.visibility = View.GONE
        }

        getProductDetails()


        binding.btnAddToCart.setOnClickListener{
            addToCart()
        }
        binding.btnGoToCart.setOnClickListener{
            startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
        }
    }

    private fun getProductDetails(){
        Firestore().getProductDetails(this, mProductId)
    }

    fun productExistsInCart(){
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE

    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product


        GlideLoader(this@ProductDetailsActivity)
            .loadProductPicture(product.image,
                binding.imageViewProductDetails)

        binding.productDetailsPrice.text = "$${product.price}"
        binding.textViewProductDetailQuantity.text = product.stock_quantity
        binding.productDetailsTitle.text = product.title
        binding.productDetailsInformation.text = product.description

        Firestore().checkIfItemExistInCart(this, mProductId)

        if(product.stock_quantity.toInt()==0){
            binding.btnAddToCart.visibility = View.GONE
            binding.textViewProductDetailQuantity.text = "out of stock"
            binding.textViewProductDetailQuantity.setTextColor(
                ContextCompat.getColor(this, R.color.colorAccent)
            )
        }
        else {
            Firestore().checkIfItemExistInCart(this,mProductId)
        }

    }



    private fun addToCart(){
        val cartItem = Cart(
            Firestore().getCurrentUserID(),
            mProductOwnerId,
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )

        //we can show here progress dialog
        Firestore().addCartItems(this, cartItem)
    }

    fun addToCartSuccess(){
        Toast.makeText(this@ProductDetailsActivity,
        "Product was added to your cart.", Toast.LENGTH_SHORT).show()

        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }
}