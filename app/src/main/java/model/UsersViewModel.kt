package model

import androidx.lifecycle.ViewModel
import data.UserData

class UsersViewModel : ViewModel() {

    private var userList: ArrayList<User> = arrayListOf()

    init {
        userList = UserData.getUsers()
    }

    fun getUser(position: Int) = userList[position]

    fun size() = userList.size

    fun delete(position: Int) = userList.removeAt(position)

    fun add(id: Int, user: User) = userList.add(id, user)
}