import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import org.json.JSONArray

class AuthModel : ViewModel() {
    private val _isLogin = MutableLiveData<Boolean>(false)
    val isLogin: LiveData<Boolean> = _isLogin

    private val _user = MutableLiveData<Map<String, Any>>(emptyMap())
    val user: LiveData<Map<String, Any>> = _user

    private val _appointment = MutableLiveData<Map<String, Any>>(emptyMap())
    val appointment: LiveData<Map<String, Any>> = _appointment

    private val _fav = MutableLiveData<List<Any>>(emptyList())
    val fav: LiveData<List<Any>> = _fav

    private val _favDoc = MutableLiveData<List<Map<String, Any>>>(emptyList())
    val favDoc: LiveData<List<Map<String, Any>>> = _favDoc

    fun setFavList(list: List<Any>) {
        _fav.value = list
    }

    fun updateFavDoc() {
        val newFavDoc = mutableListOf<Map<String, Any>>()
        _fav.value?.forEach { num ->
            _user.value?.get("doctor")?.let { doctors ->
                if (doctors is List<*>) {
                    doctors.forEach { doc ->
                        if (doc is Map<*, *> && num == doc["doc_id"]) {
                            @Suppress("UNCHECKED_CAST")
                            newFavDoc.add(doc as Map<String, Any>)
                        }
                    }
                }
            }
        }
        _favDoc.value = newFavDoc
    }

    fun loginSuccess(userData: Map<String, Any>, appointmentInfo: Map<String, Any>) {
        _isLogin.value = true
        _user.value = userData
        _appointment.value = appointmentInfo
        (userData["details"] as? Map<*, *>)?.let { details ->
            details["fav"]?.let { fav ->
                if (fav is String) {
                    _fav.value = JSONArray(fav).toList()
                }
            }
        }
    }
}

fun JSONArray.toList(): List<Any> {
    val list = mutableListOf<Any>()
    for (i in 0 until length()) {
        list.add(get(i))
    }
    return list
}

