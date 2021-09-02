package nesty.anzhy.shopapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val user_id: String = "",
    val name: String = "",
    val mobileNumber: String = "",

    val address: String = "",
    val zipCode: String = "",
    val additionalNote: String = "",

    //home, office, other
    val type: String = "",
    val otherDetails: String = "",
    var id: String = "",
) : Parcelable