import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.coroutines.launch

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        setupUI()
    }

    private fun setupUI() {
        val toolbar = findViewById<MaterialToolbar>(R.id.toolbar)
        val searchInput = findViewById<TextInputEditText>(R.id.searchInput)
        val searchLayout = findViewById<TextInputLayout>(R.id.searchLayout)
        val categoriesRecyclerView = findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        val scheduleRecyclerView = findViewById<RecyclerView>(R.id.scheduleRecyclerView)
        val nearbyDoctorsRecyclerView = findViewById<RecyclerView>(R.id.nearbyDoctorsRecyclerView)

        setSupportActionBar(toolbar)

        setupCategories(categoriesRecyclerView)
        setupSchedule(scheduleRecyclerView)
        setupNearbyDoctors(nearbyDoctorsRecyclerView)

        searchLayout.setEndIconOnClickListener {
            val query = searchInput.text.toString()
            if (query.isNotEmpty()) {
                Toast.makeText(this, "Searching for: $query", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Please enter a search query", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupCategories(recyclerView: RecyclerView) {
        val categories = listOf("Cardiology", "Dentistry", "Neurology", "Pediatrics", "Dermatology")
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = CategoriesAdapter(categories)
    }

    private fun setupSchedule(recyclerView: RecyclerView) {
        val appointments = listOf("Appointment 1", "Appointment 2")
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = ScheduleAdapter(appointments)
    }

    private fun setupNearbyDoctors(recyclerView: RecyclerView) {
        val doctors = listOf(
            Doctor("Dr. Jane Doe", "Cardiology"),
            Doctor("Dr. John Smith", "Dermatology")
        )
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = DoctorsAdapter(doctors)
    }
}