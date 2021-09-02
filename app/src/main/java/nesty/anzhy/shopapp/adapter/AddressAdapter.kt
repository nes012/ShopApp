package nesty.anzhy.shopapp.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.shopapp.databinding.ItemAddressLayoutBinding
import nesty.anzhy.shopapp.models.Address
import nesty.anzhy.shopapp.ui.activities.AddEditAddressActivity
import nesty.anzhy.shopapp.ui.activities.CheckoutActivity
import nesty.anzhy.shopapp.utils.Constants

class AddressAdapter(
    private val context: Context,
    private val addresses: ArrayList<Address>,
    private val selectAddress: Boolean
    ): RecyclerView.Adapter<AddressAdapter.VH>() {

    class VH(val binding: ItemAddressLayoutBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
       val inflater:LayoutInflater = LayoutInflater.from(parent.context)
        val binding:ItemAddressLayoutBinding = ItemAddressLayoutBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
       val address = addresses[position]
        holder.binding.tvAddressDetails.text="${address.address}, ${address.zipCode}"
        holder.binding.tvAddressFullName.text = address.name
        holder.binding.tvAddressMobileNumber.text = address.mobileNumber
        holder.binding.tvAddressType.text = address.type

        if(selectAddress){
            holder.itemView.setOnClickListener{
                val intent = Intent(context, CheckoutActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, address)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int = addresses.size

    fun notifyEditItem(activity: Activity, position: Int){
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, addresses[position])
        activity.startActivityForResult(intent, Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }
}