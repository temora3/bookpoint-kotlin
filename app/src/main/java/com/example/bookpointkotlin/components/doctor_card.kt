import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.bookpoint.R
import com.example.bookpoint.utils.Config

class DoctorCard(
    private val context: Context,
    private val doctor: Map<String, Any>,
    private val isFav: Boolean
) : RecyclerView.ViewHolder(
    LayoutInflater.from(context).inflate(R.layout.doctor_card, null)
) {

    private val cardView: CardView = itemView.findViewById(R.id.cardView)
    private val doctorImage: ImageView = itemView.findViewById(R.id.doctorImage)
    private val doctorName: TextView = itemView.findViewById(R.id.doctorName)
    private val doctorCategory: TextView = itemView.findViewById(R.id.doctorCategory)
    private val ratingIcon: ImageView = itemView.findViewById(R.id.ratingIcon)
    private val ratingText: TextView = itemView.findViewById(R.id.ratingText)
    private val reviewsText: TextView = itemView.findViewById(R.id.reviewsText)
    private val reviewCountText: TextView = itemView.findViewById(R.id.reviewCountText)

    init {
        Config.init(context)
        
        cardView.setOnClickListener {
            // Navigate to DoctorDetails
            val intent = Intent(context, DoctorDetails::class.java).apply {
                putExtra("doctor", doctor as Serializable)
                putExtra("isFav", isFav)
            }
            context.startActivity(intent)
        }

        bind()
    }

    private fun bind() {
        Glide.with(context)
            .load("http://127.0.0.1:8000${doctor["doctor_profile"]}")
            .fitCenter()
            .into(doctorImage)

        doctorName.text = "Dr ${doctor["doctor_name"]}"
        doctorCategory.text = doctor["category"].toString()

        ratingIcon.setImageResource(R.drawable.ic_star_border)
        ratingText.text = "4.5"
        reviewsText.text = "Reviews"
        reviewCountText.text = "(20)"
    }
}

