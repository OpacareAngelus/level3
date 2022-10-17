package model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import data.UserData

class UsersViewModel : ViewModel() {

    private val _userListLiveData = MutableLiveData<MutableList<User>>()
    val userListLiveData: LiveData<MutableList<User>> = _userListLiveData

    init {
        _userListLiveData.value = UserData.getUsers()
    }

    fun getUser(position: Int) = _userListLiveData.value?.get(position)

    fun size() = _userListLiveData.value?.size

    fun deleteUser(position: Int) = _userListLiveData.value?.removeAt(position)
    fun deleteUser(user: User) = _userListLiveData.value?.remove(user)


    fun add(user: User?) {
        if(user != null) _userListLiveData.value?.add(user)
    }
}