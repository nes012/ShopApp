package nesty.anzhy.shopapp.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import nesty.anzhy.shopapp.R
import nesty.anzhy.shopapp.databinding.ItemCartLayoutBinding
import nesty.anzhy.shopapp.firestore.Firestore
import nesty.anzhy.shopapp.models.Cart
import nesty.anzhy.shopapp.ui.activities.CartListActivity
import nesty.anzhy.shopapp.utils.Constants
import nesty.anzhy.shopapp.utils.GlideLoader

class CartAdapter(
    private val context: Context,
    private val cartList: ArrayList<Cart>,
    private val updateCartItem: Boolean
) : RecyclerView.Adapter<CartAdapter.VH>() {

    class VH(val binding: ItemCartLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemCartLayoutBinding.inflate(inflater, parent, false)
        return VH(binding)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val cartItem = cartList[position]

        GlideLoader(context).loadProductPicture(cartItem.image, holder.binding.ivCartItemImage)

        if (cartItem.cart_quantity == "0") {
            holder.binding.btnAddItemToCart.visibility = View.GONE
            holder.binding.removeOneItemFromCart.visibility = View.GONE

            if(updateCartItem){
                holder.binding.btnDeleteItemFromCart.visibility = View.VISIBLE
            }
            else{
                holder.binding.btnDeleteItemFromCart.visibility = View.GONE
            }

            holder.binding.tvCartQuantity.text = "out of stock"
            holder.binding.tvCartQuantity.setTextColor(
                ContextCompat.getColor(context, R.color.colorAccent)
            )
        } else {
            if (updateCartItem) {
                holder.binding.removeOneItemFromCart.visibility = View.VISIBLE
                holder.binding.btnAddItemToCart.visibility = View.VISIBLE
                holder.binding.btnDeleteItemFromCart.visibility = View.VISIBLE
            } else {
                holder.binding.removeOneItemFromCart.visibility = View.GONE
                holder.binding.btnAddItemToCart.visibility = View.GONE
                holder.binding.btnDeleteItemFromCart.visibility = View.GONE
            }

            holder.binding.tvCartItemPrice.text = "$${cartItem.price}"
            holder.binding.tvCartItemTitle.text = cartItem.title
            holder.binding.tvCartQuantity.text = cartItem.cart_quantity
        }

        holder.binding.btnDeleteItemFromCart.setOnClickListener {
            Firestore().removeItemFromCart(context, cartItem.id)
        }

        holder.binding.btnAddItemToCart.setOnClickListener {
            val cartQuantity: Int = cartItem.cart_quantity.toInt()
            if (cartQuantity < cartItem.stock_quantity.toInt()) {
                val itemHashMap = HashMap<String, Any>()
                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                Firestore().updateMyCart(context, cartItem.id, itemHashMap)
            } else {
                if (context is CartListActivity) {
                    Toast.makeText(
                        context,
                        "you can not add more than stock quantity",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
        holder.binding.removeOneItemFromCart.setOnClickListener {
            if (cartItem.cart_quantity == "1") {
                Firestore().removeItemFromCart(context, cartItem.id)
            } else {
                val cartQuantity: Int = cartItem.cart_quantity.toInt()
                val itemHashMap = HashMap<String, Any>()
                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()
                Firestore().updateMyCart(context, cartItem.id, itemHashMap)
            }
        }
    }

    override fun getItemCount(): Int = cartList.size

}