package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.R
import com.example.level3.databinding.RecyclerviewItemBinding
import com.google.android.material.snackbar.Snackbar
import extension.addImage
import data.model.User
import util.UserListController
import util.UserContactsDiffUtil


class RecyclerAdapterUserContacts(
    private val userListController: UserListController
) :
    ListAdapter<User, RecyclerAdapterUserContacts.ViewHolder>(UserContactsDiffUtil) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(position)
    }

    private fun deleteUser(user: User, view: View) {
        userListController.onDeleteUser(user)
        val delMessage = Snackbar.make(view, "${user.name} has deleted.", Snackbar.LENGTH_LONG)
        undoUserDeletion(user, delMessage)
        delMessage.show()
    }

    /**Method back to list of contacts deleted contact if user push "Cancel" on the Snackbar.*/
    private fun undoUserDeletion(user: User, delMessage: Snackbar) {
        delMessage.setAction(R.string.cancel) {
            userListController.onContactAdd(user)
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(position: Int) {
            RecyclerviewItemBinding.bind(itemView).run {
                val user = currentList[position]
                tvName.text = user.name
                tvCareer.text = user.career
                ivRecyclerItemUserPhoto.addImage(user.photo)
                user.id = position

                btnTrashCan.setOnClickListener {
                    deleteUser(user, itemView)
                }
                itemContactsRecyclerView.setOnClickListener {
                    userListController.onOpenContactProfile(user)
                }
            }
        }
    }
}


