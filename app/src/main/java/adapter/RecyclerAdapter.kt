package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.R
import com.example.level3.databinding.RecyclerviewItemBinding
import com.google.android.material.snackbar.Snackbar
import extension.addImage
import model.User
import model.UsersViewModel


class RecyclerAdapter(
    private val userList: UsersViewModel,
    private val onDeleteUser: (User) -> Unit,
    private val navGraph: NavController
) :
    ListAdapter<User, RecyclerAdapter.MyViewHolder>(object : DiffUtil.ItemCallback<User>() {

        override fun areItemsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: User, newItem: User): Boolean {
            return oldItem == newItem
        }
    }),
    View.OnClickListener {

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        lateinit var binding: RecyclerviewItemBinding

        fun bind() {
            binding = RecyclerviewItemBinding.bind(itemView)
            binding.tvName.text = userList.getUser(adapterPosition)?.name
            binding.tvCareer.text = userList.getUser(adapterPosition)?.career
            userList.getUser(adapterPosition)?.let { binding.ivUserPhoto.addImage(it) }
            binding.imgBtnTrashCan.tag = userList.getUser(adapterPosition)
            userList.getUser(adapterPosition)?.id = adapterPosition

            binding.imgBtnTrashCan.setOnClickListener {
                deleteUser(it?.tag as User, itemView)
            }

            binding.ThisRecyclerView.setOnClickListener {
                navGraph.navigate(
                    R.id.action_fragmentContacts_to_fragmentContactProfile,
                    bundleOf(
                        Pair("photo", userList.getUser(this.adapterPosition)?.photo),
                        Pair("name", userList.getUser(this.adapterPosition)?.name),
                        Pair("career", userList.getUser(this.adapterPosition)?.career),
                        Pair("address", userList.getUser(this.adapterPosition)?.homeAddress)
                    )
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind()
    }

    override fun getItemCount(): Int = userList.size()!!

    override fun onClick(view: View?) {
    }

    private fun deleteUser(user: User, view: View) {
        val delMessage = Snackbar.make(view, "${user.name} has deleted.", Snackbar.LENGTH_LONG)
        onDeleteUser(user)
        notifyItemRemoved(user.id)
        userList.size()?.let { notifyItemRangeChanged(user.id, it) }
        undoUserDeletion(user, delMessage)
        delMessage.show()
    }

    /**Method back to list of contacts deleted contact if user push "Cancel" on the Snackbar.*/
    private fun undoUserDeletion(user: User, delMessage: Snackbar) {
        delMessage.setAction("Cancel") {
            userList.add(user)
            userList.size()?.let { it1 -> notifyItemRangeChanged(user.id, it1) }
        }
    }
}


