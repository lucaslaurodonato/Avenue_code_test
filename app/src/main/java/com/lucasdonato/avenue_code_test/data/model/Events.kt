import com.google.gson.annotations.SerializedName
import java.io.Serializable

data class Events(
    @SerializedName("people") val people : List<String>,
    @SerializedName("date") val date : Long,
    @SerializedName("description") val description : String,
    @SerializedName("image") val image : String,
    @SerializedName("longitude") val longitude : Double,
    @SerializedName("latitude") val latitude : Double,
    @SerializedName("price") val price : Double,
    @SerializedName("title") val title : String,
    @SerializedName("id") val id : Int
) : Serializable