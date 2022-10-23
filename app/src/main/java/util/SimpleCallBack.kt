
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.R
import com.google.android.material.snackbar.Snackbar
import model.User
import model.UsersViewModel
import util.UserListController

class SimpleCallBack(
    val viewModel: UsersViewModel,
    private val navGraph: NavController,
    val userListController: UserListController
) {

    var simpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val user = viewModel.getUser(viewHolder.absoluteAdapterPosition)
                        val delMessage = Snackbar.make(viewHolder.itemView,
                            "${user?.name} has deleted.",
                            Snackbar.LENGTH_LONG
                        )
                        userListController.onDeleteUser(viewHolder.absoluteAdapterPosition)
                        user?.let { undoUserDeletion(it, delMessage) }
                        delMessage.show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        openContactDetail(viewHolder)
                    }
                }
            }
        }

    /**Method back to list of contacts deleted contact if user push "Cancel" on the Snackbar.*/
    private fun undoUserDeletion(user: User, delMessage: Snackbar) {
        delMessage.setAction(R.string.cancel) {
            userListController.onContactAdd(user)
        }
    }

    fun openContactDetail(viewHolder: RecyclerView.ViewHolder) {
        navGraph.navigate(
            R.id.action_fragmentContacts_to_fragmentContactProfile,
            bundleOf(
                Pair("photo", viewModel.getUser(viewHolder.absoluteAdapterPosition)?.photo),
                Pair("name", viewModel.getUser(viewHolder.absoluteAdapterPosition)?.name),
                Pair("career", viewModel.getUser(viewHolder.absoluteAdapterPosition)?.career),
                Pair("address", viewModel.getUser(viewHolder.absoluteAdapterPosition)?.homeAddress)
            )
        )
    }
}