package fragments

import adapter.RecyclerAdapter
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.R
import com.example.level3.databinding.AddContactBinding
import com.example.level3.databinding.MyContactsBinding
import com.google.android.material.snackbar.Snackbar
import model.User
import model.UsersViewModel

class FragmentContacts : Fragment() {
    private lateinit var adapter: RecyclerAdapter
    private lateinit var binding: MyContactsBinding
    private lateinit var dialogBinding: AddContactBinding
    var userList: UsersViewModel = UsersViewModel()

    private lateinit var activityResultLauncher: ActivityResultLauncher<Intent>
    private var contactPhoto: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data: Intent? = result.data
                    contactPhoto = data?.data!!
                    dialogBinding.ivUserPhoto.setImageURI(data.data!!)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        //Binding
        binding = MyContactsBinding.inflate(layoutInflater)
        //Create RecyclerView
        val recyclerView: RecyclerView = binding.rvContacts
        recyclerView.adapter = RecyclerAdapter(userList)
        //ItemTouch
        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView)
        //Listener
        binding.tvAddContact.setOnClickListener {
            dialogCreate(inflater)
        }
        return binding.root
    }

    private fun dialogCreate(inflater: LayoutInflater) {
        val dialog = Dialog(inflater.context)
        dialogBinding = AddContactBinding.inflate(inflater)
        dialog.setCancelable(false)
        dialog.setContentView(dialogBinding.root)
        setDialogListeners(dialog)
        dialog.show()
    }

    private fun setDialogListeners(dialog: Dialog) {
        dialogBinding.imgBtnBackArrow.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnSaveContact.setOnClickListener {
            saveButtonListener(dialog)
        }
        dialogBinding.ivAddPhoto.setOnClickListener() {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            activityResultLauncher.launch(intent)
        }
    }

private fun saveButtonListener(dialog: Dialog) {
    if (dialogBinding.tietUsername.text.toString() != "" &&
        dialogBinding.tietCareer.text.toString() != "" &&
        dialogBinding.tietEmail.text.toString() != "" &&
        dialogBinding.tietPhone.text.toString() != "" &&
        dialogBinding.tietAddress.text.toString() != "" &&
        dialogBinding.tietDataOfBirth.text.toString() != ""
    ) {
        saveButtonAction(dialog)
    } else {
        Toast.makeText(
            dialog.context,
            getString(R.string.error_wrong_user_data),
            Toast.LENGTH_LONG
        ).show()
    }
}

private fun saveButtonAction(dialog: Dialog) {
    var user = User(
        0,
        contactPhoto.toString(),
        dialogBinding.tietUsername.text.toString(),
        dialogBinding.tietCareer.text.toString(),
        dialogBinding.tietEmail.text.toString(),
        dialogBinding.tietPhone.text.toString(),
        dialogBinding.tietAddress.text.toString(),
        dialogBinding.tietDataOfBirth.text.toString()
    )
    userList.add(userList.size(), user)
    binding.rvContacts.adapter?.notifyItemRangeChanged(0, userList.size())
    dialog.dismiss()
}

private var simpleCallback =
    object : ItemTouchHelper.SimpleCallback(
        0, ItemTouchHelper.LEFT
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
                    val user = userList.getUser(viewHolder.adapterPosition)
                    val delMessage = Snackbar.make(
                        viewHolder.itemView,
                        "${user.name} has deleted.",
                        Snackbar.LENGTH_LONG
                    )
                    userList.delete(viewHolder.adapterPosition)
                    binding.rvContacts.adapter?.notifyItemRemoved(viewHolder.adapterPosition)
                    binding.rvContacts.adapter?.notifyItemRangeChanged(
                        viewHolder.adapterPosition,
                        binding.rvContacts.adapter!!.itemCount
                    )
                    backUser(user, delMessage, viewHolder.adapterPosition)
                    delMessage.show()
                }
            }
        }
    }

/**Method back to list of contacts deleted contact if user push "Cancel" on the Snackbar.*/
private fun backUser(user: User, delMessage: Snackbar, position: Int) {
    delMessage.setAction("Cancel", View.OnClickListener() {
        userList.add(user.id, user)
        binding.rvContacts.adapter?.notifyItemRangeChanged(
            position,
            binding.rvContacts.adapter!!.itemCount
        )
    })
}
}