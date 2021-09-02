package nesty.anzhy.shopapp.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.shopapp.models.Product
import nesty.anzhy.shopapp.databinding.ItemListProductBinding
import nesty.anzhy.shopapp.ui.activities.ProductDetailsActivity
import nesty.anzhy.shopapp.ui.fragments.ProductsFragment
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader

class ProductAdapter(
    private val context: Context,
    private val products: List<Product>,
    private val fragment: ProductsFragment
    ): RecyclerView.Adapter<ProductAdapter.VH>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding =  ItemListProductBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun getItemCount(): Int = products.size


    class VH(val binding: ItemListProductBinding):RecyclerView.ViewHolder(binding.root)

    override fun onBindViewHolder(holder: VH, position: Int) {
        val product = products[position]

        GlideLoader(context).loadProductPicture(product.image,
        holder.binding.itemImageProduct)

        holder.binding.itemProductName.text = product.user_name
        holder.binding.itemProductPrice.text = "$${product.price}"
        holder.binding.itemProductDescription.text = product.title

        holder.binding.deleteProduct.setOnClickListener{
            fragment.deleteProduct(product.product_id)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID, product.product_id)
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID, product.user_id)
            context.startActivity(intent)
        }
    }
}