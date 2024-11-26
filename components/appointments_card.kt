import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AppointmentCard : Fragment() {
    private lateinit var doctor: Map<String, Any>
    private lateinit var color: Int

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.appointment_card, container, false)

        val cardView = view.findViewById<MaterialCardView>(R.id.appointmentCard)
        cardView.setCardBackgroundColor(color)

        val doctorImage = view.findViewById<ImageView>(R.id.doctorImage)
        Glide.with(this)
            .load("http://127.0.0.1:8000${doctor["doctor_profile"]}")
            .into(doctorImage)

        view.findViewById<TextView>(R.id.doctorName).text = "Dr ${doctor["doctor_name"]}"
        view.findViewById<TextView>(R.id.doctorCategory).text = doctor["category"] as String

        val scheduleCard = view.findViewById<ScheduleCard>(R.id.scheduleCard)
        scheduleCard.setAppointment(doctor["appointments"] as Map<String, Any>)

        view.findViewById<Button>(R.id.cancelButton).setOnClickListener {
            // Handle cancel action
        }

        view.findViewById<Button>(R.id.completeButton).setOnClickListener {
            showRatingDialog()
        }

        return view
    }

    private fun showRatingDialog() {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle("Rate the Doctor")
        builder.setMessage("Please help us to rate our Doctor")
        
        val dialogView = layoutInflater.inflate(R.layout.rating_dialog, null)
        builder.setView(dialogView)

        builder.setPositiveButton("Submit") { _, _ ->
            val ratingBar = dialogView.findViewById<RatingBar>(R.id.ratingBar)
            val commentEditText = dialogView.findViewById<EditText>(R.id.commentEditText)
            
            GlobalScope.launch(Dispatchers.IO) {
                val prefs = PreferenceManager.getDefaultSharedPreferences(requireContext())
                val token = prefs.getString("token", "") ?: ""

                val rating = DioProvider().storeReviews(
                    commentEditText.text.toString(),
                    ratingBar.rating,
                    (doctor["appointments"] as Map<String, Any>)["id"] as Int,
                    doctor["doc_id"] as Int,
                    token
                )

                if (rating == 200) {
                    activity?.runOnUiThread {
                        // Navigate to main screen
                        // You might want to use Navigation component or your preferred navigation method
                    }
                }
            }
        }

        builder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }

    companion object {
        fun newInstance(doctor: Map<String, Any>, color: Int): AppointmentCard {
            val fragment = AppointmentCard()
            fragment.doctor = doctor
            fragment.color = color
            return fragment
        }
    }
}

class ScheduleCard(context: Context, attrs: AttributeSet) : MaterialCardView(context, attrs) {
    private lateinit var appointment: Map<String, Any>

    init {
        inflate(context, R.layout.schedule_card, this)
    }

    fun setAppointment(appointment: Map<String, Any>) {
        this.appointment = appointment
        findViewById<TextView>(R.id.dateText).text = "${appointment["day"]}, ${appointment["date"]}"
        findViewById<TextView>(R.id.timeText).text = appointment["time"] as String
    }
}

interface DioProvider {
    @POST("reviews")
    suspend fun storeReviews(
        @Field("comment") comment: String,
        @Field("rating") rating: Float,
        @Field("appointment_id") appointmentId: Int,
        @Field("doctor_id") doctorId: Int,
        @Header("Authorization") token: String
    ): Int
}

object RetrofitClient {
    private const val BASE_URL = "http://your-api-base-url/"

    fun getClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

