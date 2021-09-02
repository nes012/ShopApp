package nesty.anzhy.shopapp.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import nesty.anzhy.shopapp.R
import nesty.anzhy.shopapp.adapter.CartAdapter
import nesty.anzhy.shopapp.adapter.OrdersAdapter
import nesty.anzhy.shopapp.databinding.ActivityOrderDetailsBinding
import nesty.anzhy.shopapp.models.Order
import nesty.anzhy.shopapp.utils.Constants
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

class OrderDetailsActivity : AppCompatActivity() {
    private lateinit var binding:ActivityOrderDetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var myOrderDetails: Order = Order()

        if(intent.hasExtra(Constants.EXTRA_MY_ORDER_DETAILS)!=null){
            myOrderDetails = intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDER_DETAILS)!!
        }

        setupUI(myOrderDetails)
    }


    private fun setupUI(orderDetails: Order){
        binding.orderDetailsTitle.text = orderDetails.title

        val dateFormat = "dd MMM yyyy HH:mm"
        val formatter = SimpleDateFormat(dateFormat, Locale.getDefault())
        val calendar: Calendar = Calendar.getInstance()
        calendar.timeInMillis = orderDetails.order_datetime

        val orderDateTime = formatter.format(calendar.time)
        binding.tvInformationDateOrderDetails.text = orderDateTime

        val diffInMilliSeconds: Long = System.currentTimeMillis() - orderDetails.order_datetime
        val diffInHours: Long = TimeUnit.MILLISECONDS.toHours(diffInMilliSeconds)
        Log.e("Difference in Hours", "$diffInHours")

        when {
            diffInHours < 1 -> {
                binding.tvPendingOrderDetails.text = "Pending"
                binding.tvPendingOrderDetails.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailsActivity,
                        R.color.colorAccent
                    )
                )
            }
            diffInHours < 2 -> {
                binding.tvPendingOrderDetails.text = "In process"
                binding.tvPendingOrderDetails.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailsActivity,
                        R.color.colorAccent
                    )
                )
            }
            else -> {
                binding.tvPendingOrderDetails.text = "delivered"
                binding.tvPendingOrderDetails.setTextColor(
                    ContextCompat.getColor(
                        this@OrderDetailsActivity,
                        R.color.teal_700
                    )
                )
            }
        }

        binding.rvOrderDetails.layoutManager = LinearLayoutManager(this)
        binding.rvOrderDetails.setHasFixedSize(true)

        val cartAdapter = CartAdapter(this, orderDetails.items, false)

        binding.rvOrderDetails.adapter = cartAdapter

        binding.tvAddressTypeOrderDetails.text = orderDetails.address.type
        binding.tvFullNameOrderDetails.text = orderDetails.address.name
        binding.tvInformationOrderId.text = orderDetails.id
        binding.tvAddressOrderDetails.text =
            "${orderDetails.address.address}, ${orderDetails.address.zipCode}"

        binding.tvAdditionalNoteOrderDetails.text = orderDetails.address.additionalNote

        if (orderDetails.address.otherDetails.isNotEmpty()) {
            binding.tvOtherDetailsOrderDetails.visibility = View.VISIBLE
            binding.tvOtherDetailsOrderDetails.text = orderDetails.address.otherDetails
        } else {
            binding.tvOtherDetailsOrderDetails.visibility = View.GONE
        }
        binding.tvMobileNumberOrderDetails.text = orderDetails.address.mobileNumber

        binding.tvSubtotalAmountOrderDetails.text = orderDetails.subtotal_amount
        binding.tvShippingChargeAmountOrderDetails.text = orderDetails.shipping_charge
        binding.totalAmountOfOrder.text = orderDetails.total_amount

    }
}