package nesty.anzhy.shopapp.utils

import android.content.Context
import android.widget.ImageView
import com.bumptech.glide.Glide
import nesty.anzhy.shopapp.R
import java.io.IOException

class GlideLoader(val context: Context) {
    fun loadUserPicture(image: Any, imageView: ImageView){
        try {
            Glide.with(context)
                .load(image)
                .centerCrop()
                .placeholder(R.drawable.anonymous)
                .into(imageView)
        }
        catch (e: IOException){
            e.printStackTrace()
        }
    }

    fun loadProductPicture(image: Any, imageView: ImageView) {
        try {
            Glide.with(context)
                .load(image)
                .centerCrop()
                .into(imageView)
        }
        catch (e: IOException){
            e.printStackTrace()
        }
    }
}