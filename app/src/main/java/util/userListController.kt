package util

import model.User

interface UserListController {
    fun onDeleteUser(user: User)
    fun onDeleteUser(position: Int)
    fun onContactAdd(user: User)
}