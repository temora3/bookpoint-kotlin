import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject

class DoctorDetailsActivity : AppCompatActivity() {

    private lateinit var doctor: Map<String, Any>
    private var isFav: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_doctor_details)

        doctor = intent.getSerializableExtra("doctor") as Map<String, Any>
        isFav = intent.getBooleanExtra("isFav", false)

        setupUI()
    }

    private fun setupUI() {
        val doctorInfoView = findViewById<RecyclerView>(R.id.doctorInfoRecyclerView)
        val favButton = findViewById<ImageButton>(R.id.favButton)
        val bookAppointmentButton = findViewById<Button>(R.id.bookAppointmentButton)

        updateFavoriteIcon(favButton)

        favButton.setOnClickListener {
            toggleFavoriteStatus(favButton)
        }

        doctorInfoView.layoutManager = LinearLayoutManager(this)
        doctorInfoView.adapter = DoctorInfoAdapter(doctor)

        bookAppointmentButton.setOnClickListener {
            val intent = Intent(this, BookingActivity::class.java)
            intent.putExtra("doctor_id", doctor["doc_id"] as Int)
            startActivity(intent)
        }
    }

    private fun toggleFavoriteStatus(favButton: ImageButton) {
        lifecycleScope.launch {
            val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            val token = sharedPreferences.getString("token", null)

            if (token.isNullOrEmpty()) {
                showToast("Please log in to update your favorites.")
                return@launch
            }

            val favoriteList = withContext(Dispatchers.IO) {
                val prefsList = sharedPreferences.getStringSet("favorites", mutableSetOf())
                prefsList?.toMutableSet() ?: mutableSetOf()
            }

            if (favoriteList.contains(doctor["doc_id"].toString())) {
                favoriteList.remove(doctor["doc_id"].toString())
            } else {
                favoriteList.add(doctor["doc_id"].toString())
            }

            withContext(Dispatchers.IO) {
                sharedPreferences.edit().putStringSet("favorites", favoriteList).apply()
            }

            updateFavoriteIcon(favButton)
        }
    }

    private fun updateFavoriteIcon(favButton: ImageButton) {
        favButton.setImageResource(
            if (isFav) R.drawable.ic_favorite_filled else R.drawable.ic_favorite_border
        )
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

class DoctorInfoAdapter(private val doctor: Map<String, Any>) : RecyclerView.Adapter<DoctorInfoViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DoctorInfoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_doctor_info, parent, false)
        return DoctorInfoViewHolder(view)
    }

    override fun onBindViewHolder(holder: DoctorInfoViewHolder, position: Int) {
        when (position) {
            0 -> holder.bind("Doctor Name", "Dr. ${doctor["doctor_name"]}")
            1 -> holder.bind("Specialty", doctor["category"] as String)
            2 -> holder.bind("Experience", "${doctor["experience"]} years")
            3 -> holder.bind("Patients Treated", doctor["patients"].toString())
            4 -> holder.bind("Hospital", "Sarawak General Hospital")
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}

class DoctorInfoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val labelView: TextView = itemView.findViewById(R.id.infoLabel)
    private val valueView: TextView = itemView.findViewById(R.id.infoValue)

    fun bind(label: String, value: String) {
        labelView.text = label
        valueView.text = value
    }
}
