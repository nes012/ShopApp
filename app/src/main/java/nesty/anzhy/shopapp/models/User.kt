package nesty.anzhy.shopapp.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize


//это будет наша дата класс
@Parcelize
class User(
    val id: String = "",
    val firstName: String = "",
    val lastName: String = "",
    val email: String = "",
    val image: String = "",
    val mobile: Long = 0,
    val gender: String = "",
    val profileCompleted: Int = 0
) : Parcelable

