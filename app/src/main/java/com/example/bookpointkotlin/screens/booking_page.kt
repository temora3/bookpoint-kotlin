import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.button.MaterialButton
import com.google.android.material.textview.MaterialTextView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Calendar

class BookingActivity : AppCompatActivity() {

    private var selectedDate: Calendar = Calendar.getInstance()
    private var selectedTimeIndex: Int? = null
    private var isWeekend: Boolean = false
    private var token: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_booking)

        initToken()
        setupUI()
    }

    private fun initToken() {
        lifecycleScope.launch {
            val sharedPreferences: SharedPreferences = getSharedPreferences("user_prefs", MODE_PRIVATE)
            token = sharedPreferences.getString("token", null)
        }
    }

    private fun setupUI() {
        val calendarView = findViewById<CalendarView>(R.id.calendarView)
        val timeSlotsContainer = findViewById<RecyclerView>(R.id.timeSlotsRecyclerView)
        val appointmentButton = findViewById<MaterialButton>(R.id.appointmentButton)

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            selectedDate.set(year, month, dayOfMonth)
            isWeekend = checkIfWeekend(selectedDate)
            if (isWeekend) {
                selectedTimeIndex = null
                showToast("Weekend is not available. Please select another date.")
            }
            updateTimeSlots(timeSlotsContainer)
        }

        appointmentButton.setOnClickListener {
            if (selectedTimeIndex == null || isWeekend) {
                showToast("Please select a valid date and time.")
            } else {
                bookAppointment()
            }
        }
    }

    private fun checkIfWeekend(calendar: Calendar): Boolean {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY
    }

    private fun updateTimeSlots(recyclerView: RecyclerView) {
        val timeSlots = generateTimeSlots()
        val adapter = TimeSlotAdapter(timeSlots) { index ->
            selectedTimeIndex = index
        }
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun generateTimeSlots(): List<String> {
        return List(8) { index -> "${index + 9}:00 ${if (index + 9 > 11) "PM" else "AM"}" }
    }

    private fun bookAppointment() {
        lifecycleScope.launch {
            val date = selectedDate.time.toString()
            val time = generateTimeSlots()[selectedTimeIndex!!]
            val doctorId = intent.getIntExtra("doctor_id", -1)
            if (doctorId == -1 || token == null) {
                showToast("Invalid booking data.")
                return@launch
            }

            val success = withContext(Dispatchers.IO) {
                // Simulate booking process, replace with API call logic
                true
            }

            if (success) {
                navigateToSuccessScreen()
            } else {
                showToast("Failed to book appointment. Try again later.")
            }
        }
    }

    private fun navigateToSuccessScreen() {
        val intent = Intent(this, SuccessBookingActivity::class.java)
        startActivity(intent)
        finish()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
