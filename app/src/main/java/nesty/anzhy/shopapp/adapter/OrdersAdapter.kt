package nesty.anzhy.shopapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.shopapp.databinding.ItemDashboardLayoutBinding
import nesty.anzhy.shopapp.databinding.ItemListProductBinding
import nesty.anzhy.shopapp.models.Order
import nesty.anzhy.shopapp.ui.activities.OrderDetailsActivity
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader

class OrdersAdapter(
    private val context:Context,
    private var orders:ArrayList<Order>,
): RecyclerView.Adapter<OrdersAdapter.VH>() {
    class VH(val binding: ItemListProductBinding): RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemListProductBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val order = orders[position]
        GlideLoader(context).loadProductPicture(order.image,
        holder.binding.itemImageProduct)

        holder.binding.itemProductName.text = order.title
        holder.binding.itemProductPrice.text = "${order.total_amount}"

        holder.binding.deleteProduct.visibility = View.GONE

        holder.itemView.setOnClickListener{
            val intent = Intent(context, OrderDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_MY_ORDER_DETAILS, order)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int = orders.size

}