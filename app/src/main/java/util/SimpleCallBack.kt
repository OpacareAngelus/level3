import adapter.RecyclerAdapterUserContacts
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.R
import com.google.android.material.snackbar.Snackbar
import model.User
import model.UsersViewModel

class SimpleCallBack(
    var viewModel: UsersViewModel,
    var usersAdapter: RecyclerAdapterUserContacts,
    var navGraph: NavController
) {

    //Swipe here

    var simpleCallback =
        object : ItemTouchHelper.SimpleCallback(
            0, ItemTouchHelper.LEFT.or(ItemTouchHelper.RIGHT)
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                TODO("Not yet implemented")
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                when (direction) {
                    ItemTouchHelper.LEFT -> {
                        val user = viewModel.getUser(viewHolder.adapterPosition)
                        val delMessage = Snackbar.make(
                            viewHolder.itemView,
                            "${user?.name} has deleted.",
                            Snackbar.LENGTH_LONG
                        )
                        usersAdapter.submitList(viewModel.userListLiveData.value.also {
                            viewModel.userListLiveData.value?.removeAt(
                                viewHolder.adapterPosition
                            )
                        }?.toMutableList())
                        user?.let { undoUserDeletion(it, delMessage, viewHolder.adapterPosition) }
                        delMessage.show()
                    }
                    ItemTouchHelper.RIGHT -> {
                        openContactDetail(viewHolder)
                    }
                }
            }
        }

    /**Method back to list of contacts deleted contact if user push "Cancel" on the Snackbar.*/
    private fun undoUserDeletion(user: User, delMessage: Snackbar, position: Int) {
        delMessage.setAction(R.string.cancel) {
            usersAdapter.submitList(viewModel.userListLiveData.value.also {
                viewModel.userListLiveData.value?.add(
                    user!!
                )
            }?.toMutableList())
        }
    }

    fun openContactDetail(viewHolder: RecyclerView.ViewHolder) {
        navGraph.navigate(
            R.id.action_fragmentContacts_to_fragmentContactProfile,
            bundleOf(
                Pair("photo", viewModel.getUser(viewHolder.adapterPosition)?.photo),
                Pair("name", viewModel.getUser(viewHolder.adapterPosition)?.name),
                Pair("career", viewModel.getUser(viewHolder.adapterPosition)?.career),
                Pair("address", viewModel.getUser(viewHolder.adapterPosition)?.homeAddress)
            )
        )
    }
}