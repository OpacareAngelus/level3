package util

import data.model.User

interface UserListController {
    fun onDeleteUser(user: User)
    fun onContactAdd(user: User)
    fun onOpenContactProfile(user: User)
}