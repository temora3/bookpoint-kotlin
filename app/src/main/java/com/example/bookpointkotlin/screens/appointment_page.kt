import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class AppointmentPageActivity : AppCompatActivity() {

    private var status: FilterStatus = FilterStatus.UPCOMING
    private lateinit var schedules: MutableList<JSONObject>
    private lateinit var filteredSchedules: List<JSONObject>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_appointment_page)

        schedules = mutableListOf()
        filteredSchedules = listOf()

        fetchAppointments()
        setupUI()
    }

    private fun fetchAppointments() {
        lifecycleScope.launch {
            val token = getSharedPreferences("user_prefs", MODE_PRIVATE).getString("token", "") ?: ""
            val appointmentResponse = DioProvider.getAppointments(token)

            if (appointmentResponse != "Error") {
                withContext(Dispatchers.Main) {
                    schedules.clear()
                    val jsonArray = JSONArray(appointmentResponse)
                    for (i in 0 until jsonArray.length()) {
                        schedules.add(jsonArray.getJSONObject(i))
                    }
                    filterSchedules()
                }
            }
        }
    }

    private fun setupUI() {
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = AppointmentAdapter(filteredSchedules)

        // Setting up filter tabs
        findViewById<View>(R.id.tabUpcoming).setOnClickListener {
            updateStatus(FilterStatus.UPCOMING)
        }
        findViewById<View>(R.id.tabComplete).setOnClickListener {
            updateStatus(FilterStatus.COMPLETE)
        }
        findViewById<View>(R.id.tabCancel).setOnClickListener {
            updateStatus(FilterStatus.CANCEL)
        }
    }

    private fun updateStatus(newStatus: FilterStatus) {
        if (status != newStatus) {
            status = newStatus
            filterSchedules()
        }
    }

    private fun filterSchedules() {
        filteredSchedules = schedules.filter { schedule ->
            when (schedule.getString("status")) {
                "upcoming" -> FilterStatus.UPCOMING
                "complete" -> FilterStatus.COMPLETE
                "cancel" -> FilterStatus.CANCEL
                else -> null
            } == status
        }
        findViewById<RecyclerView>(R.id.recyclerView).adapter?.notifyDataSetChanged()
    }

    enum class FilterStatus {
        UPCOMING, COMPLETE, CANCEL
    }
}

class AppointmentAdapter(private val appointments: List<JSONObject>) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_appointment, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
    }

    override fun getItemCount(): Int = appointments.size

    class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(schedule: JSONObject) {
            itemView.findViewById<TextView>(R.id.doctorName).text = schedule.getString("doctor_name")
            itemView.findViewById<TextView>(R.id.category).text = schedule.getString("category")
            itemView.findViewById<ImageView>(R.id.doctorImage).setImageURI(
                Uri.parse("http://127.0.0.1:8000${schedule.getString("doctor_profile")}")
            )
            // Setting up schedule details
            itemView.findViewById<TextView>(R.id.date).text = schedule.getString("date")
            itemView.findViewById<TextView>(R.id.day).text = schedule.getString("day")
            itemView.findViewById<TextView>(R.id.time).text = schedule.getString("time")
        }
    }
}

object DioProvider {
    suspend fun getAppointments(token: String): String {
        // Simulate API call with a placeholder string
        return withContext(Dispatchers.IO) {
            try {
                // Simulate successful response
                "[{\"doctor_name\": \"Dr. Smith\", \"status\": \"upcoming\", \"category\": \"General\", \"doctor_profile\": \"/path/to/image\", \"date\": \"2024-11-27\", \"day\": \"Monday\", \"time\": \"10:00 AM\"}]"
            } catch (e: Exception) {
                "Error"
            }
        }
    }
}
