package nesty.anzhy.shopapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.shopapp.databinding.ItemListProductBinding
import nesty.anzhy.shopapp.models.SoldProduct
import nesty.anzhy.shopapp.ui.activities.SoldProductDetailsActivity
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader

class SoldProductsAdapter(
    private val context: Context,
    private val soldProductsList:ArrayList<SoldProduct>
):RecyclerView.Adapter<SoldProductsAdapter.VH>() {
    class VH(val binding: ItemListProductBinding):RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val binding: ItemListProductBinding = ItemListProductBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val soldProduct = soldProductsList[position]
        holder.binding.deleteProduct.visibility = View.GONE
        holder.binding.itemProductName.text = soldProduct.title
        holder.binding.itemProductPrice.text = soldProduct.price
        GlideLoader(context).loadProductPicture(soldProduct.image, holder.binding.itemImageProduct)


        holder.itemView.setOnClickListener{
            val intent = Intent(context, SoldProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS, soldProduct)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int = soldProductsList.size
}