package fragments

import activity.MainActivity
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
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.level3.R
import com.example.level3.databinding.AddContactBinding
import com.example.level3.databinding.MyContactsBinding
import com.google.android.material.snackbar.Snackbar
import model.User
import model.UsersViewModel

class FragmentContacts : Fragment() {

    private lateinit var binding: MyContactsBinding
    private lateinit var dialogBinding: AddContactBinding
    var viewmodel: UsersViewModel = MainActivity().userList

    private val usersAdapter by lazy {
        RecyclerAdapter(
            viewmodel,
            onDeleteUser = { user ->
                viewmodel.userListLiveData.value?.remove(user)
            }
        )
    }

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
        binding = MyContactsBinding.inflate(layoutInflater)

        binding.tvAddContact.setOnClickListener {
            dialogCreate(inflater)
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val recyclerView: RecyclerView = binding.rvContacts
        recyclerView.adapter = usersAdapter
        ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView)

    }

    fun openContactDetail(viewHolder: RecyclerView.ViewHolder) {
        findNavController()
            .navigate(
                R.id.action_fragmentContacts_to_fragmentContactProfile,
                bundleOf(
                    Pair("photo", viewmodel.getUser(viewHolder.adapterPosition)?.photo),
                    Pair("name", viewmodel.getUser(viewHolder.adapterPosition)?.name),
                    Pair("career", viewmodel.getUser(viewHolder.adapterPosition)?.career),
                    Pair("address", viewmodel.getUser(viewHolder.adapterPosition)?.homeAddress)
                )
            )
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
        viewmodel.userListLiveData.value?.add(
            User(
                0,
                contactPhoto.toString(),
                dialogBinding.tietUsername.text.toString(),
                dialogBinding.tietCareer.text.toString(),
                dialogBinding.tietEmail.text.toString(),
                dialogBinding.tietPhone.text.toString(),
                dialogBinding.tietAddress.text.toString(),
                dialogBinding.tietDataOfBirth.text.toString()
            )
        )
        usersAdapter.itemCount.let { usersAdapter.notifyItemRangeChanged(0, it) }
        dialog.dismiss()
    }

    private var simpleCallback =
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
                        val user = viewmodel.getUser(viewHolder.adapterPosition)
                        val delMessage = Snackbar.make(
                            viewHolder.itemView,
                            "${user?.name} has deleted.",
                            Snackbar.LENGTH_LONG
                        )
                        viewmodel.userListLiveData.value?.removeAt(viewHolder.adapterPosition)
                        usersAdapter.notifyItemRemoved(viewHolder.adapterPosition)
                        usersAdapter.notifyItemRangeChanged(
                            viewHolder.adapterPosition,
                            usersAdapter.itemCount
                        )
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
        delMessage.setAction("Cancel") {
            viewmodel.userListLiveData.value?.add(user)
            usersAdapter.notifyItemRangeChanged(
                position,
                usersAdapter.itemCount
            )
        }
    }
}