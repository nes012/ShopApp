package nesty.anzhy.shopapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import nesty.anzhy.shopapp.databinding.ActivitySoldProductDetailsBinding
import nesty.anzhy.shopapp.models.SoldProduct
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySoldProductDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySoldProductDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var productDetailsSoldProduct = SoldProduct()

        if(intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)){
            productDetailsSoldProduct = intent.getParcelableExtra<SoldProduct>(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }

        setupUI(productDetailsSoldProduct)

    }

    private fun setupUI(productDetails: SoldProduct){
        binding.tvInformationOrderId.text = productDetails.id
        val dateFormat = "dd MMM yyyy HH:mm"
        // Create a DateFormatter object for displaying date in specified format.
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = productDetails.order_date
        binding.tvInformationDateSoldDetails.text = formatter.format(calendar.time)

        GlideLoader(this@SoldProductDetailsActivity).loadProductPicture(
            productDetails.image,
            binding.imageViewSoldProductDetail
        )
        binding.tvSoldProductDetailsName.text = productDetails.title
        binding.tvSoldProductDetailsPrice.text ="$${productDetails.price}"
        binding.tvSoldProductDetailsQuantity.text = productDetails.sold_quantity

        binding.tvAddressTypeOrderSoldDetails.text = productDetails.address.type
        binding.tvFullNameOrderSoldDetails.text = productDetails.address.name
        binding.tvAddressOrderSoldDetails.text =
            "${productDetails.address.address}, ${productDetails.address.zipCode}"
        binding.tvAdditionalNoteOrderSoldDetails.text = productDetails.address.additionalNote

        if (productDetails.address.otherDetails.isNotEmpty()) {
            binding.tvOtherDetailsOrderSoldDetails.visibility = View.VISIBLE
            binding.tvOtherDetailsOrderSoldDetails.text = productDetails.address.otherDetails
        } else {
            binding.tvOtherDetailsOrderSoldDetails.visibility = View.GONE
        }
        binding.tvMobileNumberOrderSoldDetails.text = productDetails.address.mobileNumber

        binding.tvSubtotalAmountOrderSoldDetails.text = productDetails.subtotal_amount
        binding.tvShippingChargeAmountOrderSoldDetails.text = productDetails.shipping_charge
        binding.totalAmountOfOrder.text = productDetails.total_amount
    }
}
