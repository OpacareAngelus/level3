package adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.R
import com.google.android.material.snackbar.Snackbar
import extension.addImage
import model.User
import model.UsersViewModel


open class RecyclerAdapter(userList: UsersViewModel) :
    RecyclerView.Adapter<RecyclerAdapter.MyViewHolder>(),
    View.OnClickListener {

    var userList = userList

    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameField: TextView = itemView.findViewById(R.id.tv_name)
        val careerField: TextView = itemView.findViewById(R.id.tv_career)
        val userPhoto: ImageView = itemView.findViewById(R.id.iv_user_photo)
        val deleteBtn: ImageButton = itemView.findViewById(R.id.img_btn_trash_can)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.nameField.text = userList.getUser(position).name
        holder.careerField.text = userList.getUser(position).career
        holder.userPhoto.addImage(userList.getUser(position))
        userList.getUser(position).id = holder.adapterPosition
        holder.deleteBtn.tag = userList.getUser(position)
        holder.deleteBtn.setOnClickListener(this)
    }

    override fun getItemCount() = userList.size()

    override fun onClick(v: View?) {
        val user = v?.tag as User
        when (v.id) {
            R.id.img_btn_trash_can -> {
                deleteUser(user, v)
            }
        }
    }

    private fun deleteUser(user: User, v: View) {
        val delMessage = Snackbar.make(v, "${user.name} has deleted.", Snackbar.LENGTH_LONG)
        userList.delete(user.id)
        notifyItemRemoved(user.id)
        notifyItemRangeChanged(user.id, userList.size())
        backUser(user, delMessage)
        delMessage.show()
    }

    /**Method back to list of contacts deleted contact if user push "Cancel" on the Snackbar.*/
    private fun backUser(user: User, delMessage: Snackbar) {
        delMessage.setAction("Cancel", View.OnClickListener() {
            userList.add(user.id, user)
            notifyItemRangeChanged(user.id, userList.size())
        })
    }
}


