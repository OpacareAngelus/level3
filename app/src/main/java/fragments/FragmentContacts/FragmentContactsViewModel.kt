package fragments.FragmentContacts

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.UserData
import data.model.User

class FragmentContactsViewModel : ViewModel() {

    private val _userListLiveData = MutableLiveData<List<User>>()
    val userListLiveData: LiveData<List<User>> = _userListLiveData

    init {
        _userListLiveData.value = UserData.getUsers()
    }

    fun getUser(position: Int) = _userListLiveData.value?.get(position)

    fun size() = _userListLiveData.value?.size

    fun deleteUser(user: User) {
        _userListLiveData.value = _userListLiveData.value?.minus(user) ?: listOf()
    }

    fun add(user: User?) {
        if (user != null) _userListLiveData.value =
            _userListLiveData.value?.plus(user) ?: listOf(user)
    }
}